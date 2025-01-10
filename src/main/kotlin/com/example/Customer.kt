package com.example

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id: Long?,
    val name: String,
    val email: String,
)

