package com.example

interface Logic {
    fun add(c: Customer): Id
    fun getCustomer(cId: Id): Customer?
}

class TheLogics(private val store: Store) : Logic {
    override fun add(c: Customer): Id =
        store.transact {
            store.add(c)
        }

    override fun getCustomer(cId: Id): Customer? =
        store.transact {
            store.getCustomer(cId)
        }
}