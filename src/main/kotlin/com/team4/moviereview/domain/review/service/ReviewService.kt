package com.team4.moviereview.domain.review.service



import com.team4.moviereview.domain.member.repository.MemberRepository
import com.team4.moviereview.domain.movie.repository.movieRepository.MovieRepository
import com.team4.moviereview.domain.review.dto.ReviewRequest
import com.team4.moviereview.domain.review.dto.ReviewResponse
import com.team4.moviereview.domain.review.model.Review
import com.team4.moviereview.domain.review.repository.ReviewRepository
import com.team4.moviereview.infra.security.jwt.JwtPlugin
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
    private val memberRepository: MemberRepository,

) {

    @Transactional
    fun addReview( movieId: Long, reviewRequest: ReviewRequest,userId:Long):ReviewResponse{

        val movie = movieRepository.findByIdOrNull(movieId)?:throw RuntimeException("movie")

        val member = memberRepository.findByIdOrNull(userId)?:throw RuntimeException("user")

        val review = Review.of(
            comment=reviewRequest.comment,
            rating=reviewRequest.rating,
            created_at= LocalDateTime.now(),
            member= member,
            movie=movie,
        )

        return ReviewResponse.from(review)
    }

    @Transactional
    fun updateReview( movieId: Long, reviewId:Long, reviewRequest: ReviewRequest, userId: Long):ReviewResponse{
        if( movieRepository.existsById(movieId) ==false){ throw RuntimeException("movie")}

        val review = reviewRepository.findByIdOrNull(reviewId) ?:throw RuntimeException("review")
        val currentMember = memberRepository.findByIdOrNull(userId)?:throw RuntimeException("user")

        if(review.member.id != currentMember.id){
            throw RuntimeException("user")
        }


        review.update(reviewRequest)
        return ReviewResponse.from(review)
    }

    @Transactional
    fun deleteReview( movieId:Long, reviewId: Long, userId: Long){
        if( movieRepository.existsById(movieId) ==false){ throw RuntimeException("movie")}

        val review = reviewRepository.findByIdOrNull(reviewId) ?:throw RuntimeException("review")
        val currentMember = memberRepository.findByIdOrNull(userId)?:throw RuntimeException("user")

        if(review.member.id != currentMember.id){
            throw RuntimeException("user")
        }

        reviewRepository.delete(review) //todo : soft delete 여부
    }


}