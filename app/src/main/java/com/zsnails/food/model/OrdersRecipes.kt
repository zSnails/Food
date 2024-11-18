package com.zsnails.food.model

import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as S

@S
data class OrdersRecipes(
    val id: Long? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("recipe_id")
    val recipeId: Long? = null,
    @SerialName("order_id")
    val orderId: Long? = null,
) : Serializable
