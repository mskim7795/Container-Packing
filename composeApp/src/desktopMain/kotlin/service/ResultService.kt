package service

import model.Result
import model.Package
import model.view.ResultState
import util.deleteItem
import util.loadItem
import util.loadItemList
import util.saveItem
import util.updateItem

fun loadResultStateList(): List<ResultState> {
    return loadItemList<Result>(Package.RESULT)
        .sortedByDescending { it.name }
        .map(ResultState.Companion::create)
}

fun loadResultState(name: String): ResultState {
    return ResultState.create(loadItem<Result>(Package.RESULT, name))
}

fun updateResultName(prevName:String, name: String): Boolean {
    val result = loadItem<Result>(Package.RESULT, prevName)
    val newResult = Result(
        name = name,
        detailedContainerList = result.detailedContainerList,
        remainedCableList = result.remainedCableList
    )
    return updateItem(Package.RESULT, newResult, newResult.name)
}

fun saveResult(result: Result): Boolean {
    return saveItem(Package.RESULT, result, result.name)
}

fun deleteResult(name: String): Boolean {
    return deleteItem(Package.RESULT, name)
}