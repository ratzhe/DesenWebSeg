package br.pucpr.server.users

import br.pucpr.server.exception.BadRequestException
import br.pucpr.server.users.controller.request.CreateUserRequest
import br.pucpr.server.users.controller.request.UpdateUserRequest
import br.pucpr.server.users.controller.responses.UserResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(val service: UserService) {
    @PostMapping
    fun insert(@RequestBody @Valid user: CreateUserRequest) =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(UserResponse(service.insert(user.toUser())))

    @GetMapping
    fun list(@RequestParam sortDir: String?) =
        service.findAll(SortDir.findOrThrow(sortDir ?: "ASC"))
            .map { UserResponse(it) }
            .let { ResponseEntity.ok(it) }


    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        service.findByIdOrNull(id)
            ?.let { ResponseEntity.ok(UserResponse(it)) }
            ?: ResponseEntity.notFound().build()

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid updateUserRequest: UpdateUserRequest
    ): ResponseEntity<UserResponse> {
        val existingUser = service.findByIdOrNull(id)
            ?: return ResponseEntity.notFound().build()

        if (updateUserRequest.name == existingUser.name) {
            return ResponseEntity.noContent().build()
        }

        if (updateUserRequest.name.isNullOrBlank()) {
            throw BadRequestException("Name cannot be null or blank")
        }

        existingUser.name = updateUserRequest.name!!
        val updatedUser = service.update(existingUser)

        return ResponseEntity.ok(UserResponse(updatedUser))
    }


    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> =
        if (service.delete(id)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()
}