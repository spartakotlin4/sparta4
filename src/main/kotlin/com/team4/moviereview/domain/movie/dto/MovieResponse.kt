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
) {

    companion object {
        fun from(movie: MovieData, categories: List<Category>): MovieResponse {
            return MovieResponse(
                id = movie.id,
                title = movie.title,
                director = movie.director,
                actors = movie.actors,
                category = categories,
                releaseDate = movie.releaseDate,
                rating = movie.rating,
            )
        }
    }
}


