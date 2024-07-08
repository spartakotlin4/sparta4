package com.team4.moviereview.domain.review.repository

import com.querydsl.core.types.Projections
import com.team4.moviereview.domain.member.model.QMember
import com.team4.moviereview.domain.review.dto.ReviewResponse
import com.team4.moviereview.domain.review.model.QReview
import com.team4.moviereview.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ReviewRepositoryImpl : CustomReviewRepository, QueryDslSupport() {
    private val review = QReview.review
    private val member = QMember.member


    override fun getReviews(movieId: Long, pageable: Pageable): List<ReviewResponse> {
        val reviews = queryFactory.select(
            Projections.constructor(
                ReviewResponse::class.java,
                review.id,
                review.comment,
                review.rating,
                review.member.nickname,
                review.createdAt
            )
        )
            .from(review)
            .innerJoin(review).on(review.member.eq(member))
            .where(review.movie.id.eq(movieId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return reviews
    }

}