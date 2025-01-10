package com.example

interface Logic {
  fun add(c: Customer): Long
  fun getCustomer(cId: Long): Customer?
}

class TheLogics(private val store: Store) : Logic {
  override fun add(c: Customer): Long =
    store.transact {
      store.add(c)
    }

  override fun getCustomer(cId: Long): Customer? =
    store.transact {
      store.getCustomer(cId)
    }
}