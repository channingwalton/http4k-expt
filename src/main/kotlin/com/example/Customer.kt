package com.example

import kotlinx.serialization.Serializable

@Serializable
@JvmInline value class Id(val value: Long)

@Serializable
data class Customer(
  val id: Id?,
  val name: String,
  val email: String,
)

