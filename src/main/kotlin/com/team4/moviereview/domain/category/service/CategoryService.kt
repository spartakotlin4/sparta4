package com.team4.moviereview.domain.category.service

import com.team4.moviereview.domain.category.dto.request.CategoryCreateRequest
import com.team4.moviereview.domain.category.dto.request.CategoryUpdateRequest
import com.team4.moviereview.domain.category.dto.response.CategoryResponse
import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.category.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    fun addCategory(category: CategoryCreateRequest) : CategoryResponse{
        // TODO : 이미 등록된 카테고리명인지 확인하기
        // TODO : 이미 존재하는 카테고리명이면 예외 발생
        // TODO : 존재하지 않는다면 카테고리 DB에 저장

        if (categoryRepository.existsByName(category.name)) {
            throw IllegalArgumentException("Category name already exists")
        }

        val category : Category = Category(name = category.name)

        categoryRepository.save(category)

        return CategoryResponse.from(category)
    }


    fun updateCategoryName(categoryId: Long, request: CategoryUpdateRequest): CategoryResponse {
        TODO()
    }

    fun deleteCategory(categoryId: Long): CategoryResponse {
        TODO()
    }

}
