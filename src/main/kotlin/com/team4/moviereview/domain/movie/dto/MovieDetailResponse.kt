package com.team4.moviereview.domain.movie.dto

import com.team4.moviereview.domain.category.dto.response.CategoryResponse
import com.team4.moviereview.domain.review.dto.ReviewResponse
import java.time.LocalDate

data class MovieDetailResponse(
    val id: Long,
    val title: String,
    val director: String,
    val actors: String,
    val category: List<String>,
    val releaseDate: LocalDate,
    val rating: Double,
    val reviews: List<ReviewResponse>
)
