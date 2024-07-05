package com.team4.moviereview.domain.search.service

import com.team4.moviereview.domain.search.dto.SearchCategoryResponse
import com.team4.moviereview.domain.search.dto.SearchWordResponse
import com.team4.moviereview.domain.search.repository.SearchCategoryRepository
import com.team4.moviereview.domain.search.repository.SearchWordRepository
import org.springframework.stereotype.Service

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
}