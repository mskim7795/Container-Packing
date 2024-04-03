package service

import Repository.deleteResult
import Repository.findResult
import Repository.findResultById
import Repository.upsertResult
import model.Cable
import model.DetailedContainer
import model.Result
import model.Package
import model.SimpleCable
import model.SimpleContainerInfo
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

fun loadResultState(id: UUID): ResultState {
    return ResultState.create(findResultById(id))
}

fun updateResultName(id: UUID, name: String): Boolean {
    val result = findResultById(id)
    result.name = name
    upsertResult(result)
    return true
}

fun saveResult(result: Result): Boolean {
    upsertResult(result)
    return true
}

fun deleteResult(id: UUID): Boolean {
    val result = findResultById(id)
    deleteResult(result)
    return true
}

fun createResult(detailedContainerList: List<DetailedContainer>, remainedCableList: List<SimpleCable>, cableList: List<Cable>): Result {
    val simpleContainerInfoList = detailedContainerList.groupBy { detailedContainer ->
        detailedContainer.container.name
    }.map { entry ->
        SimpleContainerInfo(
            name = entry.key,
            count = entry.value.size,
            container = entry.value[0].container
        )
    }

    return Result(
        id = UUID.randomUUID(),
        name = convertTimeToString(LocalDateTime.now()),
        detailedContainerList = detailedContainerList,
        remainedCableList = remainedCableList,
        simpleContainerInfoList = simpleContainerInfoList,
        cableList = cableList
    )
}
