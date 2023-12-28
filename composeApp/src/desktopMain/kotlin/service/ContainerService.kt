package service

import model.Container
import model.DetailedContainer
import model.Package
import model.Rectangle
import model.view.ContainerState
import util.deleteItem
import util.loadItem
import util.loadItemList
import util.saveItem
import util.updateItem
import java.util.*

fun convertToDetailedContainerList(containerList: List<Container>): List<DetailedContainer> {
    return containerList.flatMap {container ->
            val newContainerList = mutableListOf<Container>()
            for (i: Int in 1..container.count) {
                newContainerList.add(
                    Container(
                        name = container.name,
                        width = container.width,
                        length = container.length,
                        height = container.height,
                        weight = container.weight,
                        cost = container.cost,
                        count = 1,
                        createdTime = container.createdTime
                    )
                )
            }
            newContainerList
    }.map {container ->
        val areaSortedSet = TreeSet<Rectangle>()
        areaSortedSet.add(createNewFreeRectangle(container))
        DetailedContainer(
            container = container,
            totalWeight = 0,
            simpleCableList = mutableListOf(),
            usedRectangleList = mutableListOf(),
            freeRectangleSet = areaSortedSet
        )
    }.sortedBy { detailedContainer -> detailedContainer.container.cost }
}

fun loadContainerStateList(): List<ContainerState> {
    return loadItemList<Container>(Package.CONTAINER)
        .sortedByDescending { it.createdTime }
        .map(ContainerState.Companion::create)
}

fun loadContainerState(name: String): ContainerState {
    return ContainerState.create(loadItem<Container>(Package.CONTAINER, name))
}

fun updateContainer(container: Container): Boolean {
    return updateItem(Package.CONTAINER, container, container.name)
}

fun saveContainer(container: Container): Boolean {
    return saveItem(Package.CONTAINER, container, container.name)
}

fun deleteContainer(container: Container): Boolean {
    return deleteItem(Package.CONTAINER, container.name)
}
