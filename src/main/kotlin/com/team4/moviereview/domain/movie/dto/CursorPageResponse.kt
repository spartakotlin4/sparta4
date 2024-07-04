package com.team4.moviereview.domain.movie.dto

import org.springframework.data.domain.Page

data class CursorPageResponse(
    val movies: List<MovieResponse>,
    val nextCursor: String?,
    val nextCursorAssist: Long?,
)
