package model

import org.dizitart.kno2.documentOf
import org.dizitart.no2.collection.Document
import org.dizitart.no2.common.mapper.EntityConverter
import org.dizitart.no2.common.mapper.NitriteMapper
import java.util.UUID

data class Cable(
    val id: String,
    val name: String,
    val width: Int,
    val length: Int,
    val height: Int,
    val weight: Int,
    val count: Int,
    val createdTime: Long
) {
    companion object Converter: EntityConverter<Cable> {
        override fun getEntityType(): Class<Cable> {
            return Cable::class.java
        }

        override fun fromDocument(document: Document, nitriteMapper: NitriteMapper): Cable {
            return Cable(
                id = document.get("id", String::class.java),
                name = document.get("name", String::class.java),
                width = document.get("width", String::class.java).toInt(),
                length = document.get("length", String::class.java).toInt(),
                height = document.get("height", String::class.java).toInt(),
                weight = document.get("weight", String::class.java).toInt(),
                count = document.get("count", String::class.java).toInt(),
                createdTime = document.get("createdTime", String::class.java).toLong(),
            )
        }

        override fun toDocument(cable: Cable, nitriteMapper: NitriteMapper): Document {
            return documentOf(
                "id" to cable.id,
                "name" to cable.name,
                "width" to cable.width.toString(),
                "length" to cable.length.toString(),
                "height" to cable.height.toString(),
                "weight" to cable.weight.toString(),
                "count" to cable.count.toString(),
                "createdTime" to cable.createdTime.toString()
            )
        }

    }
}
