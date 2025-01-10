package com.example

import org.jetbrains.exposed.sql.transactions.transaction

object TheLogics {
  fun add(c: Customer): Long =
    transaction {
     CustomerStore.add(c)
    }

  fun getCustomer(cId: Long): Customer? =
    transaction {
      CustomerStore.getCustomer(cId)
    }
}