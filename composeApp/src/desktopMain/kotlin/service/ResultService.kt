package service

import Repository.deleteResult
import Repository.findResult
import Repository.findResultById
import Repository.upsertResult
import model.*
import model.view.ResultState
import util.convertTimeToString
import util.loadItem
import java.time.LocalDateTime
import java.util.UUID

fun loadResultStateList(): List<ResultState> {
    return findResult()
        .sortedByDescending { it.name }
        .map(ResultState.Companion::create)
}

fun loadResultState(id: String): ResultState {
    return ResultState.create(findResultById(id))
}

fun updateResultName(id: String, name: String): Boolean {
    val result = findResultById(id)
    result.name = name
    upsertResult(result)
    return true
}

fun saveResult(result: Result): Boolean {
    upsertResult(result)
    return true
}

fun deleteResult(id: String): Boolean {
    val result = findResultById(id)
    deleteResult(result)
    return true
}

fun createResult(detailedContainerList: List<DetailedContainer>, remainedCableList: List<SimpleCable>,
                 cableList: List<Cable>, containerList: List<Container>): Result {
    val detailedContainerMap = detailedContainerList.groupBy { detailedContainer ->
        detailedContainer.container.id
    }
    val simpleContainerInfoList = containerList.map { container ->
        SimpleContainerInfo(
            name = container.name,
            container = container,
            count = detailedContainerMap.getOrDefault(container.id, emptyList()).size
        )
    }


    return Result(
        id = UUID.randomUUID().toString(),
        name = convertTimeToString(LocalDateTime.now()),
        detailedContainerList = detailedContainerList,
        remainedCableList = remainedCableList,
        simpleContainerInfoList = simpleContainerInfoList,
        cableList = cableList
    )
}
