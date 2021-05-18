package com.example.plugins

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.selects.select
import org.h2.engine.ConnectionInfo
import java.sql.Connection
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
            Database.connect("jdbc:h2:~/target", driver = "org.h2.Driver")
            transaction {

            }
        }
    }
}
