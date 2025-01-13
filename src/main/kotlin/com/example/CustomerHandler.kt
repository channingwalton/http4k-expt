package com.example

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Header.CONTENT_TYPE
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.slf4j.LoggerFactory

class CustomerHandler(logic: Logic) {

  private val logger = LoggerFactory.getLogger(this.javaClass)

  val app: HttpHandler = routes(

    "/customer" bind POST to { request ->
      logger.info("Received a POST request")
      val raw = request.bodyString()
      try {
        val customer = Json.decodeFromString<Customer>(raw)
        logic.add(customer)
        Response(OK)
      } catch (e: SerializationException) {
        logger.error("Error decoding json: $raw", e)
        Response(BAD_REQUEST.description("Unable to decode JSON"))
      } catch (e: Exception) {
        logger.error("Things have gone badly wrong", e)
        Response(INTERNAL_SERVER_ERROR.description("Bad things have happened"))
      }
    },

    "/customer/{id}" bind GET to { request ->
      request.path("id")?.toLong()?.let { okId ->
        logic.getCustomer(Id(okId))?.let { customer ->
          Response(OK)
            .with(CONTENT_TYPE of ContentType.APPLICATION_JSON)
            .body(Json.encodeToString(customer))
        } ?: Response(NOT_FOUND)
      } ?: Response(BAD_REQUEST.description("Please provide an ID"))
    }
  )
}
