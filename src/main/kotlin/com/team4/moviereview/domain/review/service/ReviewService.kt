package com.team4.moviereview.domain.review.service

import com.team4.moviereview.domain.movie.repository.MovieRepository
import com.team4.moviereview.domain.review.dto.ReviewRequest
import com.team4.moviereview.domain.review.dto.ReviewResponse
import com.team4.moviereview.domain.review.model.Review
import com.team4.moviereview.domain.review.repository.ReviewRepository
import jakarta.persistence.Id
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException
import java.time.LocalDateTime

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val movieRepository: MovieRepository,
) {

    @Transactional
    fun addReview( movieId: Long, reviewRequest: ReviewRequest):ReviewResponse{

        val movie = movieRepository.findByIdOrNull(movieId)?:throw RuntimeException("movie")

        //todo : user auth

        val review = Review.of(
            comment=reviewRequest.comment,
            rating=reviewRequest.rating,
            created_at= LocalDateTime.now(),
            member=reviewRequest.member,
            movie=movie,
        )

        return ReviewResponse.from(review)
    }

    @Transactional
    fun updateReview( movieId: Long, reviewId:Long, reviewRequest: ReviewRequest ):ReviewResponse{
        if( movieRepository.existsById(movieId) ==false){ throw RuntimeException("movie")}

        val review = reviewRepository.findByIdOrNull(reviewId) ?:throw RuntimeException("review")

        review.update(reviewRequest)
        return ReviewResponse.from(review)
    }

    @Transactional
    fun deleteReview( movieId:Long, reviewId: Long){
        if( movieRepository.existsById(movieId) ==false){ throw RuntimeException("movie")}

        val review = reviewRepository.findByIdOrNull(reviewId) ?:throw RuntimeException("review")

        reviewRepository.delete(review) //todo : soft delete 여부
    }


}