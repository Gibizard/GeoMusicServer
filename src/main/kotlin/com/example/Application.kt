package com.example

import com.example.dao.IpListDao
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import org.h2.tools.Server
import org.jetbrains.exposed.sql.Database

fun main() {
    val dao = IpListDao(Database.connect("jdbc:h2:~/test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver"))
    dao.init()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting(dao)
    }.start(wait = true)
}
