package cn.ts.routes

import cn.ts.model.connection.req.SaveDbConnectionReq
import cn.ts.model.connection.req.TestDbConnectionReq
import cn.ts.service.DbConnectService
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

/**
 * 连接路由
 * @author tomsean
 */
fun Route.dbConnectionRoute() {
    val dbConnectService by inject<DbConnectService>()
    route("/connection") {

        // 获取连接列表
        get {
            call.respond(dbConnectService.list())
        }

        // 创建/更新连接
        post {
            val req = call.receive<SaveDbConnectionReq>()
            val resp = dbConnectService.create(req)
            call.respond(resp)
        }

        // 删除连接
        delete {
            val ids = call.request.queryParameters.getAll("ids") ?: throw IllegalArgumentException("ids is null")
            call.respond(dbConnectService.delete(ids.map { it.toInt() }))
        }

        // 测试连接
        post ("/test") {
           val id = call.receive<TestDbConnectionReq>()
        }
    }
}