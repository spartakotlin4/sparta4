package com.team4.moviereview.domain.movie.repository.movieRepository

import com.team4.moviereview.domain.category.model.QCategory
import com.team4.moviereview.domain.movie.model.QMovie
import com.team4.moviereview.domain.movie.model.QMovieCategory
import com.team4.moviereview.domain.review.model.QReview
import com.team4.moviereview.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class MovieRepositoryImpl : CustomMovieRepository, QueryDslSupport() {
    private val movie = QMovie.movie
    private val category = QCategory.category
    private val movieCategory = QMovieCategory.movieCategory
    private val review = QReview.review

    override fun getMoviesByCursor(pageable: Pageable, cursor: CursorRequest): List<MovieResponse> {
        val query = queryFactory.select(
            Projections.constructor(
            MovieResponse::class.java,
                movie.id,
                movie.title,
                movie.director,
                movie.actor,
                movieCategory.category,
                movie.releaseDate,
                review.rating.avg()
            )
        )
            .from(movie)
            .innerJoin(movieCategory).on(movie.eq(movieCategory.movie))
            .innerJoin(review).on(movie.eq(review.movie))
            .groupBy(
                movie.id,
                movie.title,
                movie.director,
                movie.actor,
                movie.releaseDate,
            )
            .having(applyCursorPosition(cursor))
            .applyOrderBy(cursor.orderBy)
            .limit(pageable.pageSize.toLong())
            .fetch()
        return query
    }

    override fun getMovieDetails(pageable: Pageable, movieId: Long): MovieDetailResponse {
        TODO("Not yet implemented")
    }

    override fun searchMovies(request: SearchRequest, pageable: Pageable): Page<MovieResponse> {
        TODO("Not yet implemented")
    }

    override fun filterMovies(request: FilterRequest, pageable: Pageable): Page<MovieResponse> {
        TODO("Not yet implemented")
    }

    private fun <T> JPAQuery<T>.applyOrderBy(orderBy: String): JPAQuery<T> {
        return when (orderBy) {
            "releaseDate" -> this.orderBy(movie.releaseDate.desc(), movie.id.desc())
            "averageRating" -> this.orderBy(review.rating.avg().desc(), movie.id.desc())
            else -> throw IllegalArgumentException("올바른 정렬 타입을 입력해 주세요")
        }
    }

    private fun applyCursorPosition(cursor: CursorRequest): BooleanBuilder {
        val builder = BooleanBuilder()
        when (cursor.orderBy) {
            "releaseDate" -> {
                cursor.cursorTime.let {
                    builder.and(movie.releaseDate.lt(cursor.cursorTime))
                }
            }
            "averageRating" -> {
                cursor.cursorRate.let {
                    builder.and(review.rating.avg().lt(it))
                }
            }
            else -> {
                throw IllegalArgumentException("올바른 정렬 타입을 선택해 주세요")
            }
        }
        return builder
    }

}