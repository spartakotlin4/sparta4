package com.team4.moviereview.domain.movie.dto

import java.util.*

data class CursorRequest(
    val cursorTime: Date?,
    val cursorRate: Double?,
    val orderBy: String
)
