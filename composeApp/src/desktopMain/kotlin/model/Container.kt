package model

data class Container(
    val name: String,
    val width: Int,
    val length: Int,
    val height: Int,
    val weight: Int,
    val cost: Int,
    val count: Int = 1,
    val createdTime: Long
)
