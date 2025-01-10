package com.example

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class CustomerHandler {

  private val logger = LoggerFactory.getLogger(this.javaClass)

  val app: HttpHandler = routes(

    "/customer" bind POST to { request ->
      logger.info("Received a POST request")
      val raw = request.body.toString()
      try {
        val customer = Json.decodeFromString<Customer>(raw)
        transaction {
          CustomerStore.add(customer)
          Response(OK)
        }
      } catch (e: Exception) {
        logger.error("Error decoding json: $raw")
        Response(BAD_REQUEST.description("Unable to decode JSON"))
      }
    },

    "/customer/{id}" bind GET to { request ->
      request.path("id")?.toLong()?.let { okId ->
        transaction {
          CustomerStore.getCustomer(okId)?.let { customer ->
            Response(OK)
              .header("Content-Type", "application/json")
              .body(Json.encodeToString(customer))
          } ?: Response(NOT_FOUND)
        }
      } ?: Response(BAD_REQUEST.description("Please provide an ID"))
    }
  )
}
