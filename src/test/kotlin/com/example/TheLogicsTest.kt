package com.example

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TheLogicsTest {
    private val customers = mutableMapOf<Id, Customer>()
    private val mockStore = object : Store {
        override fun add(c: Customer): Id {
            val id = Id(customers.size.toLong())
            customers.put(id, c)
            return id
        }

        override fun getCustomer(cId: Id): Customer? {
            return customers[cId]
        }

        override fun <T> transact(statement: () -> T): T {
            return statement()
        }

    }

    private val customer = Customer(id = null, name = Name("Bob"), email = Email("bob@example.com"))

    @Test
    fun testAddAndGet() {
        val logic = TheLogics(mockStore)

        val id = logic.add(customer)
        assertEquals(customer, logic.getCustomer(id))
        assertEquals(customer, customers[id])
    }
}