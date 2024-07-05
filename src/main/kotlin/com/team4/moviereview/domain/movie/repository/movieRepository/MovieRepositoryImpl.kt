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
            .offset(pageable.offset)
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


        val movieByCategories = queryFactory.select(
            Projections.constructor(
                IdCategory::class.java,
                movie.id,
                category.name
            )
        )
            .from(movie)
            .innerJoin(movieCategory).on(movie.eq(movieCategory.movie))
            .fetch()

        val categoryMap: MutableMap<Long, MutableList<String>> = mutableMapOf<Long, MutableList<String>>()

        movieByCategories.forEach {
            if (!categoryMap.containsKey(it.movieId)) {
                categoryMap[it.movieId] = mutableListOf()
            }
            categoryMap[it.movieId]?.add(it.categoryName)
        }


        return movies.map {
            MovieResponse(
                it.movieId,
                it.title,
                it.directors,
                it.actors,
                categoryMap[it.movieId]!!,
                it.releaseDate,
                it.rating,
            )
        }
    }

    override fun filterMovies(request: FilterRequest, pageable: Pageable): Page<MovieResponse> {
        TODO("Not yet implemented")
    }

    override fun getMoviesByCategory(categoryName: String): List<MovieResponse> {

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
            .fetch()

        val movieByCategories = queryFactory.select(
            Projections.constructor(
                IdCategory::class.java,
                movie.id,
                category.name
            )
        )
            .from(movie)
            .innerJoin(movieCategory).on(movie.eq(movieCategory.movie))
            .fetch()

        val categoryMap: MutableMap<Long, MutableList<String>> = mutableMapOf<Long, MutableList<String>>()

        movieByCategories.forEach {
            if (!categoryMap.containsKey(it.movieId)) {
                categoryMap[it.movieId] = mutableListOf()
            }
            categoryMap[it.movieId]?.add(it.categoryName)
        }


        return movies.map {
            MovieResponse(
                it.movieId,
                it.title,
                it.directors,
                it.actors,
                categoryMap[it.movieId]!!,
                it.releaseDate,
                it.rating,
            )
        }
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