package com.team4.moviereview.domain.movie.dto

import java.time.LocalDate

data class FilterRequest(
    val afterReleasedDate: LocalDate?,
    val overRated: Double?,
)
