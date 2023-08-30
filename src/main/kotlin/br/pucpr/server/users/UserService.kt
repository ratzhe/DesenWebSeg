package br.pucpr.server.users

import br.pucpr.server.exception.BadRequestException
import br.pucpr.server.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService(val repository: UserRepository) {
    fun insert(user: User): User {
        if (repository.findByEmailOrNull(user.email) != null) {
            throw BadRequestException("User already exists!")
        }

        val saved = repository.save(user)
        log.info("User {} inserted", saved.id)
        return saved
    }

    fun findAll(sortDir: SortDir) = repository.findAll(sortDir)
    fun findByIdOrNull(id: Long) = repository.findByIdOrNull(id)
    fun delete(id: Long): Boolean {
        val user = repository.findByIdOrNull(id) ?: return false
        repository.delete(user)
        log.info("User {} deleted", id)
        return true
    }

    fun update(user: User): User {
        val existingUser = repository.findByIdOrNull(user.id!!)
            ?: throw NotFoundException("User not found")

        if (user.name == existingUser.name) {
            return existingUser
        }

        if (user.name.isNullOrBlank()) {
            throw BadRequestException("Name cannot be null or blank")
        }

        existingUser.name = user.name
        repository.save(existingUser)

        log.info("User {} updated", existingUser.id)
        return existingUser
    }

    companion object {
        private val log = LoggerFactory.getLogger(UserService::class.java)
    }
}
