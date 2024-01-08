package service

import model.*
import model.Result
import org.slf4j.LoggerFactory
import util.convertTimeToString
import java.time.LocalDateTime
import java.util.TreeSet
import kotlin.math.max
import kotlin.math.min

private val logger = LoggerFactory.getLogger("PackingService")

fun calculateTwoDimensionalPacking(containerList: List<Container>, cableList: List<Cable>): Result {
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
    return Result(
        name = convertTimeToString(LocalDateTime.now()),
        detailedContainerList = detailedContainerList.filter { detailedContainer ->
            detailedContainer.simpleCableList.size > 0
        },
        remainedCableList = remainedCableList
    )
}

private fun splitFreeRectangle(freeRectangle: Rectangle, usedRectangle: Rectangle): List<Rectangle> {
    // Compute the rectangles that are left.
    val newRectangles = mutableListOf<Rectangle>()

    // Left rectangle
    if (freeRectangle.x < usedRectangle.x) {
        val rectangle = Rectangle(
            x = freeRectangle.x,
            y = freeRectangle.y,
            width = usedRectangle.x - freeRectangle.x,
            length = freeRectangle.length
        )
        newRectangles.add(rectangle)
    }

    // Right rectangle
    if (freeRectangle.x + freeRectangle.width > usedRectangle.x + usedRectangle.width) {
        val rectangle = Rectangle(
            x = usedRectangle.x + usedRectangle.width,
            y = freeRectangle.y,
            width = freeRectangle.width - (usedRectangle.x - freeRectangle.x) - usedRectangle.width,
            length = freeRectangle.length
        )
        newRectangles.add(rectangle)
    }

    // Top rectangle
    if (freeRectangle.y < usedRectangle.y) {
        val rectangle = Rectangle(
            x = freeRectangle.x,
            y = freeRectangle.y,
            width = freeRectangle.width,
            length = usedRectangle.y - freeRectangle.y
        )
        newRectangles.add(rectangle)
    }

    // Bottom rectangle
    if (freeRectangle.y + freeRectangle.length > usedRectangle.y + usedRectangle.length) {
        val rectangle = Rectangle(
            x = freeRectangle.x,
            y = usedRectangle.y + usedRectangle.length,
            width = freeRectangle.width,
            length = freeRectangle.length - (usedRectangle.y - freeRectangle.y) - usedRectangle.length
        )
        newRectangles.add(rectangle)
    }

    return newRectangles
}

private fun isOverlap(innerRect: Rectangle, outerRact: Rectangle): Boolean {
    if (innerRect.x < outerRact.x) return false
    if (innerRect.y < outerRact.y) return false
    if (innerRect.x + innerRect.width > outerRact.x + outerRact.width) return false
    if (innerRect.y + innerRect.length > outerRact.y + outerRact.length) return false

    return true // rectangles overlap
}

private fun compressRectangle(freeRectangleSet: TreeSet<Rectangle>): TreeSet<Rectangle> {
    val compressedRectangle = TreeSet<Rectangle>()
    freeRectangleSet.forEach { r1 ->
        val isAnyOverlappedRectangle = freeRectangleSet.any { r2 ->
            var isOverlap = false
            if (r1 != r2) {
                if (r1.getArea() < r2.getArea()) {
                    isOverlap = isOverlap(r1, r2)
                }
            }
            isOverlap
        }

        if (!isAnyOverlappedRectangle) {
            compressedRectangle.add(r1)
        }
    }
    return compressedRectangle
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

fun remainArea(cableRectangle: Rectangle, freeRectangle: Rectangle): Long {
    return freeRectangle.getArea() - overlapArea(cableRectangle, freeRectangle)
}

private fun overlapArea(r1: Rectangle, r2: Rectangle): Long {
    val xOverlap = max(0, min(r1.x + r1.width, r2.x + r2.width) - max(r1.x, r2.x))
    val yOverlap = max(0, min(r1.y + r1.length, r2.y + r2.length) - max(r1.y, r2.y))

    return xOverlap * yOverlap.toLong()
}

private fun addCableIntoContainerInternal(cable: Cable, detailedContainer: DetailedContainer): Boolean {
    val cableRectangle = convertFromCableToRectangle(cable)
    val freeRectangleSet = detailedContainer.freeRectangleSet
    val newFreeRectangleSet = TreeSet<Rectangle>(detailedContainer.freeRectangleSet)
    freeRectangleSet.firstOrNull { freeRectangle ->
        val canPutRectangleInto = canPutRectangleInto(cableRectangle, freeRectangle)
        if (canPutRectangleInto) {
            cableRectangle.x = freeRectangle.x
            cableRectangle.y = freeRectangle.y
            logger.info("cableRectangle: {}", cableRectangle)
        }
        canPutRectangleInto
    }?: return false

    freeRectangleSet.filter { freeRectangle ->
        overlapArea(cableRectangle, freeRectangle) > 0
    }.forEach {
        newFreeRectangleSet.addAll(splitFreeRectangle(it, cableRectangle))
        newFreeRectangleSet.remove(it)
    }
    logger.info("newFreeRectangleSet: {}", newFreeRectangleSet)
    detailedContainer.freeRectangleSet = compressRectangle(newFreeRectangleSet)
    logger.info("freeRectangleSet: {}", detailedContainer.freeRectangleSet)

    addSimpleCable(cable, detailedContainer.simpleCableList)
    detailedContainer.totalWeight += cable.weight
    detailedContainer.usedRectangleList += cableRectangle

    return true
}

private fun addSimpleCable(cable: Cable, simpleCableList: MutableList<SimpleCable>) {
    simpleCableList.firstOrNull { it.id == cable.id }?.let {
        simpleCable -> simpleCable.count += 1
    } ?: simpleCableList.add(SimpleCable(cable.id, cable.name, 1));
}

private fun canPutRectangleInto(cableRectangle: Rectangle, freeRectangle: Rectangle): Boolean {
    if (cableRectangle.width <= freeRectangle.width && cableRectangle.length <= freeRectangle.length) {
        return true;
    } else if (cableRectangle.width <= freeRectangle.length && cableRectangle.length <= freeRectangle.width) {
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