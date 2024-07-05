package com.team4.moviereview.domain.movie.dto

import java.time.LocalDate

data class MovieData(
    val movieId: Long,
    val title: String,
    val actors: String,
    val directors: String,
    val releaseDate: LocalDate,
    val rating: Double
)