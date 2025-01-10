package com.example

import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CustomerStoreTest : DatabaseTest {
  @Test
  fun crudTest() {
    val customer = Customer(id = null, name = "Bob", email = "bob@example.com")
    val id = transaction { CustomerStore.add(customer) }
    val found = transaction { CustomerStore.getCustomer(id) }
    assertEquals(customer.copy(id = id), found)
  }
}