package com.example.dao

import com.example.model.Ip
import io.ktor.utils.io.core.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

interface DAOInterface : Closeable {
    fun init()
    fun createIp(ip: String, geoTag: String)
    fun updateIp(id: Int, ip: String, geoTag: String)
    fun deleteIp(id: Int)
    fun getIp(id: Int): Ip?
    fun getAllIps(): List<Ip>?
}

class IpListDao(val db : Database) : DAOInterface{
    override fun init() = transaction(db){
        SchemaUtils.create(IpList)
    }

    override fun createIp(title: String, description: String) = transaction(db){
        IpList.insert {
            it[IpList.ip] = title
            it[IpList.geoTag] = description
        }
        Unit
    }

    override fun updateIp(id: Int, title: String, description: String) = transaction(db){
        IpList.update({ IpList.id eq id }) {
            it[IpList.ip] = title
            it[IpList.geoTag] = description
        }
        Unit
    }

    override fun deleteIp(id: Int) = transaction(db){
        IpList.deleteWhere { IpList.id eq id }
        Unit
    }

    override fun getIp(id: Int): Ip? = transaction(db) {
        IpList.select { IpList.id eq id }.map {
            Ip(
                it[IpList.id], it[IpList.ip], it[IpList.geoTag]
            )
        }.singleOrNull()
    }

    override fun getAllIps(): List<Ip> = transaction(db) {
        IpList.selectAll().map {
            Ip(
                it[IpList.id], it[IpList.ip], it[IpList.geoTag]
            )
        }
    }

    override fun close() {}

}