package model

data class Cable(
    val id: Int,
    val name: String,
    val width: Int,
    val length: Int,
    val height: Int,
    val weight: Int,
    val count: Int,
    val createdTime: Long
)
