package com.example.plugins

import com.example.dao.IpListDao
import com.example.model.GeoTag
import com.example.model.Ip
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting(dao: IpListDao) {
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter())
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/ipList/{radius}") {
            val radius = call.parameters["radius"]
            val ip = call.request.origin.remoteHost
            val geoTag = call.receive<GeoTag>()
            val ipList = dao.getIpsOnRadius(ip, radius!!.toInt())

            dao.createIp(ip, geoTag.geoTag)
            call.respond(ipList)
        }

        post("/addIp") {
            val ip = call.receive<Ip>()
            dao.createIp(ip.ip, ip.geoTag)
            call.respond("Ip added successfully")
        }

        delete("/ipList/{id}") {
            val id = call.parameters["id"]
            id?.let {
                dao.deleteIp(id.toInt())
                call.respond("Ip deleted")
            } ?: call.respond("Invalid ip")
        }

        put("/updateIp") {
            val ip = call.receive<Ip>()
            dao.updateIp(ip.id, ip.ip ?: "", ip.geoTag ?: "")
            call.respond("Ip updated successfully")
        }
    }
}
