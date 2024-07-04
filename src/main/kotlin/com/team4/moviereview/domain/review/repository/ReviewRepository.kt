package com.team4.moviereview.domain.review.repository

import com.team4.moviereview.domain.review.model.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReviewRepository :JpaRepository<Review,Long>{


}