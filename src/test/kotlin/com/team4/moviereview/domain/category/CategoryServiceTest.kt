package com.team4.moviereview.domain.category

import com.team4.moviereview.domain.category.dto.request.CategoryCreateRequest
import com.team4.moviereview.domain.category.dto.request.CategoryUpdateRequest
import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.category.repository.CategoryRepository
import com.team4.moviereview.domain.category.service.CategoryService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class CategoryServiceTest @Autowired constructor(
    private val categoryRepository: CategoryRepository
) {
    private var categoryService = CategoryService(categoryRepository)


    @Test
    fun `카테고리를 생성할 때 없는 이름이면 생성된다`() {
        // Given 카테고리를 생성할 때
        val request = CategoryCreateRequest(name = "액션")

        // When 중복 명칭이 아니면
        val result = categoryService.addCategory(request)

        // Then DB에 저장된다.
        result.name shouldBe "액션"
    }

    @Test
    fun `카테고리를 생성할 때 있는 이름이면 예외가 발생한다`() { // aaa( arrange act assert ) gwt
        // Given 카테고리를 생성할 때
        categoryRepository.saveAndFlush(Category(name = "코미디"))
        val request = CategoryCreateRequest(name = "코미디")

        // When & Then 중복 명칭이면 에러가 발생한다.
        shouldThrow<RuntimeException> { categoryService.addCategory(request) }
            .let {
                it.message shouldBe "Category name already exists"
            }
    }

    @Test
    fun `카테고리 수정 시 이미 있는 카테고리명 예외 발생`() {
        // Given
        val categories = categoryRepository.saveAllAndFlush(listOf((Category(name = "호러")), Category(name = "로맨스")))
        val request = CategoryUpdateRequest(name = "로맨스")

        // When & Then
        shouldThrow<RuntimeException> { categoryService.updateCategoryName(categories[0].id!!, request) }
            .let {
                it.message shouldBe "Category name already exists"
            }
    }

    @Test
    fun `카테고리가 수정 되는 경우`() {

        // Given
        val categories = categoryRepository.saveAllAndFlush(listOf((Category(name = "호러")), Category(name = "로맨스")))
        val request = CategoryUpdateRequest(name = "모험")

        // When
        val result = categoryService.updateCategoryName(categories[0].id!!, request)

        // Then
        result.name shouldBe "모험"

    }

    @Test
    fun `카테고리가 삭제된다`() {
        // Given
        val category = categoryRepository.saveAndFlush(Category(name = "로맨스"))

        // When
        categoryService.deleteCategory(categoryId = category.id!!)

        // Then
    }

}