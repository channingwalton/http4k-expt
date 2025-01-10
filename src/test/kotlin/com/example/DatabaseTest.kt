package com.example

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll

interface DatabaseTest {
  companion object {
    @JvmStatic
    @BeforeAll
    fun setup() {
      Database.connect("jdbc:h2:mem:${this::class.java.simpleName};DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
      transaction { SchemaUtils.create(Customers) }
    }
  }
}