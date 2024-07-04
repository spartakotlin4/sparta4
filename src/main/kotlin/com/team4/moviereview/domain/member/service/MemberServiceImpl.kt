package com.team4.moviereview.domain.member.service

import com.team4.moviereview.domain.member.dto.reponse.LoginResponse
import com.team4.moviereview.domain.member.dto.reponse.RegisterResponse
import com.team4.moviereview.domain.member.dto.request.LoginRequest
import com.team4.moviereview.domain.member.dto.request.RegisterRequest
import com.team4.moviereview.domain.member.model.Member
import com.team4.moviereview.domain.member.model.toResponse
import com.team4.moviereview.domain.member.repository.MemberRepository
import com.team4.moviereview.infra.security.jwt.JwtPlugin
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
): MemberService {

    @Transactional
    override fun registerMember(request: RegisterRequest): RegisterResponse {
       if(memberRepository.existsByEmail(request.email))
           throw  IllegalArgumentException("Email already exists")

      if(request.password != request.passwordConfirm)
          throw IllegalArgumentException("Passwords don't match")


       return memberRepository.save(
           Member(
               email = request.email,
               password = passwordEncoder.encode(request.password),
               nickname = request.nickname
           )
       ).toResponse()
    }

    override fun loginMember(request: LoginRequest): LoginResponse {
        val member = memberRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Email does not exist")

        if(!passwordEncoder.matches(request.password,member.password))
            throw IllegalArgumentException("Passwords don't match")

        return LoginResponse(
            jwtPlugin.generateAccessToken(
                subject = member.id.toString(),
                email = member.email,
                role = member.role.toString()
            )
        )
    }
}