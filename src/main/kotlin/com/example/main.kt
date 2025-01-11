package com.example

import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
  val config = AppConfig.load()
  DBStore.initialize(config.database)
  val handlers = CustomerHandler(TheLogics(DBStore))
  val app = PrintRequest().then(handlers.app)
  val server = app.asServer(SunHttp(9000)).start()
  println("Server started on " + server.port())
}
