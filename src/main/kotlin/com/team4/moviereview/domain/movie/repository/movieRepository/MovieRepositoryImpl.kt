package com.team4.moviereview.domain.movie.repository.movieRepository

import com.team4.moviereview.domain.category.model.QCategory
import com.team4.moviereview.domain.movie.dto.*
import com.team4.moviereview.domain.movie.model.QMovie
import com.team4.moviereview.domain.movie.model.QMovieCategory
import com.team4.moviereview.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class MovieRepositoryImpl: CustomMovieRepository, QueryDslSupport() {
    private val movie = QMovie.movie
    private val category = QCategory.category
    private val movieCategory = QMovieCategory.movieCategory
    override fun getMoviesByCursor(pageable: Pageable, cursor: CursorRequest): List<MovieResponse> {
        TODO("Not yet implemented")
    }

    override fun getMovieDetails(pageable: Pageable, movieId: Long): MovieDetailResponse {
        TODO("Not yet implemented")
    }

    override fun searchMovies(request: SearchRequest, pageable: Pageable): Page<MovieResponse> {
        TODO("Not yet implemented")
    }

    override fun filterMovies(request: FilterRequest, pageable: Pageable): Page<MovieResponse> {
        TODO("Not yet implemented")
    }
}