package com.team4.moviereview.domain.search.service

import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.search.dto.SearchCategoryResponse
import com.team4.moviereview.domain.search.dto.SearchWordResponse
import com.team4.moviereview.domain.search.model.SearchCategory
import com.team4.moviereview.domain.search.model.SearchWord
import com.team4.moviereview.domain.search.repository.SearchCategoryRepository
import com.team4.moviereview.domain.search.repository.SearchWordRepository
import org.springframework.cache.annotation.CacheEvict
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
    private val memoryCacheKeywords: MutableMap<String, Long> = mutableMapOf()
    private val memoryCacheCategories: MutableMap<String, Long> = mutableMapOf()

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

    @Cacheable(value = ["trendingKeywordCache"])
    fun getPopularKeywordWithCache(): List<SearchWordResponse> {
        return searchWordRepository.findAllByLimit(rankLimit)
    }

    @CacheEvict(value = ["trendingKeywordCache"])
    fun getPopularKeywordCacheEvict(): String {
        return "인기 키워드 지우기 성공"
    }


    @Cacheable("popular-categories")
    fun getPopularCategoryWithCache(): List<SearchCategoryResponse> {
        return searchCategoryRepository.findAllByLimit(rankLimit)
    }

    @CachePut("popular-categories")
    fun refreshPopularCategoryWithCache(): List<SearchCategoryResponse> {
        return searchCategoryRepository.findAllByLimit(rankLimit)
    }



    @CachePut(value = ["trendingKeywordCache"], key = "'keywordList'")
    fun saveSearchedKeywordWithCache(keyword: String): List<SearchWordResponse> {
        val count = memoryCacheKeywords[keyword] ?: 0L
        memoryCacheKeywords[keyword] = count + 1
        return getSortedKeywords()
    }

    @CachePut(value = ["trendingCategoryCache"], key = "'categoryList'")
    fun saveSearchedCategoryWithCache(categoryName: String): List<SearchCategoryResponse> {
        val count = memoryCacheCategories[categoryName] ?: 0L
        memoryCacheCategories[categoryName] = count + 1
        return getSortedCategories()
    }








    private fun getSortedKeywords(): List<SearchWordResponse> {
        return memoryCacheKeywords.entries
            .sortedByDescending { it.value }
            .take(rankLimit)
            .map { SearchWordResponse(it.key, it.value) }
    }

    private fun getSortedCategories(): List<SearchCategoryResponse> {
        return memoryCacheCategories.entries
            .sortedByDescending { it.value }
            .take(rankLimit)
            .map { SearchCategoryResponse(it.key, it.value) }
    }



}
























