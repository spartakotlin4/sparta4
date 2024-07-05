package com.team4.moviereview.domain.movie.service

import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.movie.dto.*
import com.team4.moviereview.domain.movie.repository.movieRepository.MovieRepository
import com.team4.moviereview.domain.review.repository.ReviewRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class MovieServiceImpl(
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository,
) : MovieService {

    override fun getMovieList(pageable: Pageable, cursor: CursorRequest): CursorPageResponse {
        val movieList = movieRepository.getMoviesByCursor(pageable, cursor)
        val pagingMovieList = createCursorPageResponse(movieList, cursor.orderBy, pageable)

        return pagingMovieList
    }

    override fun getMovieDetails(movieId: Long, pageable: Pageable): MovieDetailResponse {
        val movieDetails = movieRepository.getMovieDetails(pageable, movieId)

        return movieDetails
    }

    override fun searchMovies(keyword: String, pageable: Pageable): List<MovieResponse> {
        TODO()
    }

    override fun filterMovies(request: FilterRequest, pageable: Pageable): List<MovieResponse> {
       val movies = movieRepository.filterMovies(request, pageable)
       return movies
    }

    override fun getMoviesByCategory(categoryName: String): List<MovieResponse> {
        val movies = movieRepository.getMoviesByCategory(categoryName)

        return movies
    }

    private fun getMovieAverageRate(movieId: Long): Double {
        TODO()
    }

    private fun getMovieCategories(movieId: Long): List<Category> {
        TODO()
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

}
