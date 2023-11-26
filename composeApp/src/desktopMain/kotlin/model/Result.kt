package model

data class Result(
    val name: String,
    val containerNameWithCountList: List<ContainerNameWithCount>,
    val cableNameList: List<String>,
    val createdTime: Long
) {
    data class ContainerNameWithCount(
        val containerName: String,
        val count: Int
    )
}
