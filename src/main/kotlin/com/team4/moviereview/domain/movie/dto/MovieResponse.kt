package com.team4.moviereview.domain.movie.dto

import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.movie.model.Movie
import java.util.Date

data class MovieResponse(
    val title: String,
    val director: String,
    val actors: String,
    val category: List<Category>,
    val releaseDate: Date,
    val rating: Double,
) {
    companion object {
        fun from(movie: Movie, category: List<Category>, rating: Double) : MovieResponse {
            return MovieResponse(
                title = movie.getTitle(),
                director = movie.getDirect(),
                actors = movie.getActor(),
                category = category,
                releaseDate = movie.getReleaseDate(),
                rating = rating
            )
        }
    }
}
