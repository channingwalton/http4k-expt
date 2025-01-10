package com.example

import arrow.core.right
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CustomerTest {
  @Test
  fun roundTripJSON() {
    val customer = Customer(id = 1, name = "Bob", email = "bob@example.com")
    val back = Customer.Factory.fromJsonString(customer.toJson())
    Assertions.assertEquals(back, customer.right())
  }
}