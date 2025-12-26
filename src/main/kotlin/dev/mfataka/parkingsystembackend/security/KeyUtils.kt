package dev.mfataka.parkingsystembackend.security

import org.springframework.core.io.Resource
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

fun readPem(resource: Resource): ByteArray {
    val text = resource.inputStream.bufferedReader().readText()
    val base64 = text
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replace("\\s".toRegex(), "")
    return Base64.getDecoder().decode(base64)
}

fun loadRsaPrivateKey(resource: Resource): RSAPrivateKey {
    val keySpec = PKCS8EncodedKeySpec(readPem(resource))
    return KeyFactory.getInstance("RSA").generatePrivate(keySpec) as RSAPrivateKey
}

fun loadRsaPublicKey(resource: Resource): RSAPublicKey {
    val keySpec = X509EncodedKeySpec(readPem(resource))
    return KeyFactory.getInstance("RSA").generatePublic(keySpec) as RSAPublicKey
}