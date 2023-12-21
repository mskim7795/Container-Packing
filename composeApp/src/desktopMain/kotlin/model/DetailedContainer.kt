package model

import java.util.TreeSet

data class DetailedContainer(
    val container: Container,
    var totalWeight: Int,
    var simpleCableList: MutableList<SimpleCable>,
    var usedRectangleList: MutableList<Rectangle>,
    var freeRectangleSet: TreeSet<Rectangle>
) {
}