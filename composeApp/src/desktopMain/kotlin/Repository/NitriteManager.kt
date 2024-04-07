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
        db = nitrite {
            loadModule(storeModule)
            registerEntityConverter(Cable.Converter)
            registerEntityConverter(Container.Converter)
            registerEntityConverter(DetailedContainer.Converter)
            registerEntityConverter(Rectangle.Converter)
            registerEntityConverter(Result.Converter)
            registerEntityConverter(SimpleCable.Converter)
            registerEntityConverter(SimpleContainerInfo.Converter)
        }
    }

    fun getContainerRepository(): ObjectRepository<Container> {
        if (db == null){
            openDb()
        }
        return db?.getRepository(Container::class.java) ?: throw RuntimeException("failed to find db")
    }

    fun getResultRepository(): ObjectRepository<Result> {
        if (db == null){
            openDb()
        }
        return db?.getRepository() ?: throw RuntimeException("failed to find db")
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