package model.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.DetailedContainer
import java.util.UUID

class DetailedContainerState(
    containerState: ContainerState,
    totalWeight: Int,
    simpleCableStateList: List<SimpleCableState>,
) {
    var containerState by mutableStateOf(containerState)
    var totalWeight by mutableStateOf(totalWeight)
    var simpleCableStateList = simpleCableStateList

    companion object {
        fun create(detailedContainer: DetailedContainer): DetailedContainerState {
            return DetailedContainerState(
                containerState = ContainerState.create(detailedContainer.container),
                totalWeight = detailedContainer.totalWeight,
                simpleCableStateList = detailedContainer.simpleCableList.map {
                    SimpleCableState.create(it)
                }.toList()
            )
        }
    }
}