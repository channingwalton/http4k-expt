package com.example

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

object DBMigration {
  fun run(dbName: String) {
    val url = "jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1"

    Database.connect(url = url, driver = "org.h2.Driver")
    Flyway.configure()
      .dataSource(url, "", "")
      .load()
      .migrate()
  }
}