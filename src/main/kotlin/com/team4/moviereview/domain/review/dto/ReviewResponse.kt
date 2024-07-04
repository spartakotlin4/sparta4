package com.team4.moviereview.domain.review.dto

import com.team4.moviereview.domain.review.model.Review
import java.time.LocalDateTime

data class ReviewResponse(
    val reviewId: Long,
    val comment: String,
    val rating: Float,
    val member: String, //todo : Member ?
    val createdAt: LocalDateTime,

    ) {
    companion object {
        fun from(review: Review): ReviewResponse {
            return ReviewResponse(
                reviewId = review.id!!,
                comment = review.comment,
                rating = review.rating,
                member = review.member.nickname,
                createdAt = review.createdAt,
            )
        }
    }
}

