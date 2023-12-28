package model.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.Container

class ContainerState (
    name: String = "",
    width: Int = 0,
    length: Int = 0,
    height: Int = 0,
    weight: Int = 0,
    cost: Int = 0,
    count: Int = 1,
    isSelected: Boolean = false
) {
    var name by mutableStateOf(name)
    var width by mutableStateOf(width)
    var length by mutableStateOf(length)
    var height by mutableStateOf(height)
    var weight by mutableStateOf(weight)
    var cost by mutableStateOf(cost)
    var count by mutableStateOf(count)
    var isSelected by mutableStateOf(isSelected)

    companion object {
        fun create(container: Container): ContainerState {
            return ContainerState(
                name = container.name,
                width = container.width,
                length = container.length,
                height = container.height,
                weight = container.weight,
                cost = container.cost
            )
        }
    }

    fun toContainer(): Container {
        return Container(
            name = name,
            width = width,
            length = length,
            height = height,
            weight = weight,
            cost = cost,
            count = count,
            createdTime = System.currentTimeMillis()
        )
    }
}
