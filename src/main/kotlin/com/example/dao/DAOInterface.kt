package com.example.dao

import com.example.model.Ip
import io.ktor.utils.io.core.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import sun.reflect.annotation.TypeAnnotation

interface DAOInterface : Closeable {
    fun init()
    fun createIp(ip: String, geoTag: String)
    fun updateIp(id: Int, ip: String, geoTag: String)
    fun updateIpOnIp(ip: String, geoTag: String)
    fun deleteIp(id: Int)
    fun getIp(id: Int): Ip?
    fun getAllIps(): List<Ip>?
    fun getIpsOnRadius(ip: String, radius: Int): List<Ip>?
}

class IpListDao(val db: Database) : DAOInterface {
    override fun init() = transaction(db) {
        SchemaUtils.create(IpList)
    }

    override fun createIp(ip: String, geoTag: String) = transaction(db) {
        if (IpList.select { IpList.ip eq ip }.count() > 0) {
            updateIpOnIp(ip, geoTag)
        } else {
            IpList.insert {
                it[IpList.ip] = ip
                it[IpList.geoTag] = geoTag
            }
        }
        Unit
    }

    override fun updateIp(id: Int, ip: String, geoTag: String) = transaction(db) {
        IpList.update({ IpList.id eq id }) {
            it[IpList.ip] = ip
            it[IpList.geoTag] = geoTag
        }
        Unit
    }

    override fun updateIpOnIp(ip: String, geoTag: String) = transaction(db) {
        IpList.update({ IpList.ip eq ip }) {
            it[IpList.geoTag] = geoTag
        }
        Unit
    }

    override fun deleteIp(id: Int) = transaction(db) {
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

    override fun getIpsOnRadius(ip: String, radius: Int): List<Ip> = transaction(db) {
        // TODO определение локации запроса

        // TODO Определение удаленности для каждого инстанса, за счет формирование его локации и оставление в выборке только попадающих в радиус
        IpList.select {IpList.ip neq ip} .map {
            Ip(
                it[IpList.id], it[IpList.ip], it[IpList.geoTag]
            )
        }

    }

    override fun close() {}

}