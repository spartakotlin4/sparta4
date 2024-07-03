package com.team4.moviereview.domain.movie.model

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "movie")
class Movie(

    private var actor: String,

    private var direct: String,

    private var title: String,

    private var description: String,

    private var releaseDate: Date,

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun getActor() = this.actor

    fun getDirect() = this.direct

    fun getTitle() = this.title

    fun getDescription() = this.description

    fun getReleaseDate() = this.releaseDate

    fun update(actor: String, direct: String, title: String, description: String, releaseDate: Date) {
        this.actor = actor
        this.direct = direct
        this.title = title
        this.description = description
        this.releaseDate = releaseDate
    }

}