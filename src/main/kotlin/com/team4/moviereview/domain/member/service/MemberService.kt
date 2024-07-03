package com.team4.moviereview.domain.member.service

import com.team4.moviereview.domain.member.dto.reponse.LoginResponse
import com.team4.moviereview.domain.member.dto.reponse.RegisterResponse
import com.team4.moviereview.domain.member.dto.request.LoginRequest
import com.team4.moviereview.domain.member.dto.request.RegisterRequest

interface MemberService {
    fun registerMember(request : RegisterRequest) : RegisterResponse
    fun loginMember(request : LoginRequest): LoginResponse

}