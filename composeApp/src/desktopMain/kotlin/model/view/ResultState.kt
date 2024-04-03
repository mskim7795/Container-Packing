package model.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.Result
import java.util.UUID

class ResultState(
    id: UUID,
    name: String,
    detailedContainerStateList: List<DetailedContainerState>,
    remainedCableStateList: List<SimpleCableState>
) {
    var id by mutableStateOf(id)
    var name by mutableStateOf(name)
    var detailedContainerStateList = detailedContainerStateList
    var remainedCableStateList = remainedCableStateList

    companion object {
        fun create(result: Result): ResultState {
            return ResultState(
                id = result.id,
                name = result.name,
                detailedContainerStateList = result.detailedContainerList.map {
                    DetailedContainerState.create(it)
                }.toList(),
                remainedCableStateList = result.remainedCableList.map {
                    SimpleCableState.create(it)
                }.toList()
            )
        }
    }
}