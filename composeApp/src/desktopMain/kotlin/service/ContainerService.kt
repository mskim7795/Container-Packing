package service

import model.Container
import model.Package
import model.view.ContainerState
import util.deleteItem
import util.loadItem
import util.loadItemList
import util.saveItem
import util.updateItem


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
