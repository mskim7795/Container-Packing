package util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun convertTimeToString(time: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd HH_mm_ss")
    return time.format(formatter)
}