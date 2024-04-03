package model

import org.dizitart.kno2.documentOf
import org.dizitart.no2.collection.Document
import org.dizitart.no2.common.mapper.EntityConverter
import org.dizitart.no2.common.mapper.NitriteMapper

data class Rectangle(
    var x: Int,
    var y: Int,
    var width: Int,
    var length: Int
) : Comparable<Rectangle> {
    fun getArea(): Long {
        return width * length.toLong();
    }

    override fun compareTo(other: Rectangle): Int {
        return when {
            getArea() < other.getArea() -> -1
            getArea() > other.getArea() -> 1
            else -> when {
                x.toLong() * y < other.x.toLong() * other.y -> -1
                x.toLong() * y > other.x.toLong() * other.y -> 1
                else -> 0
            }
        }
    }

    companion object Converter: EntityConverter<Rectangle> {
        override fun getEntityType(): Class<Rectangle> {
            return Rectangle::class.java
        }

        override fun fromDocument(document: Document, nitriteMapper: NitriteMapper): Rectangle {
            return Rectangle(
                x = document.get("x", Int::class.java),
                y = document.get("y", Int::class.java),
                width = document.get("width", Int::class.java),
                length = document.get("length", Int::class.java)
            )
        }

        override fun toDocument(rectangle: Rectangle, nitriteMapper: NitriteMapper): Document {
            return documentOf(
                "x" to rectangle.x,
                "y" to rectangle.y,
                "width" to rectangle.width,
                "length" to rectangle.length
            )
        }

    }
}