package com.zsnails.food.model

import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as S

@S
data class Order(
    var id: Long,
    @SerialName("created_at")
    var createdAt: String,
    @SerialName("owner_id")
    var ownerId: String,
    @SerialName("recipe_id")
    var recipeId: Long,
) : Serializable
