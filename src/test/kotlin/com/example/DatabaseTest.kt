package com.example

import org.junit.jupiter.api.BeforeAll

interface DatabaseTest {
  companion object {
    @JvmStatic
    @BeforeAll
    fun setup() {
      DBStore.initialize(this::class.java.simpleName)
    }
  }
}