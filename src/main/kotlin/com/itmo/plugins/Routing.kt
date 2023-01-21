package com.itmo.plugins

import com.itmo.dao
import com.itmo.model.GameModel
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/games") {
            get("/{id}") {
                val id = call.parameters["id"]
                if (id != null) {
                    val game = dao.getGame(id.toInt())
                    if (game != null) {
                        call.respond(game)
                    }
                }
                call.response.status(HttpStatusCode.NotFound)
            }
            get {
                call.respond(dao.getAllGames())
            }
            post {
                val emp = call.receive<GameModel>()
                val id = dao.createGame(emp.title, emp.year, emp.description, emp.imageURL)
                call.respond(dao.getGame(id)!!)
            }
            put {
                val emp = call.receive<GameModel>()
                dao.updateGame(emp.id, emp.title, emp.year, emp.description, emp.imageURL)
                call.respond(dao.getGame(emp.id)!!)
            }
            delete("/{id}") {
                val id = call.parameters["id"]
                if (id != null) {
                    val game = dao.getGame(id.toInt())
                    if (game != null) {
                        dao.deleteGame(id.toInt())
                        call.response.status(HttpStatusCode.OK)
                    }
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
