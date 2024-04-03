package model

import org.dizitart.kno2.documentOf
import org.dizitart.no2.collection.Document
import org.dizitart.no2.common.mapper.EntityConverter
import org.dizitart.no2.common.mapper.NitriteMapper

data class Cable(
    val id: Int = -1,
    val name: String = "",
    val width: Int = 0,
    val length: Int = 0,
    val height: Int = 0,
    val weight: Int = 0,
    val count: Int = 1,
    val createdTime: Long
) {
    companion object Converter: EntityConverter<Cable> {
        override fun getEntityType(): Class<Cable> {
            return Cable::class.java
        }

        override fun fromDocument(document: Document, nitriteMapper: NitriteMapper): Cable {
            return Cable(
                id = document.get("id", Int::class.java),
                name = document.get("name", String::class.java),
                width = document.get("width", Int::class.java),
                length = document.get("length", Int::class.java),
                height = document.get("height", Int::class.java),
                weight = document.get("weight", Int::class.java),
                count = document.get("count", Int::class.java),
                createdTime = document.get("createdTime", Long::class.java),
            )
        }

        override fun toDocument(cable: Cable, nitriteMapper: NitriteMapper): Document {
            return documentOf(
                "id" to cable.id,
                "name" to cable.name,
                "width" to cable.width,
                "length" to cable.length,
                "height" to cable.height,
                "weight" to cable.weight,
                "count" to cable.count,
                "createdTime" to cable.createdTime
            )
        }

    }
}
