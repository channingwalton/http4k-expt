package com.example

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

data class DatabaseConfig(
    val driver: String,
    val url: String,
    val user: String,
    val password: String,
) {
    companion object {
        fun load(): DatabaseConfig {
            val config: Config = ConfigFactory.load()
            return DatabaseConfig(
                driver = config.getString("app.database.driver"),
                url = config.getString("app.database.url"),
                user = config.getString("app.database.user"),
                password = config.getString("app.database.password"),
            )
        }
    }
}

data class AppConfig(
    val serverPort: Int,
    val database: DatabaseConfig,
) {
    companion object {
        fun load(): AppConfig {
            val config: Config = ConfigFactory.load()
            return AppConfig(
                serverPort = config.getInt("app.serverPort"),
                database = DatabaseConfig.load(),
            )
        }
    }
}

