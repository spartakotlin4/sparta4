package com.team4.moviereview.domain.review.controller

import com.team4.moviereview.domain.review.dto.ReviewRequest
import com.team4.moviereview.domain.review.dto.ReviewResponse
import com.team4.moviereview.domain.review.service.ReviewService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/movies/{movieId}/reviews")
class ReviewController(
    private val reviewService: ReviewService
) {

    @PostMapping()
    fun addReview(
        @PathVariable movieId:Long,
        @RequestBody reviewRequest: ReviewRequest,
    ): ResponseEntity<ReviewResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(reviewService.addReview(movieId,reviewRequest))
    }

    @PutMapping("/{reviewId}")
    fun updateReview(
        @PathVariable movieId: Long,
        @PathVariable reviewId: Long,
        @RequestBody reviewRequest: ReviewRequest,
    ):ResponseEntity<ReviewResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewService.updateReview(movieId,reviewId,reviewRequest))
    }

    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @PathVariable movieId: Long,
        @PathVariable reviewId: Long,
    ):ResponseEntity<Unit>{
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(reviewService.deleteReview(movieId,reviewId))
    }


}