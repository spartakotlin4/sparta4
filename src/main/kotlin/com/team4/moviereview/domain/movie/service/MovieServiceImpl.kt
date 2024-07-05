package com.team4.moviereview.domain.movie.service

import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.category.repository.CategoryRepository
import com.team4.moviereview.domain.movie.dto.*
import com.team4.moviereview.domain.movie.model.Movie
import com.team4.moviereview.domain.movie.repository.movieRepository.MovieRepository
import com.team4.moviereview.domain.search.service.SearchService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class MovieServiceImpl(
    private val movieRepository: MovieRepository,
    private val searchService: SearchService,
    private val categoryRepository: CategoryRepository,
) : MovieService {

    override fun getMovieList(pageable: Pageable, cursor: CursorRequest): CursorPageResponse {
        val movies = movieRepository.getMoviesByCursor(pageable, cursor)
        val movieList = movies.first
        val categoryMap = createCategoryMap(movies.second)
        val movieListWithCategory = movieCombineWithCategory(movieList, categoryMap)
        val pagingMovieList = createCursorPageResponse(movieListWithCategory, cursor.orderBy, pageable)

        return pagingMovieList
    }

    override fun getMovieDetails(movieId: Long, pageable: Pageable): MovieDetailResponse {
        val movieDetails = movieRepository.getMovieDetails(pageable, movieId)

        return movieDetails
    }

    override fun searchMovies(keyword: String, pageable: Pageable): List<MovieResponse> {
        val movies = movieRepository.searchMovies(keyword, pageable)
        val movieList = movies.first
        val categoryMap = createCategoryMap(movies.second)
        val movieListWithCategory = movieCombineWithCategory(movieList, categoryMap)
        searchService.saveSearchedKeyword(keyword)

        return movieListWithCategory
    }

    override fun filterMovies(request: FilterRequest, pageable: Pageable): List<MovieResponse> {
        val movies = movieRepository.filterMovies(request, pageable)
        val movieList = movies.first
        val categoryMap = createCategoryMap(movies.second)
        val movieListWithCategory = movieCombineWithCategory(movieList, categoryMap)

        return movieListWithCategory
    }

    override fun getMoviesByCategory(categoryName: String): List<MovieResponse> {
        val movies = movieRepository.getMoviesByCategory(categoryName)
        val category : Category? = categoryRepository.findByName(categoryName)
        if(category != null ) {
            searchService.saveSearchedCategory(category)
        }
        val movieList = movies.first
        val categoryMap = createCategoryMap(movies.second)
        val movieListWithCategory = movieCombineWithCategory(movieList, categoryMap)
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

    private fun createCategoryMap(movieByCategories: List<IdCategory>): MutableMap<Long, MutableList<String>> {
        val categoryMap: MutableMap<Long, MutableList<String>> = mutableMapOf<Long, MutableList<String>>()
        movieByCategories.forEach {
            if (!categoryMap.containsKey(it.movieId)) {
                categoryMap[it.movieId] = mutableListOf()
            }
            categoryMap[it.movieId]?.add(it.categoryName)
        }
        return categoryMap
    }

    private fun movieCombineWithCategory(movies: List<MovieData>, categoryMap: MutableMap<Long, MutableList<String>>): List<MovieResponse> {
        return movies.map{
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

}

