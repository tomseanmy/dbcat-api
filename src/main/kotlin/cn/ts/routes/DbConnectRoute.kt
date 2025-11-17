package cn.ts.routes

import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

/**
 * 连接路由
 * @author tomsean
 */
fun Route.dbConnectRoute() {
    route("/connect") {
        // 获取连接列表
        get("/list") {

        }
    }
}