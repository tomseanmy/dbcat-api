package cn.ts.service.pg

import cn.ts.model.DBMeta
import cn.ts.model.connection.DbConnection
import cn.ts.model.connection.req.TestDbConnectionReq
import cn.ts.model.exec.req.ExecSqlReq
import cn.ts.model.exec.resp.ExecuteSqlResp
import cn.ts.service.DBOperation
import cn.ts.utils.DateUtil.STANDARD
import cn.ts.utils.DateUtil.YYYYMMDD
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Array as SqlArray
import java.sql.Blob
import java.sql.Clob
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Postgres 数据库操作服务
 * @author tomsean
 */
class DBOperationPostgres : DBOperation {

    private val dbMap = mutableMapOf<Int, Database>()

    val driverName = "org.postgresql.Driver"
    val testSql = "SELECT 1"
    val selectDb = """
        SELECT * 
        FROM pg_database
        WHERE datistemplate = false;
    """.trimIndent()

    override fun testConnection(req: TestDbConnectionReq): Boolean {
        val database = Database.connect(
            url = "jdbc:postgresql://${req.host}:${req.port}/${req.initDb}",
            driver = driverName,
            user = req.username ?: "",
            password = req.password ?: ""
        )
        return transaction(database) {
            this.exec(stmt = testSql, explicitStatementType = StatementType.SELECT)
            true
        }
    }

    override fun connect(req: DbConnection): List<DBMeta> {
        val database = Database.connect(
            url = "jdbc:postgresql://${req.host}:${req.port}/${req.initDb}",
            driver = driverName,
            user = req.username ?: "",
            password = req.password ?: ""
        )
        val dbMetaList = mutableListOf<DBMeta>()
        transaction(database) {
            this.exec(stmt = selectDb, explicitStatementType = StatementType.SELECT, transform = {
                while (it.next()) {
                    val id = it.getString("oid")
                    val dbName = it.getString("datname")
                    val collate = it.getString("datcollate")
                    dbMetaList += DBMeta(id = id, name = dbName, collate = collate)
                }
            })
        }
        dbMap[req.id] = database
        return dbMetaList.toList()
    }

    override fun executeSql(req: ExecSqlReq): ExecuteSqlResp {
        var rows = 0
        val list = mutableListOf<JsonObject>()
        transaction(dbMap[req.id]) {
            val stmtType = StatementType.entries.find { req.sql.uppercase().trim().startsWith(it.name, true) }
            this.exec(stmt = req.sql, explicitStatementType = stmtType, transform = {
                if (stmtType == StatementType.SELECT) {
                    val metaData = it.metaData
                    val columnCount = metaData.columnCount
                    while (it.next()) {
                        val rowMap = mutableMapOf<String, JsonElement>()
                        // 遍历当前行的所有列
                        for (i in 1..columnCount) {
                            // JDBC 列索引从 1 开始
                            val columnName = metaData.getColumnLabel(i) // 使用 Label 以便正确处理别名
                            // 获取值。使用 getObject() 是最通用的方式
                            val value = it.getObject(i)
                            // 转换为 JsonElement（尽量解析具体类型；未知类型走默认对象的序列化）
                            rowMap[columnName] = anyToJsonElement(value)
                        }
                        list += JsonObject(rowMap)
                    }
                }
            })
        }
        return ExecuteSqlResp(
            rows = rows,
            result = list,
        )
    }

    /**
     * 将任意 JDBC/PG 值转换为 JsonElement。
     * 已知基础类型直接转换；未知类型使用 [UnknownDbValue] 的序列化器作为兜底（可正确处理 PG JSON/JSONB）。
     */
    private fun anyToJsonElement(value: Any?): JsonElement {
        return when (value) {
            null -> JsonNull
            is JsonElement -> value
            // 常见标量
            is String -> JsonPrimitive(value)
            is Number -> JsonPrimitive(value)
            is Boolean -> JsonPrimitive(value)
            is BigDecimal -> JsonPrimitive(value)
            is BigInteger -> JsonPrimitive(value)

            // kotlinx.datetime
            is LocalDateTime -> JsonPrimitive(value.format(STANDARD))
            is LocalDate -> JsonPrimitive(value.format(YYYYMMDD))

            // SQL 数组
            is SqlArray -> {
                val arr = try { value.array as? Array<*> } catch (_: Throwable) { null }
                if (arr != null) {
                    JsonArray(arr.map { anyToJsonElement(it) })
                } else {
                    // JDBC 驱动可能返回原生数组或 List
                    val obj = try { value.array } catch (_: Throwable) { null }
                    when (obj) {
                        null -> JsonNull
                        is Iterable<*> -> JsonArray(obj.map { anyToJsonElement(it) })
                        is Array<*> -> JsonArray(obj.map { anyToJsonElement(it) })
                        else -> throw IllegalArgumentException("Unsupported SQL array type: ${obj::class.simpleName}")
                    }
                }
            }

            // 二进制/大文本
            is ByteArray -> JsonPrimitive(value.joinToString(separator = ",", prefix = "[", postfix = "]"))
            is Blob -> JsonPrimitive("<BLOB size=" + (try { value.length() } catch (_: Throwable) { -1 }) + ">")
            is Clob -> JsonPrimitive("<CLOB size=" + (try { value.length() } catch (_: Throwable) { -1 }) + ">")

            // PG JSON/JSONB 等特殊对象在 UnknownDbValue 的序列化器中处理
            else -> throw IllegalArgumentException("Unsupported SQL array type: ${value::class.simpleName}")
        }
    }
}
