package com.team4.moviereview.domain.search.service

import com.team4.moviereview.domain.category.model.Category
import com.team4.moviereview.domain.search.dto.SearchCategoryResponse
import com.team4.moviereview.domain.search.dto.SearchWordResponse
import com.team4.moviereview.domain.search.model.SearchCategory
import com.team4.moviereview.domain.search.model.SearchWord
import com.team4.moviereview.domain.search.repository.SearchCategoryRepository
import com.team4.moviereview.domain.search.repository.SearchWordRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class SearchService(
    private val searchWordRepository: SearchWordRepository,
    private val searchCategoryRepository: SearchCategoryRepository,
    private val cacheManager: CacheManager,
) {
    private val startDate = LocalDate.now().minusDays(1)
    private val rankLimit = 10

    fun getPopularKeywords(): List<SearchWordResponse> {
        return searchWordRepository.findAllByLimit(rankLimit, startDate)
    }

    fun getPopularCategory(): List<SearchCategoryResponse> {
        return searchCategoryRepository.findAllByLimit(rankLimit,startDate)
    }

    fun saveSearchedKeyword(keyword: String) {
        searchWordRepository.save(SearchWord(keyword, LocalDate.now()))
    }

    fun saveSearchedCategory(category: Category) {
        searchCategoryRepository.save(SearchCategory(category, LocalDate.now()))
    }

    @Cacheable(value = ["trendingKeywordCache"], key = "'trendKeyword'")
    fun getPopularKeywordWithCache(): List<SearchWordResponse> {
        return searchWordRepository.findAllByLimit(rankLimit, startDate)
    }


    @Cacheable(value = ["popularCategoriesCache"], key = "'trendCategories'")
    fun getPopularCategoryWithCache(): List<SearchCategoryResponse> {
        return searchCategoryRepository.findAllByLimit(rankLimit, startDate)
    }

    @CachePut(value = ["popularCategoriesCache"], key = "'trendCategories'")
    fun refreshPopularCategoryWithCache(): List<SearchCategoryResponse> {
        return searchCategoryRepository.findAllByLimit(rankLimit, startDate)
    }

    @CachePut(value = ["hotKeyword"], key = "'keywordCache'")
    fun saveKeywordInCache(keyword: String): MutableMap<Long, String> {
        val cache = cacheManager.getCache("hotKeyword")
        val cacheValue = cache?.get("keywordCache")?.get() as? MutableMap<Long, String> ?: mutableMapOf()

        val newId = (cacheValue.keys.maxOrNull() ?: 0L) + 1
        cacheValue[newId] = keyword

        cache?.put("keywordCache", cacheValue)
        return cacheValue
    }

    @Transactional
    fun saveCachingKeywordInDB() {
        val cache = cacheManager.getCache("hotKeyword")
        val cacheValue = cache?.get("keywordCache")?.get() as? Map<Long, String>

        cacheValue?.let { cachedWords ->
            val searchWords = cachedWords.map { SearchWord(it.value, LocalDate.now()) }
            searchWordRepository.saveAll(searchWords)
        }

        cache?.evict("keywordCache")
    }

    @CachePut(value = ["hotCategory"], key = "'categoryCache'")
    fun saveCategoryInCache(category: Category): MutableMap<Long, Category> {
        val cache = cacheManager.getCache("hotCategory")
        val cacheValue = cache?.get("categoryCache")?.get() as? MutableMap<Long, Category> ?: mutableMapOf()

        val newId = (cacheValue.keys.maxOrNull() ?: 0L) + 1
        cacheValue[newId] = category
        cache?.put("hotCategory", cacheValue)
        return cacheValue
    }

    @Transactional
    fun saveCachingCategoryInDB() {
        val cache = cacheManager.getCache("hotCategory")
        val cacheValue = cache?.get("categoryCache")?.get() as? MutableMap<Long, Category> ?: mutableMapOf()

        cacheValue.let { cachedWords ->
            val searchCategory = cachedWords.map { SearchCategory(it.value, LocalDate.now()) }
            searchCategoryRepository.saveAll(searchCategory)
        }
        cache?.evict("hotCategory")
    }


}
