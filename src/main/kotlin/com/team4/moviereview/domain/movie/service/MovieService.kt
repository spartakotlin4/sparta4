package com.team4.moviereview.domain.movie.service

import com.team4.moviereview.domain.movie.dto.*
import org.springframework.data.domain.Pageable

interface MovieService {
    fun getMovieList(pageable: Pageable, cursor: CursorRequest): CursorPageResponse

    fun getMovieDetails(movieId: Long, pageable: Pageable): MovieDetailResponse

    fun searchMovies(keyword: String, pageable: Pageable): List<MovieResponse>

    fun searchMoviesWithCache(keyword: String, pageable: Pageable): List<MovieResponse>

    fun filterMovies(request: FilterRequest, pageable: Pageable): List<MovieResponse>

    fun getMoviesByCategory(categoryName: String): List<MovieResponse>

    fun getMoviesByCategoryWithCache(categoryName: String): List<MovieResponse>

    fun evictMovieListCache()
}