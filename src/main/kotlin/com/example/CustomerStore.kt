package com.example

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

object Customers : Table() {
  val customerId = long("id").autoIncrement()
  val name = varchar("name", 128)
  val email = varchar("email", 256)

  fun add(c: Customer): Long =
    Customers.insert {
      it[name] = c.name
      it[email] = c.email
    }[customerId]

  fun getCustomer(cId: Long): Customer? =
    selectAll().where(customerId eq cId).map { resultRow ->
      Customer(id = cId, name = resultRow[name], email = resultRow[email])
    }.firstOrNull()
}
