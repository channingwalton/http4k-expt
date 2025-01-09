package com.example

import ch.qos.logback.classic.Logger
import org.http4k.core.HttpHandler
import org.http4k.core.Method.*
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.routing.path
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class TheHandlers {

  private val logger = LoggerFactory.getLogger(this.javaClass);

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

fun main() {
  Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
  transaction {
    SchemaUtils.create(Customers)
    val id = Customers.add(Customer(id = null, name = "Channing", email = "channingwalton@mac.com"))
    println(Customers.getCustomer(id))
  }
  transaction {
    println("Again")
    println(Customers.getCustomer(1))
  }
  val handlers = TheHandlers()
  val printingApp: HttpHandler = PrintRequest().then(handlers.app)

  val server = printingApp.asServer(SunHttp(9000)).start()

  println("Server started on " + server.port())
}
