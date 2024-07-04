package com.team4.moviereview.domain.movie.dto

import java.util.Date

data class FilterRequest(
    val afterReleasedDate: Date?,
    val overRated: Double?,
)
