package com.example

interface Logic {
  fun add(c: Customer): Long
  fun getCustomer(cId: Long): Customer?
}

class TheLogics(private val customerStore: CustomerStore) : Logic {
  override fun add(c: Customer): Long =
    customerStore.transact {
      customerStore.add(c)
    }

  override fun getCustomer(cId: Long): Customer? =
    customerStore.transact {
      customerStore.getCustomer(cId)
    }
}