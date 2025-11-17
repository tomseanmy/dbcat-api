package cn.ts.model.connection.req

import cn.ts.model.DbType
import kotlinx.serialization.Serializable

/**
 * 测试数据库连接请求参数
 * @author tomsean
 */
@Serializable
data class TestDbConnectionReq(
    val host: String,
    val port: Int,
    val type: DbType,
    val initDb: String,
    val username: String? = null,
    val password: String? = null,
    val enableSsl: Boolean = false,
    val sslMode: String? = null,
    val enableSslValid: Boolean = false,
    val sslClientKeyPath: String? = null,
    val sslClientCertPath: String? = null,
    val sslRootCertPath: String? = null,
)
