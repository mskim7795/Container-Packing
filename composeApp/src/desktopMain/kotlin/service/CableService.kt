package service

import model.Cable
import model.Package
import model.view.CableState
import util.deleteItem
import util.loadItem
import util.loadItemList
import util.saveItem
import util.updateItem

fun loadCableStateList(): List<CableState> {
    return loadItemList<Cable>(Package.CABLE)
        .sortedByDescending { it.createdTime }
        .map(CableState.Companion::create)
}

fun loadCableState(name: String): CableState {
    return CableState.create(loadItem<Cable>(Package.CABLE, name))
}

fun updateCable(cable: Cable): Boolean {
    return updateItem(Package.CABLE, cable, cable.name)
}

fun saveCable(cable: Cable): Boolean {
    return saveItem(Package.CABLE, cable, cable.name)
}

fun deleteCable(cable: Cable): Boolean {
    return deleteItem(Package.CABLE, cable.name)
}

fun hasDuplicatedNames(cableList: List<Cable>): Boolean {
    return cableList.groupBy { it.name }.any { it.value.size > 1 }
}
