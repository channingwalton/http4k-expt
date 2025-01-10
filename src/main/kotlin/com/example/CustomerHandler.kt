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
      val customer = Json.decodeFromString<Customer>(raw)
      transaction {
        Customers.add(customer)
        Response(OK)
      }
    },

    "/customer/{id}" bind GET to { request ->
      val id = request.path("id")?.toLong()
      logger.info("Received a GET request with $id")
      if (id == null)
        Response(BAD_REQUEST.description("Please provide an ID"))
      else
        transaction {
          val customer = Customers.getCustomer(id)
          if (customer == null) Response(NOT_FOUND)
          else
            Response(OK)
              .header("Content-Type", "application/json")
              .body(Json.encodeToString(customer))
        }
    }
  )
}

