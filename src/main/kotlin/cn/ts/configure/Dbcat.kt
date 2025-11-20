package cn.ts.configure

import io.ktor.server.application.Application
import io.ktor.server.config.tryGetString
import org.jetbrains.exposed.crypt.Algorithms
import org.jetbrains.exposed.crypt.Encryptor

val tableEncryptor: Encryptor
    get() = _tableEncryptor

lateinit var _tableEncryptor: Encryptor

/**
 *
 * @author tomsean
 */
fun Application.configureDbcat() {
    val config = environment.config.config("dbcat")
    val secret = config.tryGetString("secret") ?: throw Exception("dbcat.secret is null")
    _tableEncryptor = Algorithms.TRIPLE_DES(secret)
}

//fun main() {
//    try {
//        // 1. 获取 Triple DES (DESede) 的 KeyGenerator
//        val keyGen = KeyGenerator.getInstance("DESede") // "DESede" 即 Triple DES
//
//        // 2. 初始化为 168 位（即 3-key 3DES，会生成 24 字节密钥）
//        keyGen.init(168) // 168 bits = 24 bytes
//
//        // 3. 生成密钥
//        val secretKey: SecretKey = keyGen.generateKey()
//
//        // 4. 获取密钥的字节数组
//        val keyBytes: ByteArray = secretKey.encoded
//
//        // 5. 打印密钥信息
//        println("生成的 3DES 密钥长度: ${keyBytes.size} 字节") // 应该输出 24
//        println("Base64 编码的密钥: ${Base64.getEncoder().encodeToString(keyBytes)}")
//
//        // 如果你需要将此密钥保存、传输或用于加密，请妥善保管 Base64 或字节数组
//    } catch (e: NoSuchAlgorithmException) {
//        println("不支持的算法: ${e.message}")
//        e.printStackTrace()
//    }
//}