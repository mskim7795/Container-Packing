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
                x = document.get("x", String::class.java).toInt(),
                y = document.get("y", String::class.java).toInt(),
                width = document.get("width", String::class.java).toInt(),
                length = document.get("length", String::class.java).toInt()
            )
        }

        override fun toDocument(rectangle: Rectangle, nitriteMapper: NitriteMapper): Document {
            return documentOf(
                "x" to rectangle.x.toString(),
                "y" to rectangle.y.toString(),
                "width" to rectangle.width.toString(),
                "length" to rectangle.length.toString()
            )
        }

    }
}