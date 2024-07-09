package com.team4.moviereview.domain.search.service

import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.search.dto.SearchCategoryResponse
import com.team4.moviereview.domain.search.dto.SearchWordResponse
import com.team4.moviereview.domain.search.model.SearchCategory
import com.team4.moviereview.domain.search.model.SearchWord
import com.team4.moviereview.domain.search.repository.SearchCategoryRepository
import com.team4.moviereview.domain.search.repository.SearchWordRepository
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SearchService(
    private val searchWordRepository: SearchWordRepository,
    private val searchCategoryRepository: SearchCategoryRepository
) {
    private val rankLimit = 10

    fun getPopularKeywords(): List<SearchWordResponse> {
        return searchWordRepository.findAllByLimit(rankLimit)
    }

    fun getPopularCategory(): List<SearchCategoryResponse> {
        return searchCategoryRepository.findAllByLimit(rankLimit)
    }

    fun saveSearchedKeyword(keyword: String) {
        searchWordRepository.save(SearchWord(keyword, LocalDate.now()))
    }

    fun saveSearchedCategory(category: Category) {
        searchCategoryRepository.save(SearchCategory(category, LocalDate.now()))
    }

    @Cacheable("popular-categories")
    fun getPopularCategoryWithCache(): List<SearchCategoryResponse> {
        return searchCategoryRepository.findAllByLimit(rankLimit)
    }

    @CachePut("popular-categories")
    fun refreshPopularCategoryWithCache(): List<SearchCategoryResponse> {
        return searchCategoryRepository.findAllByLimit(rankLimit)
    }
}