package com.team4.moviereview.domain.movie.repository.movieRepository

import com.team4.moviereview.domain.movie.dto.CursorRequest
import com.team4.moviereview.domain.movie.dto.FilterRequest
import com.team4.moviereview.domain.movie.dto.MovieData
import com.team4.moviereview.domain.movie.dto.MovieIdAndCategoryName
import org.springframework.data.domain.Pageable

interface CustomMovieRepository {

    fun getMoviesByCursor(pageable: Pageable, cursor: CursorRequest): List<MovieData>

    fun getMoviesCategories(moviesId: List<Long>): List<MovieIdAndCategoryName>

    fun getMovieDetails(movieId: Long): MovieData?

    fun searchMovies(keyword: String, pageable: Pageable): List<MovieData>

    fun filterMovies(request: FilterRequest, pageable: Pageable): List<MovieData>

    fun getMoviesByCategory(categoryName: String): List<MovieData>

}