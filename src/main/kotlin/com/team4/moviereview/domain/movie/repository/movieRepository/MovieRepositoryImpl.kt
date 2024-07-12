package com.team4.moviereview.domain.movie.repository.movieRepository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import com.team4.moviereview.domain.category.model.QCategory
import com.team4.moviereview.domain.member.model.QMember
import com.team4.moviereview.domain.movie.dto.CursorRequest
import com.team4.moviereview.domain.movie.dto.FilterRequest
import com.team4.moviereview.domain.movie.dto.MovieData
import com.team4.moviereview.domain.movie.dto.MovieIdAndCategoryName
import com.team4.moviereview.domain.movie.model.QMovie
import com.team4.moviereview.domain.movie.model.QMovieCategory
import com.team4.moviereview.domain.review.model.QReview
import com.team4.moviereview.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class MovieRepositoryImpl : CustomMovieRepository, QueryDslSupport() {
    private val movie = QMovie.movie
    private val category = QCategory.category
    private val movieCategory = QMovieCategory.movieCategory
    private val review = QReview.review
    private val member = QMember.member

    override fun getMoviesByCursor(pageable: Pageable, cursor: CursorRequest): List<MovieData> {
        val movies = queryFactory.select(
            Projections.constructor(
                MovieData::class.java,
                movie.id,
                movie.title,
                movie.actor,
                movie.director,
                movie.releaseDate,
                review.rating.avg()
            )
        )
            .from(movie)
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
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()


        return movies
    }

    override fun getMoviesCategories(moviesId: List<Long>): List<MovieIdAndCategoryName> {

        val categories = queryFactory.select(
            Projections.constructor(
                MovieIdAndCategoryName::class.java,
                movieCategory.movie.id,
                category.name
            )
        )
            .from(movieCategory)
            .innerJoin(movieCategory).on(movieCategory.category.eq(category))
            .where(movie.id.`in`(moviesId))
            .fetch()

        return categories
    }

    override fun getMovieDetails(movieId: Long): MovieData? {

        val movieDetail = queryFactory.select(
            Projections.constructor(
                MovieData::class.java,
                movie.id,
                movie.title,
                movie.actor,
                movie.director,
                movie.releaseDate,
                review.rating.avg(),
            )
        )
            .from(movie)
            .leftJoin(review).on(movie.eq(review.movie))
            .where(movie.id.eq(movieId))
            .groupBy(
                movie.id,
                movie.title,
                movie.director,
                movie.actor,
            )
            .fetchOne()

        return movieDetail

    }

    override fun searchMovies(keyword: String, pageable: Pageable): List<MovieData> {
        val builder = BooleanBuilder()

        if (keyword.isNotBlank()) {
            builder.and(
                movie.title.containsIgnoreCase(keyword)
                    .or(movie.actor.containsIgnoreCase(keyword))
                    .or(movie.director.containsIgnoreCase(keyword))
            )
        }

        val movies = queryFactory.select(
            Projections.constructor(
                MovieData::class.java,
                movie.id,
                movie.title,
                movie.actor,
                movie.director,
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
                movie.actor,
                movie.director,
                movie.releaseDate,
            )
            .orderBy(movie.releaseDate.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()


        return movies
    }

    override fun filterMovies(request: FilterRequest, pageable: Pageable): List<MovieData> {

        val movies = queryFactory.select(
            Projections.constructor(
                MovieData::class.java,
                movie.id,
                movie.title,
                movie.actor,
                movie.director,
                movie.releaseDate,
                review.rating.avg(),
            )
        )
            .from(movie)
            .leftJoin(review).on(movie.eq(review.movie))
            .where(filterSearch(request))
            .groupBy(
                movie.id,
            )
            .orderBy(movie.releaseDate.desc())
            .fetch()
            .filter {
                it.rating >= request.overRated
            }

        return movies
    }

    override fun getMoviesByCategory(categoryName: String, pageable: Pageable): List<MovieData> {

        // TODO : 추후에 동적으로 정렬기준이 생길수도
        // TODO : 추후에 페이지네이션으로 바꿀수도

        val movies = queryFactory.select(
            Projections.constructor(
                MovieData::class.java,
                movie.id,
                movie.title,
                movie.actor,
                movie.director,
                movie.releaseDate,
                review.rating.avg(),
            )
        )
            .from(movie)
            .leftJoin(review).on(movie.eq(review.movie))
            .innerJoin(movieCategory).on(movie.eq(movieCategory.movie))
            .where(movieCategory.category.name.eq(categoryName))
            .groupBy(
                movie.id
            )
            .orderBy(movie.releaseDate.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()


        return movies
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
                cursor.cursorAssist?.let {
                    builder.and(movie.id.lt(cursor.cursorAssist))
                }
            }

            "averageRating" -> {
                cursor.cursorRate.let {
                    builder.and(review.rating.avg().lt(it))
                }
                cursor.cursorAssist?.let {
                    builder.and(movie.id.lt(cursor.cursorAssist))
                }
            }

            else -> {
                throw IllegalArgumentException("올바른 정렬 타입을 선택해 주세요")
            }
        }
        return builder
    }

    private fun filterSearch(request: FilterRequest): BooleanBuilder {
        val builder = BooleanBuilder()

        request.releasedDate?.let {
            if (request.after)
                builder.and(movie.releaseDate.after(it))
            else
                builder.and(movie.releaseDate.before(it))
        }
        return builder
    }


}
