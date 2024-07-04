package com.team4.moviereview.domain.review.model

import com.team4.moviereview.domain.member.model.Member
import com.team4.moviereview.domain.movie.model.Movie
import com.team4.moviereview.domain.review.dto.ReviewRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "review")
class Review(

    @Column(name = "comment", nullable = false)
    var comment: String,

    @Column(name = "rating", nullable = false)
    var rating: Float,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    var movie: Movie,


    ) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    var id: Long? = null

    fun update(reviewRequest: ReviewRequest) {
        this.comment = reviewRequest.comment
        this.rating = reviewRequest.rating
    }

    companion object {
        fun of(comment: String, rating: Float, member: Member, movie: Movie): Review {

            //validate

            return Review(
                comment = comment,
                rating = rating,
                member = member,
                movie = movie,
            )

        }
    }
}

