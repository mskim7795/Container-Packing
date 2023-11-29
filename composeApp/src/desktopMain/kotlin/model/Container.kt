package model

data class Container(
    val name: String = "",
    val width: Int = 0,
    val length: Int = 0,
    val height: Int = 0,
    val weight: Int = 0,
    val cost: Int = 0,
    val count: Int = 1,
    val createdTime: Long = -1
)
