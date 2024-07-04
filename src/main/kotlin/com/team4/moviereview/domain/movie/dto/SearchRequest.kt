package com.team4.moviereview.domain.movie.dto

data class SearchRequest(
    val keyword: String?,
    val categoryName: List<String>,
    val orderBy: String,
)
