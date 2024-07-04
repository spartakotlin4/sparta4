package com.team4.moviereview.domain.movie.dto

import com.team4.moviereview.domain.category.dto.response.CategoryResponse
import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.movie.model.Movie
import com.team4.moviereview.domain.review.dto.ReviewResponse
import com.team4.moviereview.domain.review.model.Review
import java.util.Date

data class MovieDetailResponse(
    val id: Long,
    val title: String,
    val director: String,
    val description: String,
    val actors: String,
    val category: List<CategoryResponse>?,
    val releaseDate: Date,
    val rating: Double,
    val reviews: List<ReviewResponse>?
)
