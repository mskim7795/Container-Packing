package model

import org.dizitart.kno2.documentOf
import org.dizitart.no2.collection.Document
import org.dizitart.no2.common.mapper.EntityConverter
import org.dizitart.no2.common.mapper.NitriteMapper
import java.util.TreeSet

data class DetailedContainer(
    val container: Container,
    var totalWeight: Int,
    var simpleCableList: MutableList<SimpleCable>,
    var usedRectangleList: MutableList<Rectangle>,
    var freeRectangleSet: TreeSet<Rectangle>
) {

    companion object Converter: EntityConverter<DetailedContainer> {
        override fun getEntityType(): Class<DetailedContainer> {
            return DetailedContainer::class.java
        }

        override fun fromDocument(document: Document, nitriteMapper: NitriteMapper): DetailedContainer {
            val container = nitriteMapper.tryConvert(document.get("container", Document::class.java),
                Container::class.java) as Container
            val documentSimpleCableList = document.get("simpleCableList", List::class.java) as List<Document>
            val simpleCableList = documentSimpleCableList.map { d: Document ->
                nitriteMapper.tryConvert(
                    d,
                    SimpleCable::class.java
                ) as SimpleCable
            }.toMutableList()
            val documentUsedRectangleList = document.get("usedRectangleList", List::class.java) as List<Document>
            val usedRectangleList = documentUsedRectangleList.map { d ->
                nitriteMapper.tryConvert(
                    d,
                    Rectangle::class.java
                ) as Rectangle
            }.toMutableList()
            val documentFreeRetangleList = document.get("freeRectangleSet", List::class.java) as List<Document>
            val freeRectangleSet = TreeSet<Rectangle>()
            documentFreeRetangleList.map { d ->
                nitriteMapper.tryConvert(
                    d, Rectangle::class.java
                ) as Rectangle
            }.forEach { rectangle ->
                freeRectangleSet.add(rectangle)
            }
            return DetailedContainer(
                container = container,
                totalWeight = document.get("totalWeight", String::class.java).toInt(),
                simpleCableList = simpleCableList,
                usedRectangleList = usedRectangleList,
                freeRectangleSet = freeRectangleSet
            )
        }

        override fun toDocument(detailedContainer: DetailedContainer, nitriteMapper: NitriteMapper): Document {
            val container = nitriteMapper.tryConvert(detailedContainer.container, Document::class.java) as Document
            val simpleCableList = detailedContainer.simpleCableList.map { simpleCable ->
                nitriteMapper.tryConvert(simpleCable, Document::class.java) as Document
            }
            val usedRectangleList = detailedContainer.usedRectangleList.map { rectangle ->
                nitriteMapper.tryConvert(rectangle, Document::class.java) as Document
            }
            val freeRectangleSet = detailedContainer.freeRectangleSet.toList().map { rectangle ->
                nitriteMapper.tryConvert(rectangle, Document::class.java) as Document
            }
            return documentOf(
                "container" to container,
                "totalWeight" to detailedContainer.totalWeight.toString(),
                "simpleCableList" to simpleCableList,
                "usedRectangleList" to usedRectangleList,
                "freeRectangleSet" to freeRectangleSet
            )
        }

    }
}