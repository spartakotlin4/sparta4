package com.team4.moviereview.domain.movie.dto

import jakarta.validation.constraints.Pattern

data class SearchRequest(
    val keyword: String,
    val categoryName: List<String>,
    val orderBy: String,
)
