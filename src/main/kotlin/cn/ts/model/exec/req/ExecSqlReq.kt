package cn.ts.model.exec.req

import kotlinx.serialization.Serializable

/**
 * 执行SQL请求参数
 * @property id 对应数据库ID
 * @property sql SQL
 * @author tomsean
 */
@Serializable
data class ExecSqlReq(
    val id: Int,
    val sql: String
)