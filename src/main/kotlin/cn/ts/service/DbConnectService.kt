package cn.ts.service

import cn.ts.model.connect.DbConnection
import cn.ts.model.connect.req.SaveDbConnectReq
import cn.ts.tables.DbConnections
import cn.ts.tables.existsById
import cn.ts.tables.toDbConnection
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.koin.core.component.KoinComponent

/**
 * 连接服务
 * @author tomsean
 */
class DbConnectService : KoinComponent {

    val selectCols = listOf(
        DbConnections.id,
        DbConnections.name,
        DbConnections.host,
        DbConnections.port,
        DbConnections.type,
        DbConnections.initDb,
        DbConnections.username,
        DbConnections.enableSsl,
        DbConnections.sslMode,
        DbConnections.enableSslValid,
        DbConnections.sslClientKeyPath,
        DbConnections.sslClientCertPath,
        DbConnections.sslRootCertPath
    )

    /**
     * 获取连接列表
     */
    fun list(): List<DbConnection> = transaction {
        DbConnections.selectAll()
            .map { it.toDbConnection() }
    }

    /**
     * 创建连接
     */
    fun create(req: SaveDbConnectReq): DbConnection = transaction {
        val id = if (DbConnections.existsById(req.id)) {
            DbConnections.update({ DbConnections.id.eq(req.id) }) {
                it[name] = req.name
                it[host] = req.host
                it[port] = req.port
                it[initDb] = req.initDb
                it[username] = req.username
                it[password] = req.password
                it[enableSsl] = req.enableSsl
                it[sslMode] = req.sslMode
                it[enableSslValid] = req.enableSslValid
                it[sslClientKeyPath] = req.sslClientKeyPath
            }
            req.id
        } else {
            DbConnections.insertAndGetId {
                it[name] = req.name
                it[host] = req.host
                it[port] = req.port
                it[initDb] = req.initDb
                it[username] = req.username
                it[password] = req.password
                it[enableSsl] = req.enableSsl
                it[sslMode] = req.sslMode
                it[enableSslValid] = req.enableSslValid
                it[sslClientKeyPath] = req.sslClientKeyPath
            }.value
        }
        DbConnections.selectAll().where { DbConnections.id eq id }.first().toDbConnection()
    }

    /**
     * 删除连接
     * @param ids
     */
    fun delete(ids: List<Int>): Int = transaction {
        DbConnections.deleteWhere { DbConnections.id.inList(ids) }
    }
}