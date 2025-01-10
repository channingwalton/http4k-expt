package com.example

import org.jetbrains.exposed.sql.transactions.transaction

interface Logic {
  fun add(c: Customer): Long
  fun getCustomer(cId: Long): Customer?
}

class TheLogics : Logic {
  override fun add(c: Customer): Long =
    transaction {
      CustomerStore.add(c)
    }

  override fun getCustomer(cId: Long): Customer? =
    transaction {
      CustomerStore.getCustomer(cId)
    }
}