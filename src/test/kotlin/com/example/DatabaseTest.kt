package com.example

import org.junit.jupiter.api.BeforeAll

interface DatabaseTest {
    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            DBStore.initialize(
                DatabaseConfig(
                    driver = "org.h2.Driver",
                    url = "jdbc:h2:mem:${this::class.java.simpleName};DB_CLOSE_DELAY=-1",
                    user = "",
                    password = ""
                )
            )
        }
    }
}