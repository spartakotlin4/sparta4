package com.team4.moviereview.domain.review.dto

import com.team4.moviereview.domain.review.model.Review
import java.time.LocalDateTime

data class ReviewResponse (
    val comment:String,
    val rating:Float,
    val member:String, //todo : Member ?
    val created_at:LocalDateTime,

        ){
    companion object{
        fun from(review:Review) : ReviewResponse{

            return ReviewResponse(
            comment = review.comment,
            rating = review.rating,
            member = review.member.name,
            created_at = review.created_at,
            )
        }

        fun from(reviews: List<Review>):List<ReviewResponse> = reviews.map{
            review -> ReviewResponse(
            comment = review.comment,
            rating = review.rating,
            member = review.member.name,
            created_at = review.created_at,
            )
        }
    }
}

