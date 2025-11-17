package cn.ts.configure

import cn.ts.service.DbConnectService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

/**
 * Dependency Injection
 * @author tomsean
 */
fun Application.configureDI() {
    install(Koin) {
        slf4jLogger()

        modules(module {
            single<DbConnectService> { DbConnectService() }
        })
    }
}