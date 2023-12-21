package service

import model.*

fun calculateTwoDimensionalPacking(containerList: List<Container>, cableList: List<Cable>) {
    val sortedCableList = cableList.sortedByDescending { cable -> cable.width * cable.length }
    val detailedContainerList = convertToDetailedContainerList(containerList)
    val remainedCableList = mutableListOf<SimpleCable>()
    sortedCableList.forEach {cable ->
        for (i: Int in 1..cable.count) {
            if(!addCableIntoContainer(cable, detailedContainerList)) {
                addSimpleCable(cable, remainedCableList)
            }
        }
    }
}

private fun splitFreeRectangle(freeRectangle: Rectangle, usedRectangle: Rectangle): List<Rectangle> {
    // Compute the rectangles that are left.
    val newRectangles = mutableListOf<Rectangle>()

    // Left rectangle
    if (usedRectangle.x > freeRectangle.x) {
        newRectangles.add(Rectangle(usedRectangle.x - freeRectangle.x, freeRectangle.length, freeRectangle.x, freeRectangle.y))
    }

    // Right rectangle
    if (freeRectangle.x + freeRectangle.width > usedRectangle.x + usedRectangle.width) {
        newRectangles.add(Rectangle(freeRectangle.x + freeRectangle.width - usedRectangle.x - usedRectangle.width, freeRectangle.length, usedRectangle.x + usedRectangle.width, freeRectangle.y))
    }

    // Top rectangle
    if (usedRectangle.y > freeRectangle.y) {
        newRectangles.add(Rectangle(freeRectangle.width, usedRectangle.y - freeRectangle.y, freeRectangle.x, freeRectangle.y))
    }

    // Bottom rectangle
    if (freeRectangle.y + freeRectangle.length > usedRectangle.y + usedRectangle.length) {
        newRectangles.add(Rectangle(freeRectangle.width, freeRectangle.y + freeRectangle.length - usedRectangle.y - usedRectangle.length, freeRectangle.x, usedRectangle.y + usedRectangle.length))
    }

    return newRectangles
}

private fun isOverlap(rect1: Rectangle, rect2: Rectangle): Boolean {
    if (rect1.x + rect1.width <= rect2.x) return false // rect1 is left to rect2
    if (rect1.x >= rect2.x + rect2.width) return false // rect1 is right to rect2
    if (rect1.y + rect1.length <= rect2.y) return false // rect1 is above rect2
    if (rect1.y >= rect2.y + rect2.length) return false // rect1 is below rect2

    return true // rectangles overlap
}


private fun addCableIntoContainer(cable: Cable, detailedContainerList: List<DetailedContainer>): Boolean {
    for (i: Int in detailedContainerList.indices) {
        val detailedContainer = detailedContainerList[i]

        if (!checkHeight(cable, detailedContainer) || !checkWeight(cable, detailedContainer)) {
            continue
        }

        if (addCableIntoContainerInternal(cable, detailedContainer)) {
            return true;
        }
    }

    return false;
}

private fun addCableIntoContainerInternal(cable: Cable, detailedContainer: DetailedContainer): Boolean {
    val cableRectangle = convertFromCableToRectangle(cable)
    val freeRectangleSet = detailedContainer.freeRectangleSet
    val addedRectangleSet = mutableSetOf<Rectangle>()
    val removedRectangleSet = mutableSetOf<Rectangle>()
    var canSetCable = false
    freeRectangleSet.filter { rectangle ->
        val canPutRectangleInto = canPutRectangleInto(cableRectangle, rectangle)
        if (canPutRectangleInto) {
            canSetCable = true;
        }
        canPutRectangleInto
    }.forEach {
        addedRectangleSet.addAll(splitFreeRectangle(it, cableRectangle))
        removedRectangleSet.add(it)
    }
    return if (canSetCable) {
        freeRectangleSet.addAll(addedRectangleSet)
        freeRectangleSet.removeAll(removedRectangleSet)
        addSimpleCable(cable, detailedContainer.simpleCableList)
        detailedContainer.totalWeight += cable.weight
        detailedContainer.usedRectangleList += cableRectangle
        true
    } else {
        false
    }
}

private fun addSimpleCable(cable: Cable, simpleCableList: MutableList<SimpleCable>) {
    simpleCableList.firstOrNull { it.id == cable.id }?.let {
        simpleCable -> simpleCable.count += 1
    } ?: simpleCableList.add(SimpleCable(cable.id, cable.name, 1));
}

private fun canPutRectangleInto(cableRectangle: Rectangle, freeRectangle: Rectangle): Boolean {
    if (cableRectangle.width <= freeRectangle.width && cableRectangle.length <= freeRectangle.length) {
        cableRectangle.x = freeRectangle.x
        cableRectangle.y = freeRectangle.y
        return true;
    } else if (cableRectangle.width <= freeRectangle.length && cableRectangle.length <= freeRectangle.width) {
        cableRectangle.x = freeRectangle.x
        cableRectangle.y = freeRectangle.y
        rotateRectangle(cableRectangle)
        return true;
    }
    return false;
}

private fun rotateRectangle(rectangle: Rectangle) {
    val tmp = rectangle.width
    rectangle.width = rectangle.length
    rectangle.length = tmp
}

private fun checkHeight(cable: Cable, detailedContainer: DetailedContainer): Boolean {
    return cable.height <= detailedContainer.container.height;
}

private fun checkWeight(cable: Cable, detailedContainer: DetailedContainer): Boolean {
    return cable.weight + detailedContainer.totalWeight <= detailedContainer.container.weight;
}