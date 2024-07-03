package com.team4.moviereview.domain.movie.dto

data class FilterRequest(
    val afterReleasedDate: String,
    val overRated: Double,
    val overViewCount: Int
)
