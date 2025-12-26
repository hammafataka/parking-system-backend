package dev.mfataka.parkingsystembackend.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import dev.mfataka.parkingsystembackend.security.loadRsaPrivateKey
import dev.mfataka.parkingsystembackend.security.loadRsaPublicKey
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project ParkingSystemBackend
 * @date 21.12.2025 0:58
 */
@Configuration
class JwtKeyConfig {

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val publicKey = loadRsaPublicKey(ClassPathResource("keystore/public_key.pem"))
        val privateKey = loadRsaPrivateKey(ClassPathResource("keystore/private_key.pem"))

        val rSAKey = RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID("parking-key-1")
            .build()

        val jwkSource = JWKSource<SecurityContext> { selector, _ -> selector.select(JWKSet(rSAKey)) }

        return NimbusJwtEncoder(jwkSource)
    }

    @Bean
    fun jwtDecoder(): ReactiveJwtDecoder {
        val publicKey = loadRsaPublicKey(ClassPathResource("keystore/public_key.pem"))
        return NimbusReactiveJwtDecoder.withPublicKey(publicKey).build()
    }


}