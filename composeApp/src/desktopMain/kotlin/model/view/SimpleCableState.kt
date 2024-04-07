package model.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.SimpleCable

class SimpleCableState(
    id: String,
    name: String = "",
    count: Int = 1,
) {
    var id by mutableStateOf(id)
    var name by mutableStateOf(name)
    var count by mutableStateOf(count)

    companion object {
        fun create(simpleCable: SimpleCable): SimpleCableState {
            return SimpleCableState(
                id = simpleCable.id,
                name = simpleCable.name,
                count = simpleCable.count
            )
        }
    }

    fun toSimpleCable(simpleCableState: SimpleCableState): SimpleCable {
        return SimpleCable(
            id = simpleCableState.id,
            name = simpleCableState.name,
            count = simpleCableState.count
        )
    }
}