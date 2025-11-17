package cn.ts.model

import kotlinx.serialization.Serializable

/**
 *
 * @author tomsean
 */
@Serializable
enum class DbType {
    MYSQL,
    ORACLE,
    SQLSERVER,
    POSTGRESQL,
    DB2,
    H2,
    SQLITE,
    MARIADB,
}