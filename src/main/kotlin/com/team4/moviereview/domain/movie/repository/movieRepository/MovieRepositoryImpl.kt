package com.team4.moviereview.domain.movie.repository.movieRepository

import com.team4.moviereview.domain.category.model.QCategory
import com.team4.moviereview.domain.movie.model.QMovie
import com.team4.moviereview.domain.movie.model.QMovieCategory
import com.team4.moviereview.infra.querydsl.QueryDslSupport
import org.springframework.stereotype.Repository

@Repository
class MovieRepositoryImpl: CustomMovieRepository, QueryDslSupport() {
    private val movie = QMovie.movie
    private val category = QCategory.category
    private val movieCategory = QMovieCategory.movieCategory
}