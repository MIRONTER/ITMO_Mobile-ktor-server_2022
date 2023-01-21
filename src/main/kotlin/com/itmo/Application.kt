package com.itmo

import com.itmo.dao.DAOFacadeDatabase
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.itmo.plugins.*
import io.ktor.serialization.jackson.*
import org.jetbrains.exposed.sql.Database
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.callloging.*

val dao = DAOFacadeDatabase(Database.connect("jdbc:h2:file:./build/db", driver = "org.h2.Driver"))

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module, watchPaths = listOf("classes"))
        .start(wait = true)
}

fun Application.module() {
    dao.init()

    install(CallLogging)
    install(ContentNegotiation) {
        jackson ()
    }

    configureRouting()
}
