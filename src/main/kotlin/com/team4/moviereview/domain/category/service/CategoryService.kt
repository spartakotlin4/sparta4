package com.team4.moviereview.domain.category.service

import com.team4.moviereview.domain.category.dto.request.CategoryCreateRequest
import com.team4.moviereview.domain.category.dto.request.CategoryUpdateRequest
import com.team4.moviereview.domain.category.dto.response.CategoryResponse
import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.category.repository.CategoryRepository
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    fun addCategory(request: CategoryCreateRequest): CategoryResponse {

        if (categoryRepository.existsByName(request.name)) {
            throw RuntimeException("Category name already exists")
        }

        val category = Category(name = request.name)

        categoryRepository.save(category)

        refreshCategory()

        return CategoryResponse.from(category)
    }

    @Transactional
    fun updateCategoryName(categoryId: Long, request: CategoryUpdateRequest): CategoryResponse {

        val category: Category =
            categoryRepository.findByIdOrNull(categoryId) ?: throw RuntimeException("Category Not Found")

        if (categoryRepository.existsByName(request.name)) {
            throw RuntimeException("Category name already exists")
        }

        category.updateName(request.name)

        refreshCategory()

        return CategoryResponse.from(category)
    }

    @Transactional
    fun deleteCategory(categoryId: Long): Unit {

        val category: Category =
            categoryRepository.findByIdOrNull(categoryId) ?: throw RuntimeException("Category Not Found")

        categoryRepository.delete(category)
    }

    @Cacheable("category")
    fun getCachingCategory(): List<Category> {
        return categoryRepository.findAll()
    }

    @CachePut("category")
    fun refreshCategory(): List<Category> {
        return categoryRepository.findAll()
    }
}
