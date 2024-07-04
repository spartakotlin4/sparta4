package com.team4.moviereview.domain.movie.dto

import com.team4.moviereview.domain.category.model.Category
import java.time.LocalDate

data class MovieResponse(
    val id: Long,
    val title: String,
    val director: String,
    val actors: String,
    val category: List<Category>,
    val releaseDate: LocalDate,
    val rating: Double,
)
