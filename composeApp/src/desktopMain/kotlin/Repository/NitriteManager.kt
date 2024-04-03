package Repository

import model.Cable
import model.Container
import model.DetailedContainer
import model.Rectangle
import model.Result
import model.SimpleCable
import model.SimpleContainerInfo
import org.dizitart.kno2.getRepository
import org.dizitart.kno2.nitrite
import org.dizitart.no2.Nitrite
import org.dizitart.no2.common.mapper.NitriteMapper
import org.dizitart.no2.common.mapper.SimpleNitriteMapper
import org.dizitart.no2.common.module.NitriteModule.module
import org.dizitart.no2.mvstore.MVStoreModule
import org.dizitart.no2.repository.ObjectRepository

class NitriteManager private constructor() {
    private var db: Nitrite? = null

    private fun openDb() {
        if (db == null){
            db?.close()
        }

        val rootPath: String = System.getProperty("user.home") + "/Documents/Container_Packing"

        val storeModule = MVStoreModule.withConfig()
            .filePath("${rootPath}/container_packing.db")
            .build()
        val nitriteMapper = SimpleNitriteMapper()
        nitriteMapper.registerEntityConverter(Cable.Converter)
        nitriteMapper.registerEntityConverter(Container.Converter)
        nitriteMapper.registerEntityConverter(DetailedContainer.Converter)
        nitriteMapper.registerEntityConverter(Rectangle.Converter)
        nitriteMapper.registerEntityConverter(Result.Converter)
        nitriteMapper.registerEntityConverter(SimpleCable.Converter)
        nitriteMapper.registerEntityConverter(SimpleContainerInfo.Converter)
        db = nitrite {
            loadModule(storeModule)
            loadModule(module(nitriteMapper))
        }
    }

    fun getContainerRepository(): ObjectRepository<Container> {
        if (db == null){
            openDb()
        }
        return db?.getRepository<Container>() ?: throw RuntimeException("failed to find db")
    }

    fun getResultRepository(): ObjectRepository<Result> {
        if (db == null){
            openDb()
        }
        return db?.getRepository<Result>() ?: throw RuntimeException("failed to find db")
    }

    companion object {
        private val _instance = NitriteManager()
        private const val dbName = "task.db"

        @JvmStatic
        fun getInstance(): NitriteManager {
            return _instance
        }
    }
}