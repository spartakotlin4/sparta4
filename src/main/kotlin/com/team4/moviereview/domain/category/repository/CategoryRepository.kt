package com.team4.moviereview.domain.category.repository

import com.team4.moviereview.domain.category.model.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long> {

    fun existsByName(name: String): Boolean
    fun findByName(categoryName: String): Category?
}
