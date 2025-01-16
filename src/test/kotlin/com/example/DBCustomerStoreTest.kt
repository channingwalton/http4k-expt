package com.example

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DBCustomerStoreTest : DatabaseTest {
    @Test
    fun crud() {
        val customer = Customer(id = null, name = Name("Bob"), email = Email("bob@example.com"))
        val id = DBStore.transact { DBStore.add(customer) }
        val found = DBStore.transact { DBStore.getCustomer(id) }
        assertEquals(customer.copy(id = id), found)
    }
}