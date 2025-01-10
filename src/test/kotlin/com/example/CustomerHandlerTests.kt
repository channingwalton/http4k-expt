package com.example

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class CustomerHandlerTests {

  companion object {
    @JvmStatic
    @BeforeAll
    fun setup() {
      Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
      transaction { SchemaUtils.create(Customers) }
    }
  }

  @Test
  fun `Customer test`() {
    val customer = Customer(id = 1, name = "Bob", email = "bob@example.com")

    val json = Json.encodeToString(customer)
    assertEquals(Response(OK), CustomerHandler().app(Request(POST, "/customer").body(json)))

    val expected = Response(OK).header("Content-Type", "application/json").body(json)
    assertEquals(expected, CustomerHandler().app(Request(GET, "/customer/1")))
  }

  @Test
  fun `Customer unknown customer`() {
    assertEquals(Response(NOT_FOUND), CustomerHandler().app(Request(GET, "/customer/999")))
  }

}
