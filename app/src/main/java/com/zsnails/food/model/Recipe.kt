package com.zsnails.food.model

import kotlinx.serialization.Serializable as S
import kotlinx.serialization.SerialName
import java.io.Serializable

@S
data class Recipe(
    var id: Long,
    @SerialName("nombre")
    var name: String?,
    @SerialName("descripcion")
    var description: String?,
    @SerialName("instrucciones")
    var instructions: List<String>?,
    @SerialName("created_at")
    var createdAt: String?,
    @SerialName("id_rest")
    var restaurantId: String?
) : Serializable