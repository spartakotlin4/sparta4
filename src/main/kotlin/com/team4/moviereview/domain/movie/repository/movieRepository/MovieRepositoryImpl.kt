package com.team4.moviereview.domain.movie.repository.movieRepository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import com.team4.moviereview.domain.category.dto.response.CategoryResponse
import com.team4.moviereview.domain.category.model.QCategory
import com.team4.moviereview.domain.member.model.QMember
import com.team4.moviereview.domain.movie.dto.*
import com.team4.moviereview.domain.movie.model.QMovie
import com.team4.moviereview.domain.movie.model.QMovieCategory
import com.team4.moviereview.domain.review.dto.ReviewResponse
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
    private val member = QMember.member

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
            .leftJoin(review).on(movie.eq(review.movie))
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

        val movieDetail = queryFactory.select(
            Projections.constructor(
                MovieDetailData::class.java,
                movie.id,
                movie.title,
                movie.director,
                movie.actor,
                movie.releaseDate,
                review.rating.avg(),
            )
        )
            .from(movie)
            .innerJoin(movieCategory).on(movie.eq(movieCategory.movie))
            .leftJoin(review).on(movie.eq(review.movie))
            .innerJoin(category).on(category.eq(movieCategory.category))
            .where(movie.id.eq(movieId))
            .groupBy(
                movie.id,
                movie.title,
                movie.director,
                movie.actor,
            )
            .fetchOne()!!

        val reviews = queryFactory.select(
            Projections.constructor(
                ReviewResponse::class.java,
                review.id,
                review.comment,
                review.rating,
                review.member.nickname,
                review.createdAt
            )
        )
            .from(review)
            .innerJoin(member).on(review.member.eq(member))
            .where(review.movie.id.eq(movieId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val category = queryFactory.select(
            Projections.constructor(
                CategoryResponse::class.java,
                category.id,
                category.name
            )
        )
            .from(category)
            .innerJoin(movieCategory).on(category.eq(movieCategory.category))
            .where(movieCategory.movie.id.eq(movieId))
            .fetch()


        return MovieDetailResponse(
            id = movieDetail.id,
            title = movieDetail.title,
            director = movieDetail.director,
            actors = movieDetail.actors,
            category = category,
            releaseDate = movieDetail.releaseDate,
            rating = movieDetail.rating,
            reviews = reviews
        )
    }

    override fun searchMovies(keyword: String, pageable: Pageable): List<MovieResponse> {
        val builder = BooleanBuilder()

        if (keyword.isNotBlank()) {
            builder.and(
                movie.title.containsIgnoreCase(keyword)
                    .or(movie.actor.containsIgnoreCase(keyword))
                    .or(movie.director.containsIgnoreCase(keyword))
            )
        }

        val movie = queryFactory.select(
            Projections.constructor(
                MovieData::class.java,
                movie.id,
                movie.title,
                movie.director,
                movie.actor,
                movie.releaseDate,
                review.rating.avg()
            )
        )
            .from(movie)
            .leftJoin(review).on(movie.eq(review.movie))
            .where(builder)
            .groupBy(
                movie.id,
                movie.title,
                movie.director,
                movie.actor,
                movie.releaseDate,
            )
            .orderBy(movie.releaseDate.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()


        val movieResponses = movie.map { movieData ->
            val categories = queryFactory.selectFrom(category)
                .innerJoin(movieCategory).on(category.eq(movieCategory.category))
                .where(movieCategory.movie.id.eq(movieData.id))
                .fetch()

            MovieResponse.from(movieData, categories)
        }

        return movieResponses
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