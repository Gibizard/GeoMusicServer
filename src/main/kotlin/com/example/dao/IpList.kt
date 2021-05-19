package com.example.dao

import org.jetbrains.exposed.sql.Table

object IpList : Table() {
    val id = integer("id").autoIncrement()
    val ip = varchar("ip", 50)
    val geoTag = varchar("geo_tag", 50)

    override val primaryKey: PrimaryKey?
        get() = super.primaryKey ?: PrimaryKey(id)
}