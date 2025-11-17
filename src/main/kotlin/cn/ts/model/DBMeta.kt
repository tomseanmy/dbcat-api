package cn.ts.model

import kotlinx.serialization.Serializable

/**
 * 数据库元数据
 * @author tomsean
 */
@Serializable
data class DBMeta(
    val id: String,
    val name: String,
    val collate: String,
    val properties: Map<String, String> = emptyMap()
)
