package com.team4.moviereview.domain.movie.service

import com.team4.moviereview.domain.movie.dto.FilterRequest
import com.team4.moviereview.domain.movie.dto.MovieDetailResponse
import com.team4.moviereview.domain.movie.dto.MovieResponse
import com.team4.moviereview.domain.movie.dto.SearchRequest
import org.springframework.stereotype.Service

@Service
interface MovieService {
    fun getMovieList(): List<MovieResponse>

    fun getMovieDetails(movieId: Long): MovieDetailResponse

    fun searchMovies(request: SearchRequest): MovieResponse

    fun filterMovies(request: FilterRequest): MovieResponse
}