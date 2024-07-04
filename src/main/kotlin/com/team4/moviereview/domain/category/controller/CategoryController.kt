package com.team4.moviereview.domain.category.controller

import com.team4.moviereview.domain.category.dto.request.CategoryCreateRequest
import com.team4.moviereview.domain.category.dto.request.CategoryUpdateRequest
import com.team4.moviereview.domain.category.dto.response.CategoryResponse
import com.team4.moviereview.domain.category.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val categoryService: CategoryService
) {
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    fun addCategory(@RequestBody request: CategoryCreateRequest): ResponseEntity<CategoryResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(request))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{category-id}")
    fun updateCategoryName(
        @PathVariable(name = "category-id") categoryId: Long,
        @RequestBody request: CategoryUpdateRequest
    ): ResponseEntity<CategoryResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategoryName(categoryId, request))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{category-id}")
    fun deleteCategory(@PathVariable(name = "category-id") categoryId: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(categoryService.deleteCategory(categoryId))
    }

}