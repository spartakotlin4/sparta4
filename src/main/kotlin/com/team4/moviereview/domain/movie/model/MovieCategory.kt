package com.team4.moviereview.domain.movie.model

import com.team4.moviereview.domain.category.model.Category
import jakarta.persistence.*

@Entity
@Table(name = "movie_category")
class MovieCategory(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private val movie: Movie,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private val category: Category,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_category_id")
    val id: Long? = null
}