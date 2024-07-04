package com.team4.moviereview.domain.member.controller

import com.team4.moviereview.domain.member.dto.reponse.LoginResponse
import com.team4.moviereview.domain.member.dto.reponse.RegisterResponse
import com.team4.moviereview.domain.member.dto.request.LoginRequest
import com.team4.moviereview.domain.member.dto.request.RegisterRequest
import com.team4.moviereview.domain.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping
@RestController
class MemberController(
    val memberService: MemberService
) {

    @PostMapping("/register")
    fun registerMember(@RequestBody request : RegisterRequest): ResponseEntity<RegisterResponse> {
     return try {
         ResponseEntity.status(HttpStatus.CREATED).body(memberService.registerMember(request))
     }
     catch (e:IllegalStateException){
         ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
     }
    }




    @PostMapping("/login")
    fun loginMember(@RequestBody request : LoginRequest): ResponseEntity<LoginResponse> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(memberService.loginMember(request))
        }
        catch (e:IllegalStateException){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}