package Repository

import model.Result
import java.util.UUID

private val resultRepository = NitriteManager.getInstance().getResultRepository()

fun upsertResult(result: Result) {
    resultRepository.update(result, true)
}

fun findResultById(id: UUID): Result {
    return resultRepository.getById(id)
}

fun findResult(): List<Result> {
    return resultRepository.find().toList()
}

fun deleteResult(result: Result) {
    resultRepository.remove(result)
}