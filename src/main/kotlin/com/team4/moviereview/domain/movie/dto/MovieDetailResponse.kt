package com.team4.moviereview.domain.movie.dto

import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.movie.model.Movie
import com.team4.moviereview.domain.review.model.Review
import java.util.Date

data class MovieDetailResponse(
    val title: String,
    val director: String,
    val description: String,
    val actors: String,
    val category: List<Category>,
    val releaseDate: Date,
    val rating: Double,
    val reviews: List<Review>
) {
    companion object {
        fun from(movie: Movie, category: List<Category>, rating: Double, reviews: List<Review>): MovieDetailResponse {
            return MovieDetailResponse(
                title = movie.getTitle(),
                director = movie.getDirect(),
                description = movie.getDescription(),
                actors = movie.getActor(),
                category = category,
                releaseDate = movie.getReleaseDate(),
                rating = rating,
                reviews = reviews
            )
        }
    }
}
