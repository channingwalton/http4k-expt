package com.example

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.ContentType
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Header.CONTENT_TYPE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CustomerHandlerTests : DatabaseTest {

  private val handler = CustomerHandler(TheLogics())

  @Test
  fun `Customer test`() {
    val customer = Customer(id = 1, name = "Bob", email = "bob@example.com")

    val json = Json.encodeToString(customer)
    assertEquals(Response(OK), handler.app(Request(POST, "/customer").body(json)))

    val expected = Response(OK).with(CONTENT_TYPE of ContentType.APPLICATION_JSON).body(json)
    assertEquals(expected, handler.app(Request(GET, "/customer/1")))
  }

  @Test
  fun `unknown customer`() {
    assertEquals(Response(NOT_FOUND), handler.app(Request(GET, "/customer/999")))
  }

  @Test
  fun `bad json`() {
    assertEquals(Response(BAD_REQUEST), handler.app(Request(POST, "/customer").body("err")))
  }

}
