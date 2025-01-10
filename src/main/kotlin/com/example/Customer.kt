package com.example

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Customer(val id: Long?, val name: String, val email: String) {
  fun toJson() = Json.encodeToString(this)

  companion object Factory {
    fun fromJsonString(json: String): Either<String, Customer> =
      try {
        Json.decodeFromString<Customer>(json).right()
      } catch (e: SerializationException) {
        "Unable to convert JSON to Customer. ${e.message}".left()
      }
  }
}
