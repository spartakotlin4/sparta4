package com.team4.moviereview

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableCaching
class MovieReviewApplication

fun main(args: Array<String>) {
    runApplication<MovieReviewApplication>(*args)
}
