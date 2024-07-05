package com.team4.moviereview.domain.search.model

import com.team4.moviereview.domain.category.model.Category
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "search_category")
class SearchCategory(

    @ManyToOne
    @JoinColumn(name = "category_id")
    val category: Category,

    @Column(name = "searched_at")
    val searchedAt: LocalDate,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_category_id", nullable = false)
    val id: Long? = null
}