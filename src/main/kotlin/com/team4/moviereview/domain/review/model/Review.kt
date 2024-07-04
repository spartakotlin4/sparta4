package com.team4.moviereview.domain.review.model

import com.team4.moviereview.domain.movie.model.Movie
import com.team4.moviereview.domain.review.dto.ReviewRequest
import jakarta.persistence.*
import org.springframework.cglib.core.Local
import java.lang.reflect.Member
import java.time.LocalDateTime

@Entity
@Table(name="review")
class Review(

    @Column(name="comment", nullable=false)
    var comment : String,

    @Column(name="rating", nullable = false)
    var rating : Float,

    @Column(name="created_at", nullable = false)
    var created_at : LocalDateTime,

    @ManyToOne
    @JoinColumn(name="member_id", nullable = false)
    var member: Member,

    @ManyToOne
    @JoinColumn(name="movie_id", nullable=false)
    var movie: Movie,


) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long?=null

    fun update(reviewRequest: ReviewRequest){
        this.comment = reviewRequest.comment
        this.rating = reviewRequest.rating
    }

    companion object{
        fun of( comment: String, rating: Float, created_at: LocalDateTime, member: Member, movie: Movie):Review{

            //validate

            return Review(
                comment=comment,
                rating=rating,
                created_at=created_at,
                member=member,
                movie=movie,
            )

        }
    }
}

