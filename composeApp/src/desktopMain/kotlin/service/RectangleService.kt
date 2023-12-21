package service

import model.Cable
import model.Container
import model.Rectangle

fun createNewFreeRectangle(container: Container): Rectangle {
    return Rectangle(
        x = 0,
        y = 0,
        width = container.width,
        length = container.length
    )
}

fun convertFromCableToRectangle(cable: Cable): Rectangle {
    return Rectangle(
        x = 0,
        y = 0,
        width = cable.width,
        length = cable.length
    )
}