package cn.ts.model.exec.resp

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * 执行SQL返回参数
 * @property rows 影响行数
 * @property result 执行结果
 * @property error 错误信息
 * @author tomsean
 */
@Serializable
data class ExecuteSqlResp(
    val rows: Int = 0,
    val result: List<JsonObject>,
    val error: String? = null
)