package cn.ts

import cn.ts.configure.configureDI
import cn.ts.configure.configureDatabases
import cn.ts.configure.configureDbcat
import cn.ts.configure.configureHTTP
import cn.ts.configure.configureMonitoring
import cn.ts.configure.configureRouting
import cn.ts.configure.configureSecurity
import cn.ts.configure.configureSerialization
import cn.ts.configure.configureSockets
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.CommandLineConfig
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.configure
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.loadCommonConfiguration

fun main(args: Array<String>) {
    println("start main")
    embeddedServer(
        factory = CIO,
        environment = applicationEnvironment {
            configure(*args)
        },
        configure = {
            val cliConfig = CommandLineConfig(args)
            takeFrom(cliConfig.engineConfig)
            loadCommonConfiguration(cliConfig.rootConfig.environment.config)
        }
    ) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    println("load module")
    configureDI()
    configureDbcat()
    configureSockets()
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureSecurity()
    configureHTTP()
    configureRouting()
}