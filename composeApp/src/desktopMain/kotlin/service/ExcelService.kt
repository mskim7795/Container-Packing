package service

import model.DetailedContainer
import model.Result
import org.apache.poi.ss.formula.functions.Column
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

private const val columnSize: Int = 4

fun saveExcelWithResult(result: Result): Unit {
    val workbook = XSSFWorkbook()
    val headerStyle = getHeaderCellStyle(workbook)

    val overviewSheet = workbook.createSheet("Overview")

    createOverviewSheet(overviewSheet, result, headerStyle)

    val detailSheet = workbook.createSheet("Detail")
    createDetailSheet(detailSheet, result.detailedContainerList, headerStyle)
}

fun createOverviewSheet(sheet: XSSFSheet, result: Result, headerStyle: XSSFCellStyle): Unit {
    var rowCount: Int = 0
    var columnCount: Int = 0
    var headerRow = getRow(sheet, rowCount++)
    createHeaderCell(headerRow, "Container", columnCount, headerStyle)
    createHeaderCell(headerRow, "Width (mm)", columnCount + 1, headerStyle)
    createHeaderCell(headerRow, "Length (mm)", columnCount + 2, headerStyle)
    createHeaderCell(headerRow, "Height (mm)", columnCount + 3, headerStyle)
    createHeaderCell(headerRow, "Limited weight (kg)", columnCount + 4, headerStyle)
    createHeaderCell(headerRow, "Cost (\$)", columnCount + 5, headerStyle)
    createHeaderCell(headerRow, "Used Count", columnCount + 6, headerStyle)

    result.simpleContainerInfoList.forEach { simpleContainerInfo ->
        val row = getRow(sheet, rowCount++)
        row.createCell(columnCount).setCellValue(simpleContainerInfo.name)
        row.createCell(columnCount + 1).setCellValue(simpleContainerInfo.container.width.toString())
        row.createCell(columnCount + 2).setCellValue(simpleContainerInfo.container.length.toString())
        row.createCell(columnCount + 3).setCellValue(simpleContainerInfo.container.height.toString())
        row.createCell(columnCount + 4).setCellValue(simpleContainerInfo.container.weight.toString())
        row.createCell(columnCount + 5).setCellValue(simpleContainerInfo.container.cost.toString())
        row.createCell(columnCount + 6).setCellValue(simpleContainerInfo.container.count.toString())
    }

    columnCount += 8
    rowCount = 0
    headerRow = getRow(sheet, rowCount++)
    createHeaderCell(headerRow, "Cable", columnCount, headerStyle)
    createHeaderCell(headerRow, "Width (mm)", columnCount + 1, headerStyle)
    createHeaderCell(headerRow, "Length (mm)", columnCount + 2, headerStyle)
    createHeaderCell(headerRow, "Height (mm)", columnCount + 3, headerStyle)
    createHeaderCell(headerRow, "Weight (kg)", columnCount + 4, headerStyle)
    createHeaderCell(headerRow, "Count", columnCount + 5, headerStyle)

    result.cableList.forEach { cable ->
        val row = getRow(sheet, rowCount++)
        row.createCell(columnCount).setCellValue(cable.name)
        row.createCell(columnCount + 1).setCellValue(cable.width.toString())
        row.createCell(columnCount + 2).setCellValue(cable.length.toString())
        row.createCell(columnCount + 3).setCellValue(cable.height.toString())
        row.createCell(columnCount + 4).setCellValue(cable.weight.toString())
        row.createCell(columnCount + 5).setCellValue(cable.count.toString())
    }

}

fun createDetailSheet(sheet: XSSFSheet, detailedContainerList: List<DetailedContainer>, headerStyle: XSSFCellStyle) {
    var startRow = 0
    var startColumn = 0
    var containerCount = 1
    detailedContainerList.forEach { detailedContainer ->
        val pair = createDetailContainerResult(sheet, detailedContainer, startRow, startColumn, containerCount++, headerStyle)
        startRow = pair.first
        startColumn = pair.second
    }
}

fun createDetailContainerResult(sheet: XSSFSheet, detailedContainer: DetailedContainer, startRow: Int, startColumn: Int, containerCount: Int, headerStyle: XSSFCellStyle): Pair<Int, Int> {
    var rowCount: Int = startRow
    val headerRow = getRow(sheet, rowCount++)

    createHeaderCell(headerRow, "Num $containerCount", 0, headerStyle)
    createHeaderCell(headerRow, detailedContainer.container.name, 1, headerStyle)
    createHeaderCell(headerRow, "Cable name", 2, headerStyle)
    createHeaderCell(headerRow, "Cable count", 3, headerStyle)

    detailedContainer.simpleCableList.forEach { simpleCable ->
        val row = getRow(sheet, rowCount++)

        row.createCell(2).setCellValue(simpleCable.name)
        row.createCell(3).setCellValue(simpleCable.count.toString())
    }
    return calculateRowAndColumnValue(rowCount, startColumn, containerCount)
}

fun calculateRowAndColumnValue(row: Int, column: Int, containerCount: Int): Pair<Int, Int> {
    return if ((containerCount - 1) % 10 == 0) {
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
        fillForegroundColor = IndexedColors.LIGHT_BLUE.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
    }
}

private fun createHeaderCell(headerRow: XSSFRow, name: String, columnCount: Int, headerStyle: XSSFCellStyle) {
    val cell = headerRow.createCell(columnCount)
    cell.setCellValue(name)
    cell.cellStyle = headerStyle
}
