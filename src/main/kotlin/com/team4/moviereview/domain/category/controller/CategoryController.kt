package com.team4.moviereview.domain.category.controller

import com.team4.moviereview.domain.category.dto.request.CategoryCreateRequest
import com.team4.moviereview.domain.category.dto.request.CategoryUpdateRequest
import com.team4.moviereview.domain.category.dto.response.CategoryResponse
import com.team4.moviereview.domain.category.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    @PostMapping()
    fun addCategory(@RequestBody request: CategoryCreateRequest): ResponseEntity<CategoryResponse> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(request))
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @PutMapping("/{category-id}")
    fun updateCategoryName(
        @PathVariable(name = "category-id") categoryId: Long,
        @RequestBody request: CategoryUpdateRequest
    ): ResponseEntity<CategoryResponse> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategoryName(categoryId, request))
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @DeleteMapping("/{category-id}")
    fun deleteCategory(@PathVariable(name = "category-id") categoryId: Long): ResponseEntity<Unit> {
        return try {
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(categoryService.deleteCategory(categoryId))
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

}