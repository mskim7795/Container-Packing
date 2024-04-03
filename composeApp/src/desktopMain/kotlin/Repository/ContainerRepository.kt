package Repository

import model.Container
import java.util.UUID

private val containerRepository = NitriteManager.getInstance().getContainerRepository()

fun upsertContainer(container: Container) {
    containerRepository.update(container, true)
}

fun findContainerById(id: UUID): Container {
    return containerRepository.getById(id)
}

fun findContainerList(): List<Container> {
    return containerRepository.find().toList()
}

fun deleteContainer(container: Container) {
    containerRepository.remove(container)
}