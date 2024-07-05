package com.team4.moviereview.domain.search.repository

import com.team4.moviereview.domain.search.dto.SearchCategoryResponse
import com.team4.moviereview.domain.search.model.SearchCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SearchCategoryRepository: JpaRepository<SearchCategory, Long> {

    @Query("select c.category.name, count(c) as cnt from SearchCategory c group by c.category order by cnt desc, c.category.name desc limit :rankLimit")
    fun findAllByLimit(rankLimit: Int): List<SearchCategoryResponse>
}