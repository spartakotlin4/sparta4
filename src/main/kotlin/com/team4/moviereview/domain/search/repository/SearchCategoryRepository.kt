package com.team4.moviereview.domain.search.repository

import com.team4.moviereview.domain.search.dto.SearchCategoryResponse
import com.team4.moviereview.domain.search.model.SearchCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface SearchCategoryRepository : JpaRepository<SearchCategory, Long> {

    @Query("select new com.team4.moviereview.domain.search.dto.SearchCategoryResponse (c.category.name, count(c)) from SearchCategory c where c.searchedAt >= :startDate group by c.category order by count(c) desc, c.category.name desc limit :rankLimit")
    fun findAllByLimit(rankLimit: Int, startDate: LocalDate): List<SearchCategoryResponse>
}