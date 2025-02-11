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
        val resultCache = cacheManager.getCache("trendingResultCache")
        val keywordCache = cacheManager.getCache("trendingKeywordCache")!!


        //1. 캐시에서 데이터 조회
        val cachedMovies = resultCache?.get(keyword)?.get() as? List<MovieResponse>
        if (!cachedMovies.isNullOrEmpty()) {
            return cachedMovies
        }

        //2. 캐시미스 -> db에서 조회
        val movieListWithCategory = searchMoviesInDB(keyword, pageable)

        //3. 검색 키워드 저장
        searchService.saveKeywordInCache(keyword)

        // 4. trendingKeywordCache에 존재하는 keyword일 경우, 검색결과캐시에 저장
        val isTrendingKeyword = keywordCache.get("trendKeyword")?.get() as? List<SearchWordResponse>
        val keywordExistsInCache = isTrendingKeyword?.any { it.keyword == keyword } ?: false

        if (keywordExistsInCache) {
            resultCache?.put(keyword, movieListWithCategory)
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

    override fun getMoviesByCategory(categoryName: String): List<MovieResponse> {
        return getMovieByCategoryInDB(categoryName)
    }

    override fun getMoviesByCategoryWithCache(categoryName: String): List<MovieResponse> {
        val resultCache = cacheManager.getCache("trendingCategoryResult")
        val categoryCache = cacheManager.getCache("popularCategoriesCache")!!

        val cachedMovies = resultCache?.get(categoryName)?.get() as? List<MovieResponse>
        if (!cachedMovies.isNullOrEmpty()) {
            return cachedMovies
        }

        val movieListWithCategory = getMovieByCategoryInDBWittCache(categoryName)

        val isTrendingKeyword = categoryCache.get("trendCategories")?.get() as? List<SearchCategoryResponse>
        val keywordExistsInCache = isTrendingKeyword?.any { it.categoryName == categoryName } ?: false

        if (keywordExistsInCache) {
            resultCache?.put(categoryName, movieListWithCategory)
        }

        return movieListWithCategory
    }

    private fun getMovieByCategoryInDB(categoryName: String): List<MovieResponse> {
        val movies = movieRepository.getMoviesByCategory(categoryName)
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

    private fun getMovieByCategoryInDBWittCache(categoryName: String): List<MovieResponse> {
        val movies = movieRepository.getMoviesByCategory(categoryName)
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
                it.actors,
                categoryMap[it.movieId]!!,
                it.releaseDate,
                it.rating,
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
            actors = movie.actors,
            category = categoryMap[movie.movieId]!!,
            releaseDate = movie.releaseDate,
            rating = movie.rating,
            reviews = reviews
        )
    }

    override fun evictMovieListCache() {
        cacheManager.getCache("trendingResultCache")?.clear()
        cacheManager.getCache("trendingCategoryResult")?.clear()
    }

}




