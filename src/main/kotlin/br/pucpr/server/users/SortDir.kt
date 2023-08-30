package br.pucpr.server.users

import br.pucpr.server.exception.BadRequestException

enum class SortDir {
    ASC, DESC;

    companion object {
        fun findOrThrow(sortDir: String) =
            values().find { it.name == sortDir.uppercase() }
                ?: throw BadRequestException("Invalid sort dir!")

    }

}