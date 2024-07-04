package com.team4.moviereview.domain.movie.service

import com.team4.moviereview.domain.movie.dto.*
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
interface MovieService {
    fun getMovieList(pageable: Pageable, cursor: CursorRequest): CursorPageResponse

    fun getMovieDetails(movieId: Long, pageable: Pageable): MovieDetailResponse

    fun searchMovies(request: SearchRequest, pageable: Pageable): List<MovieResponse>

    fun filterMovies(request: FilterRequest, pageable: Pageable): List<MovieResponse>
}