package com.team4.moviereview.domain.movie.repository.movieRepository

import com.team4.moviereview.domain.movie.dto.CursorRequest
import com.team4.moviereview.domain.movie.dto.FilterRequest
import com.team4.moviereview.domain.movie.dto.MovieDetailResponse
import com.team4.moviereview.domain.movie.dto.MovieResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomMovieRepository {

    fun getMoviesByCursor(pageable: Pageable, cursor: CursorRequest): List<MovieResponse>

    fun getMovieDetails(pageable: Pageable, movieId: Long): MovieDetailResponse

    fun searchMovies(keyword: String, pageable: Pageable): Page<MovieResponse>

    fun filterMovies(request: FilterRequest, pageable: Pageable): Page<MovieResponse>

    fun getMoviesByCategory(categoryName: String): List<MovieResponse>
}