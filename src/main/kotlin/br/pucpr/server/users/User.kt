package br.pucpr.server.users

data class User(
    var id: Long? = null,
    var name: String,
    val email: String,
    val password: String
)

