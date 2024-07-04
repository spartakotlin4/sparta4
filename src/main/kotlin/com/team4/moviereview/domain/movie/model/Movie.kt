package com.team4.moviereview.domain.movie.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "movie")
class Movie(

    @Column(name = "actor", nullable = false)
    private var actor: String,

    @Column(name = "director", nullable = false)
    private var director: String,

    @Column(name = "title", nullable = false)
    private var title: String,

    @Column(name = "release", nullable = false)
    private var releaseDate: LocalDate,

    ) {
    @Column(name = "movie_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun getActor() = this.actor

    fun getDirect() = this.director

    fun getTitle() = this.title

    fun getReleaseDate() = this.releaseDate

    fun update(actor: String, direct: String, title: String, description: String, releaseDate: LocalDate) {
        this.actor = actor
        this.director = direct
        this.title = title
        this.releaseDate = releaseDate
    }

}