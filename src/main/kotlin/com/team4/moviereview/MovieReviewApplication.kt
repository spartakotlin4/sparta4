package com.team4.moviereview

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
class MovieReviewApplication

fun main(args: Array<String>) {
    runApplication<MovieReviewApplication>(*args)
}
