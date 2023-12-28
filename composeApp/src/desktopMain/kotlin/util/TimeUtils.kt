package util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun convertTimeToString(time: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return time.format(formatter)
}