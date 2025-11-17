package cn.ts.model.connection.req

import cn.ts.model.DbType
import kotlinx.serialization.Serializable

/**
 *
 * @author tomsean
 */
@Serializable
data class SaveDbConnectionReq(
    val id: Int? = null,
    val group: List<String>,
    val name: String,
    val host: String,
    val port: Int,
    val type: DbType,
    val initDb: String,
    val username: String?,
    val password: String? = null,
    val enableSsl: Boolean = false,
    val sslMode: String?,
    val enableSslValid: Boolean = false,
    val sslClientKeyPath: String?,
    val sslClientCertPath: String?,
    val sslRootCertPath: String?
)
