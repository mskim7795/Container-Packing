package model

import org.dizitart.kno2.documentOf
import org.dizitart.kno2.isEmpty
import org.dizitart.no2.collection.Document
import org.dizitart.no2.common.mapper.EntityConverter
import org.dizitart.no2.common.mapper.NitriteMapper
import org.dizitart.no2.repository.annotations.Entity
import org.dizitart.no2.repository.annotations.Id
import org.slf4j.LoggerFactory
import java.util.UUID

private val logger = LoggerFactory.getLogger("Result")

@Entity
data class Result(

    @Id
    val id: String = UUID.randomUUID().toString(),
    val detailedContainerList: List<DetailedContainer> = emptyList(),
    val remainedCableList: List<SimpleCable> = emptyList(),
    var name: String = "",
    val simpleContainerInfoList: List<SimpleContainerInfo> = emptyList(),
    val cableList: List<Cable> = emptyList()
) {
    companion object Converter: EntityConverter<Result> {
        override fun getEntityType(): Class<Result> {
            return Result::class.java
        }

        override fun fromDocument(document: Document, nitriteMapper: NitriteMapper): Result {
            if (document.isEmpty()) {
                return Result()
            }
            val documentDetailedContainerList =
                document.get("detailedContainerList", List::class.java) as List<Document>
            val detailedContainerList = documentDetailedContainerList.map { d ->
                nitriteMapper.tryConvert(
                    d, DetailedContainer::class.java
                ) as DetailedContainer
            }
            val documentRemainedCableList =
                document.get("remainedCableList", List::class.java) as List<Document>
            val remainedCableList = documentRemainedCableList.map { d ->
                nitriteMapper.tryConvert(d, SimpleCable::class.java) as SimpleCable
            }
            val documentSimpleContainerInfoList =
                document.get("simpleContainerInfoList", List::class.java) as List<Document>
            val simpleContainerInfoList = documentSimpleContainerInfoList.map { d ->
                nitriteMapper.tryConvert(
                    d, SimpleContainerInfo::class.java
                ) as SimpleContainerInfo
            }
            val documentCableList = document.get("cableList", List::class.java) as List<Document>
            val cableList = documentCableList.map { d ->
                nitriteMapper.tryConvert(d, Cable::class.java) as Cable
            }
            return Result(
                id = document.get("id", String::class.java),
                detailedContainerList = detailedContainerList,
                remainedCableList = remainedCableList,
                name = document.get("name", String::class.java),
                simpleContainerInfoList = simpleContainerInfoList,
                cableList = cableList
            )
        }

        override fun toDocument(result: Result, nitriteMapper: NitriteMapper): Document {
            val documentDetailedContainerList = result.detailedContainerList.map { detailedContainer ->
                nitriteMapper.tryConvert(detailedContainer, Document::class.java) as Document
            }
            val documentRemainedCableList = result.remainedCableList.map { simpleCable ->
                nitriteMapper.tryConvert(simpleCable, Document::class.java) as Document
            }
            val documentSimpleContainerInfoList = result.simpleContainerInfoList.map { simpleContainerInfo ->
                nitriteMapper.tryConvert(simpleContainerInfo, Document::class.java) as Document
            }
            val documentCableList = result.cableList.map { cable ->
                nitriteMapper.tryConvert(cable, Document::class.java) as Document
            }
            return documentOf(
                "id" to result.id,
                "detailedContainerList" to documentDetailedContainerList,
                "remainedCableList" to documentRemainedCableList,
                "name" to result.name,
                "simpleContainerInfoList" to documentSimpleContainerInfoList,
                "cableList" to documentCableList
            )
        }

    }
}
