package com.team4.moviereview.domain.review.repository

import com.team4.moviereview.domain.review.model.Review
import org.springframework.data.jpa.repository.JpaRepository


interface ReviewRepository :JpaRepository<Review,Long>{


}