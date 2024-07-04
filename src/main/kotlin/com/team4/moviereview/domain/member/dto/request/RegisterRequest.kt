package com.team4.moviereview.domain.member.dto.request

data class RegisterRequest(
    val nickname: String,
    val email: String,
    val password: String,
    val passwordConfirm:String
)
