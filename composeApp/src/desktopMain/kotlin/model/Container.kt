package model

import org.dizitart.kno2.documentOf
import org.dizitart.no2.collection.Document
import org.dizitart.no2.common.mapper.EntityConverter
import org.dizitart.no2.common.mapper.NitriteMapper
import org.dizitart.no2.repository.annotations.Entity
import org.dizitart.no2.repository.annotations.Id
import java.util.UUID

@Entity
data class Container(

    @Id
    val id: UUID = UUID.randomUUID(),
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
                "id" to container.id.toString(),
                "name" to container.name,
                "width" to container.width,
                "length" to container.length,
                "height" to container.height,
                "weight" to container.weight,
                "cost" to container.cost,
                "count" to container.count,
                "createdTime" to container.createdTime,
            )
        }

        override fun fromDocument(document: Document, nitriteMapper: NitriteMapper): Container {
            return Container(
                UUID.fromString(document.get("id", String::class.java)),
                document.get("name", String::class.java),
                document.get("width", Int::class.java),
                document.get("length", Int::class.java),
                document.get("height", Int::class.java),
                document.get("weight", Int::class.java),
                document.get("cost", Int::class.java),
                document.get("count", Int::class.java),
                document.get("createdTime", Long::class.java),
            )
        }
    }
}
