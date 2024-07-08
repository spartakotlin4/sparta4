package com.team4.moviereview.domain.movie.repository.movieRepository

import com.team4.moviereview.domain.movie.dto.*
import org.springframework.data.domain.Pageable

interface CustomMovieRepository {

    fun getMoviesByCursor(pageable: Pageable, cursor: CursorRequest): List<MovieData>

    fun getMoviesCategories(moviesId: List<Long>): List<IdCategory>

    fun getMovieDetails(pageable: Pageable, movieId: Long): MovieDetailResponse

    fun searchMovies(keyword: String, pageable: Pageable): List<MovieData>

    fun filterMovies(request: FilterRequest, pageable: Pageable): List<MovieData>

    fun getMoviesByCategory(categoryName: String): List<MovieData>
}