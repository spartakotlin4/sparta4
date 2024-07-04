package com.team4.moviereview.domain.member.model


import com.team4.moviereview.domain.member.dto.reponse.RegisterResponse
import jakarta.persistence.*


@Entity
@Table(name = "member")
class Member(

    @Column(name = "email")
    var email: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "nickname")
    var nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    var role : UserRole =UserRole.USER,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    var id: Long? = null

) {



}

fun Member .toResponse(): RegisterResponse {
    return RegisterResponse(
        nickname = nickname
    )
}

