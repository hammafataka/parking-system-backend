package dev.mfataka.parkingsystembackend.config

import dev.mfataka.parkingsystembackend.security.DEVICE_ID
import dev.mfataka.parkingsystembackend.security.REFRESH_TOKEN
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Mono

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 20.12.2025 23:37
 */

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(val userDetailsService: ReactiveUserDetailsService) {

    private val log = KotlinLogging.logger {}

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.cors {}
            .csrf { it.disable() }
            .authorizeExchange {
                it.pathMatchers(
                    "/api/v1/users/authenticate",
                    "/api/v1/auth/authenticate",
                    "/api/v1/auth/login",
                    "/api/v1/auth/refresh",
                    "/api/v1/auth/logout",
                    "/public/**"
                ).permitAll()
                    .anyExchange().authenticated()
            }
            .exceptionHandling {
                it.accessDeniedHandler { exchange, exception ->
                    log.error(exception) { "Unauthorized request" }
                    exchange.response.statusCode = HttpStatus.FORBIDDEN
                    exchange.response.setComplete()
                }
                it.authenticationEntryPoint { exchange, exception ->
                    log.error(exception) { "Unauthorized request" }
                    exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                    exchange.response.setComplete()
                }
            }
            .oauth2ResourceServer { it.jwt { jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()) } }
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        return UrlBasedCorsConfigurationSource().apply {
            val corsConfiguration = CorsConfiguration().apply {
                allowedOriginPatterns = listOf("http://localhost:*", "http://10.0.2.2:*", "http://127.0.0.1:*")
                allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                allowedHeaders = listOf("content-type", "authorization", "X-XSRF-TOKEN", REFRESH_TOKEN, DEVICE_ID)
                exposedHeaders = listOf("set-cookie")
                allowCredentials = true
            }
            registerCorsConfiguration("/**", corsConfiguration)
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(passwordEncoder: PasswordEncoder): ReactiveAuthenticationManager =
        UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService).apply {
            setPasswordEncoder(passwordEncoder)
        }

    @Bean
    fun jwtAuthConverter(): Converter<Jwt, Mono<AbstractAuthenticationToken>> {
        val converter = JwtAuthenticationConverter().apply {
            setJwtGrantedAuthoritiesConverter { jwt ->
                val roles = (jwt.claims["roles"] as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                roles.map { SimpleGrantedAuthority(it) }
            }
        }
        return ReactiveJwtAuthenticationConverterAdapter(converter)
    }

}