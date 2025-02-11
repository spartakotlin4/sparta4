package com.team4.moviereview.domain.search.repository

import com.team4.moviereview.domain.search.dto.SearchWordResponse
import com.team4.moviereview.domain.search.model.SearchWord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface SearchWordRepository : JpaRepository<SearchWord, Int> {

    @Query("select new com.team4.moviereview.domain.search.dto.SearchWordResponse( s.keyword, count(s)) from SearchWord s where s.searchedAt >= :startDate group by s.keyword order by count(s) desc, s.keyword desc limit :limitRank")
    fun findAllByLimit(limitRank: Int, startDate: LocalDate): List<SearchWordResponse>

}