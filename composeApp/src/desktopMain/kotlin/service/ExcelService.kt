package service

import model.Cable
import model.DetailedContainer
import model.Result
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import util.saveResultExcelFile
import java.io.InputStream

private const val columnSize: Int = 2
fun saveExcelWithResult(result: Result): Unit {
    val workbook = XSSFWorkbook()
    val headerStyle = getHeaderCellStyle(workbook)
    val valueCellStyle = getValueCellStyle(workbook)

    val overviewSheet = workbook.createSheet("Overview")

    val overviewColumnCount = createOverviewSheet(overviewSheet, result, headerStyle, valueCellStyle)
    for(i in 0..overviewColumnCount) {
        overviewSheet.autoSizeColumn(i)
    }

    val detailSheet = workbook.createSheet("Detail")
    val detailColumnCount = createDetailSheet(detailSheet, result.detailedContainerList, headerStyle, valueCellStyle)
    for(i in 0..detailColumnCount) {
        detailSheet.autoSizeColumn(i)
    }
    saveResultExcelFile(workbook, result.id)
}

fun createOverviewSheet(sheet: XSSFSheet, result: Result, headerStyle: XSSFCellStyle, valueCellStyle: XSSFCellStyle): Int {
    var rowCount: Int = 0
    var columnCount: Int = 0
    sheet.addMergedRegion(CellRangeAddress(rowCount, rowCount, columnCount, columnCount + 6))
    var commonRow = getRow(sheet, rowCount++)
    createCell(commonRow, "Container Info", columnCount, headerStyle)
    var headerRow = getRow(sheet, rowCount++)
    createCell(headerRow, "Container name", columnCount, headerStyle)
    createCell(headerRow, "Width (mm)", columnCount + 1, headerStyle)
    createCell(headerRow, "Length (mm)", columnCount + 2, headerStyle)
    createCell(headerRow, "Height (mm)", columnCount + 3, headerStyle)
    createCell(headerRow, "Limited weight (kg)", columnCount + 4, headerStyle)
    createCell(headerRow, "Cost (\$)", columnCount + 5, headerStyle)
    createCell(headerRow, "Used Count", columnCount + 6, headerStyle)

    result.simpleContainerInfoList.forEach { simpleContainerInfo ->
        val row = getRow(sheet, rowCount++)
        createCell(row, simpleContainerInfo.name, columnCount, valueCellStyle)
        createCell(row, simpleContainerInfo.container.width.toString(), columnCount + 1, valueCellStyle)
        createCell(row, simpleContainerInfo.container.length.toString(), columnCount + 2, valueCellStyle)
        createCell(row, simpleContainerInfo.container.height.toString(), columnCount + 3, valueCellStyle)
        createCell(row, simpleContainerInfo.container.weight.toString(), columnCount + 4, valueCellStyle)
        createCell(row, simpleContainerInfo.container.cost.toString(), columnCount + 5, valueCellStyle)
        createCell(row, simpleContainerInfo.count.toString(), columnCount + 6, valueCellStyle)
    }

    columnCount += 8
    rowCount = 0
    sheet.addMergedRegion(CellRangeAddress(rowCount, rowCount, columnCount, columnCount + 5))
    commonRow = getRow(sheet, rowCount++)
    createCell(commonRow, "Total(used + remained) Cable Info", columnCount, headerStyle)
    headerRow = getRow(sheet, rowCount++)
    createCell(headerRow, "Cable name", columnCount, headerStyle)
    createCell(headerRow, "Width (mm)", columnCount + 1, headerStyle)
    createCell(headerRow, "Length (mm)", columnCount + 2, headerStyle)
    createCell(headerRow, "Height (mm)", columnCount + 3, headerStyle)
    createCell(headerRow, "Weight (kg)", columnCount + 4, headerStyle)
    createCell(headerRow, "Count", columnCount + 5, headerStyle)

    result.cableList.forEach { cable ->
        val row = getRow(sheet, rowCount++)
        createCell(row, cable.name, columnCount, valueCellStyle)
        createCell(row, cable.width.toString(), columnCount + 1, valueCellStyle)
        createCell(row, cable.length.toString(), columnCount + 2, valueCellStyle)
        createCell(row, cable.height.toString(), columnCount + 3, valueCellStyle)
        createCell(row, cable.weight.toString(), columnCount + 4, valueCellStyle)
        createCell(row, cable.count.toString(), columnCount + 5, valueCellStyle)
    }

    columnCount += 7
    rowCount = 0
    sheet.addMergedRegion(CellRangeAddress(rowCount, rowCount, columnCount, columnCount + 1))
    commonRow = getRow(sheet, rowCount++)
    createCell(commonRow, "Remained Cable Info", columnCount, headerStyle)
    headerRow = getRow(sheet, rowCount++)
    createCell(headerRow, "Cable name", columnCount, headerStyle)
    createCell(headerRow, "Count", columnCount + 1, headerStyle)

    result.remainedCableList.forEach { cable ->
        val row = getRow(sheet, rowCount++)
        createCell(row, cable.name, columnCount, valueCellStyle)
        createCell(row, cable.count.toString(), columnCount + 1, valueCellStyle)
    }

    return columnCount + 8
}

fun createDetailSheet(sheet: XSSFSheet, detailedContainerList: List<DetailedContainer>, headerStyle: XSSFCellStyle, valueStyle: XSSFCellStyle): Int {
    var startRow = 0
    var startColumn = 0
    var containerCount = 1
    detailedContainerList.forEach { detailedContainer ->
        val pair = createDetailContainerResult(sheet, detailedContainer, startRow, startColumn, containerCount++, headerStyle, valueStyle)
        startRow = pair.first
        startColumn = pair.second
    }

    return startColumn + columnSize
}

fun createDetailContainerResult(sheet: XSSFSheet, detailedContainer: DetailedContainer, startRow: Int, startColumn: Int, containerCount: Int, headerStyle: XSSFCellStyle, valueStyle: XSSFCellStyle): Pair<Int, Int> {
    var rowCount: Int = startRow
    val commonRow = getRow(sheet, rowCount++)
    sheet.addMergedRegion(CellRangeAddress(startRow, startRow, startColumn, startColumn + 1))
    createCell(commonRow, "Num ${containerCount}, Container name: ${detailedContainer.container.name}", startColumn, headerStyle)

    val headerRow = getRow(sheet, rowCount++)
    createCell(headerRow, "Cable name", startColumn, headerStyle)
    createCell(headerRow, "Cable count", startColumn + 1, headerStyle)

    detailedContainer.simpleCableList.forEach { simpleCable ->
        val row = getRow(sheet, rowCount++)

        createCell(row, simpleCable.name, startColumn, valueStyle)
        createCell(row, simpleCable.count.toString(), startColumn + 1, valueStyle)
    }
    return calculateRowAndColumnValue(rowCount, startColumn, containerCount)
}

fun calculateRowAndColumnValue(row: Int, column: Int, containerCount: Int): Pair<Int, Int> {
    return if ((containerCount) % 10 == 0) {
        Pair(0, column + columnSize + 1)
    } else {
        Pair(row + 1, column)
    }
}

private fun getRow(sheet: XSSFSheet, rowCount: Int): XSSFRow {
    return sheet.getRow(rowCount) ?: sheet.createRow(rowCount)
}

private fun getHeaderCellStyle(workbook: XSSFWorkbook): XSSFCellStyle {
    return workbook.createCellStyle().apply {
        fillForegroundColor = IndexedColors.GREY_40_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        borderTop = BorderStyle.THIN
        borderBottom = BorderStyle.THIN
        borderLeft = BorderStyle.THIN
        borderRight = BorderStyle.THIN
    }
}

private fun getValueCellStyle(workbook: XSSFWorkbook): XSSFCellStyle {
    return workbook.createCellStyle().apply {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        borderTop = BorderStyle.THIN
        borderBottom = BorderStyle.THIN
        borderLeft = BorderStyle.THIN
        borderRight = BorderStyle.THIN
    }
}

private fun createCell(row: XSSFRow, value: String, columnCount: Int, style: XSSFCellStyle) {
    val cell = row.createCell(columnCount)
    cell.setCellValue(value)
    cell.cellStyle = style
}

fun convertCableListFromExcel(inputStream: InputStream): List<Cable> {
    val workbook = XSSFWorkbook(inputStream)
    val sheet = workbook.getSheetAt(0)
    val cableList = mutableListOf<Cable>()

    for (rowIndex in 2 until findFirstEmptyRow(sheet)) {
        val row = sheet.getRow(rowIndex)
        System.currentTimeMillis()
        if (row != null) {
            val cable = Cable(
                id = rowIndex.toString(),
                name = if (!row.getCell(1)?.toString().isNullOrBlank())
                    row.getCell(1)?.toString().orEmpty() else "undefined",
                count = row.getCell(2)?.numericCellValue?.toInt() ?: 0,
                width = row.getCell(3)?.numericCellValue?.toInt() ?: 0,
                length = row.getCell(4)?.numericCellValue?.toInt() ?: 0,
                height = row.getCell(5)?.numericCellValue?.toInt() ?: 0,
                weight = row.getCell(6)?.numericCellValue?.toInt() ?: 0,
                createdTime = System.currentTimeMillis()
            )
            cableList.add(cable)
        }
    }

    return cableList.toList()
}

private fun findFirstEmptyRow(sheet: Sheet): Int {
    for (rowIndex in 2..sheet.lastRowNum) {
        val row = sheet.getRow(rowIndex)
        if (row == null || isCellEmpty(row.getCell(1))) {
            return rowIndex
        }
        var isEmpty = true
        for (cell in row) {
            if (cell.toString().isNotBlank()) {
                isEmpty = false
                break
            }
        }
        if (isEmpty) {
            return rowIndex
        }
    }
    return 1
}

private fun isCellEmpty(cell: Cell?): Boolean {
    if (cell == null) {
        return true
    }
    return when (cell.cellType) {
        CellType.BLANK -> true
        CellType.STRING -> cell.stringCellValue.isBlank()
        CellType.NUMERIC -> false
        CellType.BOOLEAN -> false
        CellType.FORMULA -> cell.cellFormula.isBlank()
        else -> false
    }
}
