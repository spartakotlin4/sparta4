package com.team4.moviereview.domain.movie.dto

import java.time.LocalDate

data class FilterRequest(
    val releasedDate: LocalDate?,
    val after : Boolean,
    val overRated: Double = 0.0,
)
