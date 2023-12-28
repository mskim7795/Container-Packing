package model

data class Result(
    val detailedContainerList: List<DetailedContainer> = emptyList(),
    val remainedCableList: List<SimpleCable> = emptyList(),
    val name: String = ""
)
