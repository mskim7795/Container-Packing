package model

import org.dizitart.kno2.documentOf
import org.dizitart.no2.collection.Document
import org.dizitart.no2.common.mapper.EntityConverter
import org.dizitart.no2.common.mapper.NitriteMapper

data class SimpleContainerInfo(
    val name: String,
    val count: Int,
    val container: Container
) {
    companion object Converter: EntityConverter<SimpleContainerInfo> {
        override fun getEntityType(): Class<SimpleContainerInfo> {
            return SimpleContainerInfo::class.java
        }

        override fun fromDocument(document: Document, nitriteMapper: NitriteMapper): SimpleContainerInfo {
            val container = nitriteMapper.tryConvert(
                document.get("container", Document::class.java),
                Container::class.java
            ) as Container
            return SimpleContainerInfo(
                name = document.get("name", String::class.java),
                count = document.get("count", Int::class.java),
                container = container
            )
        }

        override fun toDocument(simpleContainerInfo: SimpleContainerInfo, nitriteMapper: NitriteMapper): Document {
            val container = nitriteMapper.tryConvert(simpleContainerInfo.container, Document::class.java) as Document
            return documentOf(
                "name" to simpleContainerInfo.name,
                "count" to simpleContainerInfo.count,
                "container" to container
            )
        }

    }
}