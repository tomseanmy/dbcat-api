package cn.ts.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.count

/**
 * 检测ID是否存在
 * @author tomsean
 */
fun IntIdTable.existsById(id: Int?): Boolean {
    if (id == null) return false
    val idCol = this.id
    val idCount = this.id.count()
    val row = this.select(idCount).where { idCol eq id }.limit(1).first()
    val count = row.getOrNull(idCount) ?: 0
    return count > 0
}