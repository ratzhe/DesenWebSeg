package br.pucpr.server.users.controller.request

import jakarta.validation.constraints.NotBlank

data class UpdateUserRequest(
    @field:NotBlank
    val name: String?
)
