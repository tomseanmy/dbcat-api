package cn.ts.configure

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

val json by lazy {
    Json {
        prettyPrint = true
        isLenient = true
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(json)
    }
}