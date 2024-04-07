package service

import model.DetailedContainer
import model.Result
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import util.saveResultExcelFile

private const val columnSize: Int = 4
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
    createCell(commonRow, "Num $containerCount", startColumn, headerStyle)

    val headerRow = getRow(sheet, rowCount++)
    createCell(headerRow, "Container name: ${detailedContainer.container.name}", startColumn, headerStyle)
    createCell(headerRow, "Cable name", startColumn + 1, headerStyle)
    createCell(headerRow, "Cable count", startColumn + 2, headerStyle)

    detailedContainer.simpleCableList.forEach { simpleCable ->
        val row = getRow(sheet, rowCount++)

        createCell(row, simpleCable.name, startColumn + 1, valueStyle)
        createCell(row, simpleCable.count.toString(), startColumn + 2, valueStyle)
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
        // 배경색 설정
        fillForegroundColor = IndexedColors.GREY_40_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
    }
}

private fun getValueCellStyle(workbook: XSSFWorkbook): XSSFCellStyle {
    return workbook.createCellStyle().apply {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
    }
}

private fun createCell(row: XSSFRow, value: String, columnCount: Int, style: XSSFCellStyle) {
    val cell = row.createCell(columnCount)
    cell.setCellValue(value)
    cell.cellStyle = style
}
