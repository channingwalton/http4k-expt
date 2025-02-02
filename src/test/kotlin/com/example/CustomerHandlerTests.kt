package com.example

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
import java.io.ByteArrayInputStream

class CustomerHandlerTests {

    private val customer = Customer(id = Id(1), name = Name("Bob"), email = Email("bob@example.com"))
    private val mockLogic = object : Logic {
        override fun add(c: Customer): Id = c.id!!
        override fun getCustomer(cId: Id): Customer? =
            if (cId == customer.id) customer else null
    }

    private val handler = CustomerHandler(mockLogic)

    @Test
    fun `create and retrieve a customer`() {
        val json = Json.encodeToString(customer)

        // The body must be a Stream to match what actually happens in the app,
        // as opposed to just a String
        val request = Request(POST, "/customer").body(bodyStream(json))
        assertEquals(Response(OK), handler.app(request))

        val expected = Response(OK).with(CONTENT_TYPE of ContentType.APPLICATION_JSON).body(json)
        assertEquals(expected, handler.app(Request(GET, "/customer/1")))
    }

    @Test
    fun `unknown customer`() {
        assertEquals(Response(NOT_FOUND), handler.app(Request(GET, "/customer/999")))
    }

    @Test
    fun `bad json`() {
        val request = Request(POST, "/customer").body(bodyStream("oops"))
        assertEquals(Response(BAD_REQUEST), handler.app(request))
    }

    private fun bodyStream(txt: String): ByteArrayInputStream = ByteArrayInputStream(txt.toByteArray())
}
