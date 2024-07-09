package com.team4.moviereview.domain.search.controller

import com.team4.moviereview.domain.search.dto.SearchCategoryResponse
import com.team4.moviereview.domain.search.dto.SearchWordResponse
import com.team4.moviereview.domain.search.service.SearchService
import com.team4.moviereview.infra.aop.StopWatch
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/popular")
class SearchController(
    private val searchService: SearchService,
) {

    @StopWatch
    @GetMapping("/v1/keyword")
    fun getPopularKeyword(): ResponseEntity<List<SearchWordResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getPopularKeywords())
    }

    @StopWatch
    @GetMapping("/v1/category")
    fun getPopularCategory(): ResponseEntity<List<SearchCategoryResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getPopularCategory())
    }

    @StopWatch
    @GetMapping("/v2/keyword")
    fun getPopularKeywordWithCache(): ResponseEntity<List<SearchWordResponse>> {

        return ResponseEntity.status(HttpStatus.OK).body(searchService.getPopularKeywordWithCache(1))
    }

    @StopWatch
    @GetMapping("/v2/category")
    fun getPopularCategoryWithCache(): ResponseEntity<List<SearchCategoryResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getPopularCategoryWithCache())
    }

//    // 스케쥴러 도입시 컨트롤러에서 사라지고 service 것만 실행
//    @GetMapping("/v2/keyword/delete")
//    fun getPopularKeywordWithCacheEvict(@RequestParam key : String) : ResponseEntity<String> {
//        return ResponseEntity.status(HttpStatus.OK).body(searchService.getPopularKeywordCacheEvict(key))
//
//    }

//    @GetMapping("save")
//    fun save() : ResponseEntity<String> {
//        return ResponseEntity.status(HttpStatus.CREATED).body(searchService.save())
//    }
//
//    @GetMapping("delete")
//    fun delete() : ResponseEntity<String> {
//        searchService.cacheEvict()
//        return ResponseEntity.status(HttpStatus.CREATED).body("성공!")
//    }

}