package cn.ts.service

import cn.ts.model.DBMeta
import cn.ts.model.connection.DbConnection
import cn.ts.model.connection.req.TestDbConnectionReq
import cn.ts.model.exec.req.ExecSqlReq
import cn.ts.model.exec.resp.ExecuteSqlResp

/**
 * 数据库操作服务
 * @author tomsean
 */
interface DBOperation {

    /**
     * 测试数据库连接
     */
    fun testConnection(req: TestDbConnectionReq): Boolean

    /**
     * 连接数据库
     * @param req 数据库连接参数
     * @return 数据库元数据列表
     */
    fun connect(req: DbConnection): List<DBMeta>

    /**
     * 执行SQL
     */
    fun executeSql(req: ExecSqlReq): ExecuteSqlResp

}