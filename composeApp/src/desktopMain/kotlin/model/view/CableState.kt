package model.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.Cable

class CableState(
    id: Int,
    name: String = "",
    width: Int = 0,
    length: Int = 0,
    height: Int = 0,
    weight: Int = 0,
    count: Int = 1
) {
    var id by mutableStateOf(id)
    var name by mutableStateOf(name)
    var width by mutableStateOf(width)
    var length by mutableStateOf(length)
    var height by mutableStateOf(height)
    var weight by mutableStateOf(weight)
    var count by mutableStateOf(count)

    companion object {
        fun create(cable: Cable): CableState {
            return CableState(
                id = cable.id,
                name = cable.name,
                width = cable.width,
                length = cable.length,
                height = cable.height,
                weight = cable.weight,
                count = cable.count
            )
        }
    }

    fun toCable(): Cable {
        return Cable(
            id = id,
            name = name,
            width = width,
            length = length,
            height = height,
            weight = weight,
            count = count,
            createdTime = System.currentTimeMillis()
        )
    }
}
