package com.example

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface Store {
  fun add(c: Customer): Long
  fun getCustomer(cId: Long): Customer?
  fun <T> transact(statement: () -> T): T
}

object DBStore : Store {

  object Customers : Table() {
    val customerId = long("id").autoIncrement()
    val fullName = varchar("full_name", 128)
    val email = varchar("email", 256)
  }

  override fun add(c: Customer): Long =
    Customers.insert { r ->
      r[fullName] = c.name
      r[email] = c.email
    }[Customers.customerId]

  override fun getCustomer(cId: Long): Customer? =
    Customers.selectAll().where(Customers.customerId eq cId).map {
      Customer(id = cId, name = it[Customers.fullName], email = it[Customers.email])
    }.firstOrNull()

  override fun <T> transact(statement: () -> T): T =
    transaction { statement() }

  fun initialize(config: DatabaseConfig) {
    Database.connect(url = config.url, driver = config.driver, user = config.user, password = config.password)
    Flyway.configure()
      .dataSource(config.url, config.user, config.password)
      .load()
      .migrate()
  }
}