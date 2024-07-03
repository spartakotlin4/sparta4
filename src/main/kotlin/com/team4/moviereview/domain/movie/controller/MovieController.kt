package com.team4.moviereview.domain.movie.controller

import com.team4.moviereview.domain.movie.dto.FilterRequest
import com.team4.moviereview.domain.movie.dto.MovieDetailResponse
import com.team4.moviereview.domain.movie.dto.MovieResponse
import com.team4.moviereview.domain.movie.dto.SearchRequest
import com.team4.moviereview.domain.movie.service.MovieService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/movies")
class MovieController(
    private val movieService: MovieService
) {

    @GetMapping("/list")
    fun getMovieList(): ResponseEntity<List<MovieResponse>> {
        return try {
            ResponseEntity.status(HttpStatus.OK)
                .body(movieService.getMovieList())
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_GATEWAY).build()
        }
    }

    @GetMapping("/{movie-id}")
    fun getMovieDetails(
        @PathVariable ("movie-id") movieId: Long
    ): ResponseEntity<MovieDetailResponse> {
        return try {
            ResponseEntity.status(HttpStatus.OK)
                .body(movieService.getMovieDetails(movieId))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/search")
    fun searchMovies(
        @ModelAttribute request: SearchRequest
    ): ResponseEntity<MovieResponse> {
        return try {
            ResponseEntity.status(HttpStatus.OK)
                .body(movieService.searchMovies(request))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @GetMapping("/filter")
    fun filterMovies(
        @ModelAttribute request: FilterRequest,
    ): ResponseEntity<MovieResponse> {
        return try {
            ResponseEntity.status(HttpStatus.OK)
                .body(movieService.filterMovies(request))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}