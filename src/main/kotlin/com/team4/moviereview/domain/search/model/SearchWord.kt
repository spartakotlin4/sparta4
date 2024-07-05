package com.team4.moviereview.domain.search.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "saerch_word")
class SearchWord(

    @Column(name = "keyword", nullable = false)
    val keyword: String,

    @Column(name = "searched_at", nullable = false)
    val searchedAt: LocalDate,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_word_id")
    val id: Long? = null
}