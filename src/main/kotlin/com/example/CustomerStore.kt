package com.example

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface CustomerStore {
  fun add(c: Customer): Long
  fun getCustomer(cId: Long): Customer?

  fun <T> transact(statement: () -> T): T
}

object DBCustomerStore : CustomerStore {

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
}