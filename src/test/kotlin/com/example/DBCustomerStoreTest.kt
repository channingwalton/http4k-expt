package com.example

import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DBCustomerStoreTest : DatabaseTest {
  @Test
  fun crud() {
    val customer = Customer(id = null, name = "Bob", email = "bob@example.com")
    val id = transaction { DBStore.add(customer) }
    val found = transaction { DBStore.getCustomer(id) }
    assertEquals(customer.copy(id = id), found)
  }
}