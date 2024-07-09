package com.team4.moviereview.domain.movie.controller

import com.team4.moviereview.domain.movie.dto.*
import com.team4.moviereview.domain.movie.service.MovieService
import com.team4.moviereview.infra.aop.StopWatch
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/movies")
class MovieController(
    private val movieService: MovieService
) {

    @GetMapping("/list")
    fun getMovieList(
        pageable: Pageable,
        @ModelAttribute cursor: CursorRequest
    ): ResponseEntity<CursorPageResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(movieService.getMovieList(pageable, cursor))
    }

    @GetMapping("/{movie-id}")
    fun getMovieDetails(
        @PathVariable("movie-id") movieId: Long,
        pageable: Pageable,
    ): ResponseEntity<MovieDetailResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(movieService.getMovieDetails(movieId, pageable))
    }

    @StopWatch
    @GetMapping("/search")
    fun searchMovies(
        @RequestParam(value = "keyword", required = true) keyword: String,
        pageable: Pageable,
    ): ResponseEntity<List<MovieResponse>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(movieService.searchMovies(keyword, pageable))
    }

    @StopWatch
    @GetMapping("/search/v2")
    fun searchMoviesWithCache(
        @RequestParam(value = "keyword", required = true) keyword: String,
        pageable: Pageable,
    ): ResponseEntity<List<MovieResponse>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(movieService.searchMoviesWithCache(keyword, pageable))
    }


    @StopWatch
    @GetMapping("/filter")
    fun filterMovies(
        @ModelAttribute request: FilterRequest,
        pageable: Pageable,
    ): ResponseEntity<List<MovieResponse>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(movieService.filterMovies(request, pageable))
    }


    @StopWatch
    @GetMapping("/category")
    fun getMoviesByCategory(
        @RequestParam(value = "categoryName") categoryName: String
    ): ResponseEntity<List<MovieResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(movieService.getMoviesByCategory(categoryName))
    }
}