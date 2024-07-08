package com.team4.moviereview.domain.review.repository

import com.team4.moviereview.domain.review.dto.ReviewResponse
import org.springframework.data.domain.Pageable

interface CustomReviewRepository {

    fun getReviews(movieId: Long, pageable: Pageable) : List<ReviewResponse>

}