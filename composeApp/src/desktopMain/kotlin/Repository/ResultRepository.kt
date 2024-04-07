package Repository

import model.Result

private val nitriteManager = NitriteManager.getInstance()

fun upsertResult(result: Result) {
    nitriteManager.getResultRepository().update(result, true)
}

fun findResultById(id: String): Result {
    return nitriteManager.getResultRepository().getById(id)
}

fun findResult(): List<Result> {
    return nitriteManager.getResultRepository().find().toList()
}

fun deleteResult(result: Result) {
    nitriteManager.getResultRepository().remove(result)
}