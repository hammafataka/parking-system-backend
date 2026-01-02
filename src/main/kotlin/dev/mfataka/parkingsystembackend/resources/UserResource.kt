package dev.mfataka.parkingsystembackend.resources

import dev.mfataka.parkingsystembackend.collection.User
import dev.mfataka.parkingsystembackend.model.user.RegisterUserRequest
import dev.mfataka.parkingsystembackend.security.UserResponse
import dev.mfataka.parkingsystembackend.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 18.12.2025 22:53
 */
@RestController
@RequestMapping(path = ["/api/v1/users"])
class UserResource(@Autowired val userService: UserService) {


    @PostMapping(value = ["/register"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun registerUser(@RequestBody request: RegisterUserRequest): Mono<User> {
        return userService.registerOne(request)
    }

    @GetMapping(value = ["/current"])
    fun getCurrent(auth: Authentication): Mono<UserResponse> {
        return userService.findByUserName(auth.name)
            .map { it.asResponse() }
    }


}