package cn.ts.model.connection

import cn.ts.model.DbType
import kotlinx.serialization.Serializable

/**
 * 数据库连接数据类
 * @property id 连接ID
 * @property group 分组
 * @property name 连接名
 * @property host 连接地址
 * @property port 端口
 * @property type 数据库类型
 * @property initDb 初始化数据库
 * @property username 用户名
 * @property password 密码
 * @property enableSsl 是否启用ssl
 * @property sslMode ssl模式
 * @property enableSslValid 是否启用ssl证书验证
 * @property sslClientKeyPath ssl客户端密钥路径
 * @property sslClientCertPath ssl客户端证书路径
 * @property sslRootCertPath ssl根证书路径
 * @author tomsean
 */
@Serializable
data class DbConnection(
    val id: Int,
    val group: List<String>,
    val name: String,
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
    val sslRootCertPath: String? = null
)