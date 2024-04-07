package service

import Repository.findContainerById
import Repository.findContainerList
import Repository.upsertContainer
import model.Container
import model.DetailedContainer
import model.Rectangle
import model.view.ContainerState
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

private val logger: Logger = LoggerFactory.getLogger("ContainerService")

fun convertToDetailedContainerList(containerList: List<Container>): List<DetailedContainer> {
    return containerList.flatMap {container ->
            val newContainerList = mutableListOf<Container>()
            for (i: Int in 1..container.count) {
                newContainerList.add(
                    Container(
                        id = container.id,
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

fun findContainerList(): List<ContainerState> {
    return findContainerList()
        .sortedByDescending { it.createdTime }
        .map(ContainerState.Companion::create)
}

fun findContainerState(id: String): ContainerState {
    return ContainerState.create(findContainerById(id))
}

fun updateContainer(container: Container): Boolean {
    upsertContainer(container)
    return true
}

fun saveContainer(container: Container): Boolean {
    upsertContainer(container)
    return true
}

fun deleteContainer(id: String): Boolean {
    val container = findContainerById(id)
    Repository.deleteContainer(container)
    return true
}
