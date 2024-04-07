package Repository

import exception.EmptyDocumentException
import model.Container
import org.dizitart.no2.filters.Filter

private val nitriteManager = NitriteManager.getInstance()

fun upsertContainer(container: Container) {
    nitriteManager.getContainerRepository().update(container, true)
}

fun findContainerById(id: String): Container {
    return nitriteManager.getContainerRepository().getById(id)
}

fun findContainerList(): List<Container> {
    return try {
        nitriteManager.getContainerRepository().find(Filter.ALL).toList()
    } catch (e: EmptyDocumentException) {
        emptyList()
    }
}

fun deleteContainer(container: Container) {
    nitriteManager.getContainerRepository().remove(container)
}