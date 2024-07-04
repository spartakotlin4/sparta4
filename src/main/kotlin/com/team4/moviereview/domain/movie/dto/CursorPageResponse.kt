package com.team4.moviereview.domain.movie.dto

data class CursorPageResponse(
    val movies: List<MovieResponse>,
    val nextCursor: String?,
    val nextCursorAssist: Long?,
)
