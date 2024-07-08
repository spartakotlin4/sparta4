package com.team4.moviereview.domain.review.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.team4.moviereview.domain.review.model.Review
import java.time.LocalDateTime

data class ReviewResponse(
    val reviewId: Long,
    val comment: String,
    val rating: Float,
    val member: String, //todo : Member ?
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
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

