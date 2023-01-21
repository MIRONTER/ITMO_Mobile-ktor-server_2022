package com.itmo.dao

import com.itmo.model.GameModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.Closeable

interface DAOFacade: Closeable{
    fun init()
    fun createGame(title:String, year: Int, description: String, imageURL: String): Int
    fun updateGame(id:Int, title:String, year: Int, description: String, imageURL: String)
    fun deleteGame(id:Int)
    fun getGame(id:Int): GameModel?
    fun getAllGames(): List<GameModel>
}

class DAOFacadeDatabase(private val db: Database): DAOFacade {

    override fun init() = transaction(db) {
        SchemaUtils.create(Games)
    }

    override fun createGame(title: String, year: Int, description: String, imageURL: String) = transaction(db) {
        Games.insertAndGetId {
            it[Games.title] = title
            it[Games.year] = year
            it[Games.description] = description
            it[Games.imageURL] = imageURL
        }.value
    }

    override fun updateGame(id: Int, title: String, year: Int, description: String, imageURL: String) = transaction(db) {
        Games.update({ Games.id eq id }) {
            it[Games.title] = title
            it[Games.year] = year
            it[Games.description] = description
            it[Games.imageURL] = imageURL
        }
        Unit
    }

    override fun deleteGame(id: Int) = transaction(db) {
        Games.deleteWhere { Games.id eq id }
        Unit
    }

    override fun getGame(id: Int) = transaction(db) {
        Games.select { Games.id eq id }.map {
            GameModel(
                it[Games.id].value, it[Games.title], it[Games.year], it[Games.description], it[Games.imageURL]
            )
        }.singleOrNull()
    }

    override fun getAllGames() = transaction(db) {
        Games.selectAll().map {
            GameModel(
                it[Games.id].value, it[Games.title], it[Games.year], it[Games.description], it[Games.imageURL]
            )
        }
    }

    override fun close() {}
}