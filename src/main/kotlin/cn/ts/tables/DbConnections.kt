package cn.ts.tables

import cn.ts.configure.tableEncryptor
import cn.ts.model.connect.DbConnection
import cn.ts.model.DbType
import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

/**
 * 数据库连接表
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
object DbConnections : IntIdTable("db_connection") {
    val name = varchar("name", 100)
    val host = varchar("url", 150)
    val port = integer("port")
    val type = enumeration<DbType>("type")
    val initDb = varchar("init_db", 100)
    val username = varchar("username", 300).nullable()
    val password = encryptedVarchar("password", 2048, tableEncryptor).nullable()
    val enableSsl = bool("enable_ssl").default(false)
    val sslMode = varchar("ssl_mode", 100).nullable()
    val enableSslValid = bool("enable_ssl_valid").default(false)
    val sslClientKeyPath = varchar("ssl_client_key_path", 1500).nullable()
    val sslClientCertPath = varchar("ssl_client_cert_path", 1500).nullable()
    val sslRootCertPath = varchar("ssl_root_cert_path", 1500).nullable()
}

fun ResultRow.toDbConnection() = DbConnection(
    id = this[DbConnections.id].value,
    name = this[DbConnections.name],
    host = this[DbConnections.host],
    port = this[DbConnections.port],
    type = this[DbConnections.type],
    initDb = this[DbConnections.initDb],
    username = this[DbConnections.username],
    password = this[DbConnections.password],
    enableSsl = this[DbConnections.enableSsl],
    sslMode = this[DbConnections.sslMode],
    enableSslValid = this[DbConnections.enableSslValid],
    sslClientKeyPath = this[DbConnections.sslClientKeyPath],
    sslClientCertPath = this[DbConnections.sslClientCertPath],
    sslRootCertPath = this[DbConnections.sslRootCertPath]
)