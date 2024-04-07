package service

import model.Cable
import model.Package
import model.view.CableState
import util.deleteItem
import util.loadItem
import util.loadItemList
import util.saveItem
import util.updateItem

fun hasDuplicatedNames(cableList: List<Cable>): Boolean {
    return cableList.groupBy { it.name }.any { it.value.size > 1 }
}
