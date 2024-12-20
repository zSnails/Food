package com.zsnails.food.model

import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as S

@S
data class Order(
    val id: Long? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("owner_id")
    val ownerId: String,
    @SerialName("price")
    val price: Double
) : Serializable
