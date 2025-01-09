package com.example

import org.http4k.core.Method.*
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.http4k.core.Status.Companion.NOT_FOUND
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll


class HelloWorldTest {

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
    val customer = Customer(id = 1, name = "Channing",email = "channingwalton@mac.com")

    val json = Json.encodeToString(customer)
    assertEquals(Response(OK), TheHandlers().app(Request(POST, "/customer").body(json)))

    val expected = Response(OK).header("Content-Type", "application/json").body(json)
    assertEquals(expected, TheHandlers().app(Request(GET, "/customer/1")))
  }

  @Test
  fun `Customer unknown customer`() {
    assertEquals(Response(NOT_FOUND), TheHandlers().app(Request(GET, "/customer/999")))
  }

}
