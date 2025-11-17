package cn.ts.model.connect.req

import cn.ts.model.DbType
import kotlinx.serialization.Serializable

/**
 *
 * @author tomsean
 */
@Serializable
data class SaveDbConnectReq(
    val id: Int? = null,
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
