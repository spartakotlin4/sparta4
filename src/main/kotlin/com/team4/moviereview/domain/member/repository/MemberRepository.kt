package com.team4.moviereview.domain.member.repository

import com.team4.moviereview.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun existsByEmail(email: String) :Boolean
    fun findByEmail(email: String): Member?
}



