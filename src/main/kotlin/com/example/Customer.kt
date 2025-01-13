package com.example

import kotlinx.serialization.Serializable

@Serializable
@JvmInline value class Id(val value: Long)

@Serializable
@JvmInline value class Email(val value: String)

@Serializable
@JvmInline value class Name(val value: String)

@Serializable
data class Customer(
  val id: Id?,
  val name: Name,
  val email: Email,
)

