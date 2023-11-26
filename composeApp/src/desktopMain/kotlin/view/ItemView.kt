package view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.view.CableState

@Composable
fun createFieldView(modifier: Modifier, text: String, size: Float) {
    Box(modifier) {
        Text(text = "${text}:",
            fontWeight = FontWeight.Bold,
            fontSize = size.sp,
            modifier = Modifier.fillMaxSize().padding(5.dp),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun createUnitView(modifier: Modifier, unit: String) {
    Box(modifier) {
        Text(text = "(${unit})",
            fontWeight = FontWeight.Bold,
            modifier = modifier.align(Alignment.BottomCenter)
        )
    }
}

fun isNonNegativeInteger(text: String): Boolean {
    return text.isEmpty() || text.all { it.isDigit() }
}

fun isUniqueCableName(name: String, cableStateList: List<CableState>): Boolean {
    return !cableStateList.any { it.name == name }
}
