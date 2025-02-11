package com.team4.moviereview.infra.scheduler

import com.team4.moviereview.domain.category.service.CategoryService
import com.team4.moviereview.domain.movie.service.MovieService
import com.team4.moviereview.domain.search.service.SearchService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ScheduledTasks(
    private val searchService: SearchService,
    private val categoryService: CategoryService,
    private val movieService: MovieService
) {

    private val logger = LoggerFactory.getLogger(ScheduledTasks::class.java)


    @Scheduled(cron = "0 */10 * * * ?")
    fun refreshCategories() {
        categoryService.refreshCategory()
        logger.info("${LocalDateTime.now()} 에 refreshCategories 를 실행했습니다")
    }

    @Scheduled(cron = "0 0 * * * ?")
    fun refreshPopularCategories() {
        searchService.refreshPopularCategoryWithCache()
        logger.info("${LocalDateTime.now()} 에 refreshPopularCategory 를 실행했습니다")
    }

    @Scheduled(cron = "0 */1 * * * ?")
    fun saveKeyword() {
        searchService.saveCachingKeywordInDB()
        logger.info("${LocalDateTime.now()} 에 saveCachingKeywordInDB 를 실행했습니다")
    }

    @Scheduled(cron = "30 */1 * * * ?")
    fun saveCategory() {
        searchService.saveCachingCategoryInDB()
        logger.info("${LocalDateTime.now()} 에 saveCachingCategoryInDB 를 실행했습니다")
    }

    @Scheduled(cron = "0 0 4 * * ?")
    fun evictListCache() {
        movieService.evictMovieListCache()
    }

}