package com.example

import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
  DBMigration.run("test")

  val handlers = CustomerHandler(TheLogics(DBCustomerStore))
  val app = PrintRequest().then(handlers.app)
  val server = app.asServer(SunHttp(9000)).start()
  println("Server started on " + server.port())
}
