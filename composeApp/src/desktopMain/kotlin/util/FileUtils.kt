package util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import model.Package
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.util.stream.Collectors

val gson = Gson()
val rootPath: String = System.getProperty("user.home") + "/Documents/Container_Packing"

inline fun <reified T> loadItemList(packageName: Package): List<T> {
    val indexFile = File("${rootPath}/${packageName}/index.json")
    val indexJson = indexFile.readText()
    val indexSet = gson.fromJson<Set<String>>(indexJson, object : TypeToken<Set<String>>() {}.type)
    return loadItemFileList(packageName, indexSet).stream()
        .map { gson.fromJson(it, T::class.java) }
        .collect(Collectors.toList())
}

fun initializeRootFolder() {
    val rootDirectory = File(rootPath)
    if (!rootDirectory.exists()) {
        rootDirectory.mkdirs()
    }

    Package.entries.forEach {
        val packageDirectory = File("${rootPath}/${it}")
        if (!packageDirectory.exists()) {
            packageDirectory.mkdirs()
        }
    }
}

fun saveResultExcelFile(workbook: XSSFWorkbook, name: String) {
    val fileOutputStream = FileOutputStream(File("${rootPath}/${Package.RESULT}", "${name}.xlsx"))
    workbook.write(fileOutputStream)
    fileOutputStream.close()
}

fun chooseSavePath(): String? {
    val fileDialog = FileDialog(Frame(), "Save File", FileDialog.SAVE)
    fileDialog.isVisible = true
    val directory = fileDialog.directory
    val filename = fileDialog.file
    return if (directory != null && filename != null) {
        "$directory${filename}.xlsx"
    } else {
        null
    }
}

fun downloadResultFile(name: String, savePath: String) {
    val sourceFile = File("${rootPath}/${Package.RESULT}", "${name}.xlsx")
    val destinationFile = File(savePath)

    if (sourceFile.exists()) {
        val inputChannel: FileChannel = FileInputStream(sourceFile).channel
        val outputChannel: FileChannel = FileOutputStream(destinationFile).channel
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
        inputChannel.close()
        outputChannel.close()
    }
}

fun loadItemFileList(packageName: Package, indexSet: Set<String>): Set<String> {
    return indexSet.stream()
        .filter { File("${rootPath}/${packageName}/list/${it}.json").exists() }
        .map { File("${rootPath}/${packageName}/list/${it}.json").readText() }
        .collect(Collectors.toSet())
}

inline fun <reified T> loadItem(packageName: Package, itemName: String): T {
    val itemJson = File("${rootPath}/${packageName}/list/${itemName}.json").readText()
    return gson.fromJson(itemJson, T::class.java)
}

fun <T> updateItem(packageName: Package, item: T, itemName: String): Boolean {
    val indexSet: MutableSet<String> = loadIndexSet(packageName)
    indexSet.add(itemName)
    saveIndexList(packageName, indexSet)

    val itemJson = gson.toJson(item)
    File("${rootPath}/${packageName}/list/${itemName}.json").writeText(itemJson)
    return true
}

fun <T> saveItem(packageName: Package, item: T, itemName: String): Boolean {
    val indexSet: MutableSet<String> = loadIndexSet(packageName)
    if (indexSet.contains(itemName)) {
        return false
    }
    indexSet.add(itemName)
    saveIndexList(packageName, indexSet)

    val itemJson = gson.toJson(item)
    File("${rootPath}/${packageName}/list/${itemName}.json").writeText(itemJson)
    return true
}

fun deleteItem(packageName: Package, itemName: String): Boolean {
    val indexSet: MutableSet<String> = loadIndexSet(packageName)
    if (!indexSet.contains(itemName)) {
        return false
    }
    indexSet.remove(itemName)
    saveIndexList(packageName, indexSet)

    File("${rootPath}/${packageName}/list/${itemName}.json").delete()
    return true
}

private fun loadIndexSet(packageName: Package): MutableSet<String> {
    val indexJson = File("${rootPath}/${packageName}/index.json").readText()
    val type = object : TypeToken<Set<String>>() {}.type
    return gson.fromJson(indexJson, type)
}

private fun saveIndexList(packageName: Package, indexSet: Set<String>): Unit {
    val indexListJson = gson.toJson(indexSet)
    File("${rootPath}/${packageName}/index.json").writeText(indexListJson)
}
