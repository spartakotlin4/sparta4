package com.team4.moviereview.domain.search.controller

import com.team4.moviereview.domain.category.dto.response.CategoryResponse
import com.team4.moviereview.domain.search.dto.SearchCategoryResponse
import com.team4.moviereview.domain.search.dto.SearchWordResponse
import com.team4.moviereview.domain.search.service.SearchService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/popular")
class SearchController(
    private val searchService: SearchService,
) {

    @GetMapping("/keyword")
    fun getPopularKeyword(): ResponseEntity<List<SearchWordResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getPopularKeywords())
    }

    @GetMapping("/category")
    fun getPopularCategory(): ResponseEntity<List<SearchCategoryResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getPopularCategory())
    }

}