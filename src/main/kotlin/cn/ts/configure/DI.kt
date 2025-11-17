package cn.ts.configure

import cn.ts.model.DbType
import cn.ts.service.DBOperation
import cn.ts.service.DbConnectService
import cn.ts.service.pg.DBOperationPostgres
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import kotlin.math.sin

/**
 * Dependency Injection
 * @author tomsean
 */
fun Application.configureDI() {
    install(Koin) {
        slf4jLogger()

        modules(module {
            single<DbConnectService> { DbConnectService() }
            single<DBOperation>(named(DbType.POSTGRESQL)) { DBOperationPostgres() }
        })
    }
}