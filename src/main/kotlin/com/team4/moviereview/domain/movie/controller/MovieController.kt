package com.team4.moviereview.domain.movie.controller

import com.team4.moviereview.domain.movie.dto.*
import com.team4.moviereview.domain.movie.service.MovieService
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
        @RequestParam pageable: Pageable,
        @ModelAttribute cursor: CursorRequest
    ): ResponseEntity<CursorPageResponse> {
        return try {
            ResponseEntity.status(HttpStatus.OK)
                .body(movieService.getMovieList(pageable, cursor))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_GATEWAY).build()
        }
    }

    @GetMapping("/{movie-id}")
    fun getMovieDetails(
        @PathVariable ("movie-id") movieId: Long,
        @RequestParam pageable: Pageable,
    ): ResponseEntity<MovieDetailResponse> {
        return try {
            ResponseEntity.status(HttpStatus.OK)
                .body(movieService.getMovieDetails(movieId, pageable))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/search")
    fun searchMovies(
        @ModelAttribute request: SearchRequest,
        @RequestParam pageable: Pageable,
    ): ResponseEntity<List<MovieResponse>> {
        return try {
            ResponseEntity.status(HttpStatus.OK)
                .body(movieService.searchMovies(request, pageable))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @GetMapping("/filter")
    fun filterMovies(
        @ModelAttribute request: FilterRequest,
        @RequestParam pageable: Pageable,
    ): ResponseEntity<List<MovieResponse>> {
        return try {
            ResponseEntity.status(HttpStatus.OK)
                .body(movieService.filterMovies(request, pageable))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}