package com.zsnails.food.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable as S
import java.io.Serializable

@S
data class Ingredient(
    var id: Long,
    @SerialName("created_at")
    var createdAt: String,
    @SerialName("nombre")
    var name: String
) : Serializable
