package com.team4.moviereview.domain.movie.repository.movieRepository

import com.team4.moviereview.domain.movie.model.Movie
import org.springframework.data.jpa.repository.JpaRepository

interface MovieRepository: JpaRepository<Movie, Long>, CustomMovieRepository {
}