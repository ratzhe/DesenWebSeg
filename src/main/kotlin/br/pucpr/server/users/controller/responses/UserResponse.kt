package br.pucpr.server.users.controller.responses

import br.pucpr.server.users.User

data class UserResponse(
    val id: Long,
    val email: String,
    val name: String
) {
    constructor(user: User): this(user.id!!, user.email, user.name)
}
