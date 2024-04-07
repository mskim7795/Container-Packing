package model

import exception.EmptyDocumentException
import org.dizitart.kno2.documentOf
import org.dizitart.kno2.isEmpty
import org.dizitart.no2.collection.Document
import org.dizitart.no2.common.mapper.EntityConverter
import org.dizitart.no2.common.mapper.NitriteMapper
import org.dizitart.no2.repository.annotations.Entity
import org.dizitart.no2.repository.annotations.Id
import java.util.UUID

@Entity
data class Container(

    @Id
    val id: String = UUID.randomUUID().toString(),

    val name: String = "",
    val width: Int = 0,
    val length: Int = 0,
    val height: Int = 0,
    val weight: Int = 0,
    val cost: Int = 0,
    val count: Int = 1,
    val createdTime: Long = -1
) {
    fun getAreaPerCost(): Double {
        return length * height / cost.toDouble()
    }

    companion object Converter: EntityConverter<Container> {
        override fun getEntityType(): Class<Container> {
            return Container::class.java
        }

        override fun toDocument(container: Container, nitriteMapper: NitriteMapper): Document {
            return documentOf(
                "id" to container.id,
                "name" to container.name,
                "width" to container.width.toString(),
                "length" to container.length.toString(),
                "height" to container.height.toString(),
                "weight" to container.weight.toString(),
                "cost" to container.cost.toString(),
                "count" to container.count.toString(),
                "createdTime" to container.createdTime.toString(),
            )
        }

        override fun fromDocument(document: Document, nitriteMapper: NitriteMapper): Container {
            if (document.isEmpty()) {
                return Container()
            }
            return Container(
                document.get("id", String::class.java),
                document.get("name", String::class.java),
                document.get("width", String::class.java).toInt(),
                document.get("length", String::class.java).toInt(),
                document.get("height", String::class.java).toInt(),
                document.get("weight", String::class.java).toInt(),
                document.get("cost", String::class.java).toInt(),
                document.get("count", String::class.java).toInt(),
                document.get("createdTime", String::class.java).toLong(),
            )
        }
    }
}
