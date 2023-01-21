package com.itmo.dao

import org.jetbrains.exposed.dao.IntIdTable

object Games: IntIdTable(){
    val title = varchar("title", 50)
    val year = integer("year")
    val description = varchar("description", 200)
    val imageURL = varchar("imageURL", 200)
}