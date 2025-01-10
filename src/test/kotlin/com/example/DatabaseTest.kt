package com.example

import org.junit.jupiter.api.BeforeAll

interface DatabaseTest {
  companion object {
    @JvmStatic
    @BeforeAll
    fun setup() {
      DBCustomerStore.initialize(this::class.java.simpleName)
    }
  }
}