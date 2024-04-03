package model

import org.dizitart.kno2.documentOf
import org.dizitart.no2.collection.Document
import org.dizitart.no2.common.mapper.EntityConverter
import org.dizitart.no2.common.mapper.NitriteMapper

data class SimpleCable(
    val id: Int = 1,
    val name: String = "",
    var count: Int = 1,
) {

    companion object Converter: EntityConverter<SimpleCable> {
        override fun getEntityType(): Class<SimpleCable> {
            return SimpleCable::class.java
        }

        override fun fromDocument(document: Document, nitriteMapper: NitriteMapper): SimpleCable {
            return SimpleCable(
                id = document?.get("id", Int::class.java) ?: throw RuntimeException("failed to load SimpleCable"),
                name = document.get("name", String::class.java) ?: throw RuntimeException("failed to load SimpleCable"),
                count = document.get("count", Int::class.java)
            )
        }

        override fun toDocument(simpleCable: SimpleCable, nitriteMapper: NitriteMapper): Document {
            return documentOf(
                "id" to simpleCable.id,
                "name" to simpleCable.name,
                "count" to simpleCable.count
            )
        }

    }
}