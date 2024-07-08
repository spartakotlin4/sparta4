package com.team4.moviereview.domain.movie.dto

import java.time.LocalDate

data class CursorRequest(
    val cursorTime: LocalDate?,
    val cursorRate: Double?,
    val orderBy: String,
    val cursorAssist: Long?,
)
