package com.example

import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
  Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
  transaction { SchemaUtils.create(Customers) }
  val handlers = CustomerHandler()
  val printingApp: HttpHandler = PrintRequest().then(handlers.app)

  val server = printingApp.asServer(SunHttp(9000)).start()

  println("Server started on " + server.port())
}
