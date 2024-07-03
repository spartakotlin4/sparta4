package com.team4.moviereview.domain.category.dto.response

import com.team4.moviereview.domain.category.model.Category

data class CategoryResponse(
    val categoryId: Long,
    val name: String
) {
    companion object {
        fun from (category: Category) : CategoryResponse {
            return CategoryResponse(
                categoryId = category.id!!,
                name = category.name
            )
        }
    }
}

