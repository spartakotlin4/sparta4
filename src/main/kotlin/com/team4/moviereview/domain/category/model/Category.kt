package com.team4.moviereview.domain.category.model

import jakarta.persistence.*

@Entity
@Table(name = "category")
class Category(
    @Column(name = "name", nullable = false)
    var name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    var id: Long? = null,
) {

}
