package com.team4.moviereview.domain.review.controller

import com.team4.moviereview.domain.review.dto.ReviewRequest
import com.team4.moviereview.domain.review.dto.ReviewResponse
import com.team4.moviereview.domain.review.service.ReviewService
import com.team4.moviereview.infra.security.userprincipal.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/movies/{movieId}/reviews")
class ReviewController(
    private val reviewService: ReviewService
) {
    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    fun addReview(
        @PathVariable movieId:Long,
        @RequestBody reviewRequest: ReviewRequest,
        @AuthenticationPrincipal authenticationPrincipal: UserPrincipal,
    ): ResponseEntity<ReviewResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(reviewService.addReview(movieId,reviewRequest, authenticationPrincipal.id))
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{reviewId}")
    fun updateReview(
        @PathVariable movieId: Long,
        @PathVariable reviewId: Long,
        @RequestBody reviewRequest: ReviewRequest,
        @AuthenticationPrincipal authenticationPrincipal: UserPrincipal,
    ):ResponseEntity<ReviewResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewService.updateReview(movieId,reviewId,reviewRequest,authenticationPrincipal.id))
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @PathVariable movieId: Long,
        @PathVariable reviewId: Long,
        @AuthenticationPrincipal authenticationPrincipal: UserPrincipal,
    ):ResponseEntity<Unit>{
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(reviewService.deleteReview(movieId,reviewId,authenticationPrincipal.id))
    }


}