package com.team4.moviereview.domain.category.service

import com.team4.moviereview.domain.category.dto.request.CategoryCreateRequest
import com.team4.moviereview.domain.category.dto.request.CategoryUpdateRequest
import com.team4.moviereview.domain.category.dto.response.CategoryResponse
import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.category.repository.CategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    fun addCategory(request: CategoryCreateRequest): CategoryResponse {
        // TODO : 이미 등록된 카테고리명인지 확인하기
        // TODO : 이미 존재하는 카테고리명이면 예외 발생
        // TODO : 존재하지 않는다면 카테고리 DB에 저장

        if (categoryRepository.existsByName(request.name)) {
            throw IllegalArgumentException("Category name already exists")
        }

        val category = Category(name = request.name)

        categoryRepository.save(category)

        return CategoryResponse.from(category)
    }

    @Transactional
    fun updateCategoryName(categoryId: Long, request: CategoryUpdateRequest): CategoryResponse {
        // TODO : 카테고리가 DB에 있는지 확인
        // TODO : 만약 카테고리가 없다면 예외 발생
        // TODO : 만약 변경 이름과 기존 이름이 같다면 예외 발생
        // TODO : 만약 이미 있는 이름이라면 예외처리
        // TODO : 문제가 없다면 DB에 저장 후 반환

        val category: Category =
            categoryRepository.findByIdOrNull(categoryId) ?: throw IllegalArgumentException("Category Not Found")

        if (categoryRepository.existsByName(request.name)) {
            throw IllegalArgumentException("Category name already exists")
        }

        category.updateName(request.name)

        return CategoryResponse.from(category)
    }

    @Transactional
    fun deleteCategory(categoryId: Long): Unit {
        // TODO : 카테고리가 DB에 있는지 확인
        // TODO : 만약 없다면 예외 발생
        // TODO : 존재할 경우 DB에 삭제

        val category: Category =
            categoryRepository.findByIdOrNull(categoryId) ?: throw IllegalArgumentException("Category Not Found")

        categoryRepository.delete(category)
    }

}
