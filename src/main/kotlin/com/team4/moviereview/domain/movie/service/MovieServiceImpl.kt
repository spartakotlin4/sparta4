package com.team4.moviereview.domain.movie.service

import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.category.repository.CategoryRepository
import com.team4.moviereview.domain.movie.dto.*
import com.team4.moviereview.domain.movie.repository.movieRepository.MovieRepository
import com.team4.moviereview.domain.review.dto.ReviewResponse
import com.team4.moviereview.domain.review.repository.ReviewRepository
import com.team4.moviereview.domain.search.dto.SearchCategoryResponse
import com.team4.moviereview.domain.search.dto.SearchWordResponse
import com.team4.moviereview.domain.search.service.SearchService
import com.team4.moviereview.infra.exception.ModelNotFoundException
import org.springframework.cache.CacheManager
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.math.round

@Service
class MovieServiceImpl(
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository,
    private val searchService: SearchService,
    private val categoryRepository: CategoryRepository,
    private val cacheManager: CacheManager,
) : MovieService {


    override fun getMovieList(pageable: Pageable, cursor: CursorRequest): CursorPageResponse {
        val movies = movieRepository.getMoviesByCursor(pageable, cursor)
        val moviesId = movies.map { it.movieId }
        val movieIdAndCategoriesName = movieRepository.getMoviesCategories(moviesId)
        val categories = createCategoryMap(movieIdAndCategoriesName)
        val movieListWithCategory = movieCombineWithCategory(movies, categories)
        val pagingMovieList = createCursorPageResponse(movieListWithCategory, cursor.orderBy, pageable)

        return pagingMovieList
    }

    override fun getMovieDetails(movieId: Long, pageable: Pageable): MovieDetailResponse {
        val movie = movieRepository.getMovieDetails(movieId) ?: throw ModelNotFoundException("Movie", movieId)

        val reviews = reviewRepository.getReviews(movie.movieId, pageable)

        val movieIdAndCategoriesName = movieRepository.getMoviesCategories(listOf(movie.movieId))

        val categories = createCategoryMap(movieIdAndCategoriesName)

        val movieDetailsResponse = movieDetailsWithReviewsAndCategories(movie, reviews, categories)

        return movieDetailsResponse
    }

    override fun searchMovies(keyword: String, pageable: Pageable): List<MovieResponse> {
        searchService.saveSearchedKeyword(keyword)
        return searchMoviesInDB(keyword, pageable)
    }

    private fun searchMoviesInDB(keyword: String, pageable: Pageable): List<MovieResponse> {
        val movies = movieRepository.searchMovies(keyword, pageable)
        val moviesId = movies.map { it.movieId }
        val movieIdAndCategoriesName = movieRepository.getMoviesCategories(moviesId)
        val categories = createCategoryMap(movieIdAndCategoriesName)

        return movieCombineWithCategory(movies, categories)

    }

    override fun searchMoviesWithCache(keyword: String, pageable: Pageable): List<MovieResponse> {
        //1. 검색 키워드 저장
        searchService.saveKeywordInCache(keyword)

        val resultCache = cacheManager.getCache("trendingResultCache")
        val keywordCache = cacheManager.getCache("trendingKeywordCache")!!


        val pageNum = pageable.pageNumber
        //2. 캐시에서 데이터 조회
        val cachedMovies = resultCache?.get("${keyword}${pageNum}")?.get() as? List<MovieResponse>
        if (!cachedMovies.isNullOrEmpty()) {
            return cachedMovies
        }

        //3. 캐시미스 -> db에서 조회
        val movieListWithCategory = searchMoviesInDB(keyword, pageable)

        // 4. trendingKeywordCache에 존재하는 keyword일 경우, 검색결과캐시에 저장
        val isTrendingKeyword = keywordCache.get("trendKeyword")?.get() as? List<SearchWordResponse>
        val keywordExistsInCache = isTrendingKeyword?.any { it.keyword == keyword && pageNum == 0 } ?: false

        if (keywordExistsInCache) {
            resultCache?.put("${keyword}${pageNum}", movieListWithCategory)
        }

        return movieListWithCategory
    }


    override fun filterMovies(request: FilterRequest, pageable: Pageable): List<MovieResponse> {
        val movies = movieRepository.filterMovies(request, pageable)
        val moviesId = movies.map { it.movieId }
        val movieIdAndCategoriesName = movieRepository.getMoviesCategories(moviesId)
        val categories = createCategoryMap(movieIdAndCategoriesName)
        val movieListWithCategory = movieCombineWithCategory(movies, categories)

        return movieListWithCategory
    }

    override fun getMoviesByCategory(categoryName: String, pageable: Pageable): List<MovieResponse> {
        return getMovieByCategoryInDB(categoryName, pageable)
    }

    override fun getMoviesByCategoryWithCache(categoryName: String, pageable: Pageable): List<MovieResponse> {

        val resultCache = cacheManager.getCache("trendingCategoryResult")
        val categoryCache = cacheManager.getCache("popularCategoriesCache")!!

        val pageNum = pageable.pageNumber

        val cachedMovies = resultCache?.get("${categoryName}${pageNum}")?.get() as? List<MovieResponse>
        if (!cachedMovies.isNullOrEmpty()) {
            val category: Category? = categoryRepository.findByName(categoryName)
            if (category != null) {
                searchService.saveCategoryInCache(category)
            }
            return cachedMovies
        }


        val movieListWithCategory = getMovieByCategoryInDBWittCache(categoryName, pageable)
        val isTrendingKeyword = categoryCache.get("trendCategories")?.get() as? List<SearchCategoryResponse>
        val keywordExistsInCache = isTrendingKeyword?.any { it.categoryName == categoryName && pageNum == 0 } ?: false

        if (keywordExistsInCache) {
            resultCache?.put("${categoryName}${pageNum}", movieListWithCategory)
        }

        return movieListWithCategory
    }

    private fun getMovieByCategoryInDB(categoryName: String, pageable: Pageable): List<MovieResponse> {
        val movies = movieRepository.getMoviesByCategory(categoryName, pageable)
        val moviesId = movies.map { it.movieId }
        val movieIdAndCategoriesName = movieRepository.getMoviesCategories(moviesId)
        val categories = createCategoryMap(movieIdAndCategoriesName)
        val movieListWithCategory = movieCombineWithCategory(movies, categories)

        val category: Category? = categoryRepository.findByName(categoryName)
        if (category != null) {
            searchService.saveSearchedCategory(category)
        }

        return movieListWithCategory
    }

    private fun getMovieByCategoryInDBWittCache(categoryName: String, pageable: Pageable): List<MovieResponse> {
        val movies = movieRepository.getMoviesByCategory(categoryName, pageable)
        val moviesId = movies.map { it.movieId }
        val movieIdAndCategoriesName = movieRepository.getMoviesCategories(moviesId)
        val categories = createCategoryMap(movieIdAndCategoriesName)
        val movieListWithCategory = movieCombineWithCategory(movies, categories)

        val category: Category? = categoryRepository.findByName(categoryName)
        if (category != null) {
            searchService.saveCategoryInCache(category)
        }

        return movieListWithCategory
    }


    private fun createCursorPageResponse(
        movieList: List<MovieResponse>,
        cursorType: String,
        pageable: Pageable
    ): CursorPageResponse {
        val nextCursor = if (movieList.size == pageable.pageSize) {
            when (cursorType) {
                "releaseDate" -> movieList.last().releaseDate.toString()
                "rating" -> movieList.last().rating.toString()
                else -> throw RuntimeException("올바른 정렬 타입을 선택해 주세요")
            }
        } else null

        val nextCursorAssist = if (movieList.size == pageable.pageSize) {
            movieList.last().id
        } else null

        return CursorPageResponse(movieList.toList(), nextCursor, nextCursorAssist)
    }

    private fun createCategoryMap(movieByCategories: List<MovieIdAndCategoryName>): MutableMap<Long, MutableList<String>> {
        val categoryMap: MutableMap<Long, MutableList<String>> = mutableMapOf<Long, MutableList<String>>()
        movieByCategories.forEach {
            if (!categoryMap.containsKey(it.movieId)) {
                categoryMap[it.movieId] = mutableListOf()
            }
            categoryMap[it.movieId]?.add(it.categoryName)
        }
        return categoryMap
    }

    private fun movieCombineWithCategory(
        movies: List<MovieData>,
        categoryMap: MutableMap<Long, MutableList<String>>
    ): List<MovieResponse> {
        return movies.map {
            MovieResponse(
                it.movieId,
                it.title,
                it.directors,
                it.actors.split("#").filter { it1 -> it1.isNotEmpty() },
                categoryMap[it.movieId] ?: mutableListOf(),
                it.releaseDate,
                round(it.rating * 100) / 100.0,
            )
        }
    }

    private fun movieDetailsWithReviewsAndCategories(
        movie: MovieData,
        reviews: List<ReviewResponse>,
        categoryMap: MutableMap<Long, MutableList<String>>
    ): MovieDetailResponse {

        return MovieDetailResponse(
            id = movie.movieId,
            title = movie.title,
            director = movie.directors,
            actors = movie.actors.split("#").filter { it.isNotEmpty() },
            category = categoryMap[movie.movieId]!!,
            releaseDate = movie.releaseDate,
            round(movie.rating * 100) / 100.0,
            reviews = reviews
        )
    }

    override fun evictMovieListCache() {
        cacheManager.getCache("trendingResultCache")?.clear()
        cacheManager.getCache("trendingCategoryResult")?.clear()
    }

}




