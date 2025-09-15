package com.housweet.presentation.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_A5A5A5
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.White

data class MenuItem(
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun CustomMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    menuItems: List<MenuItem>,
    modifier: Modifier = Modifier
) {
    if (expanded) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { onDismissRequest() }
                }
        )

        Surface(
            modifier = modifier
                .padding(top = 4.dp, end = 10.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(6.dp),
                    spotColor = Gray_A5A5A5,
                    ambientColor = Gray_CBCBCB
                ),
            shape = RoundedCornerShape(6.dp),
            color = White
        ) {
            Column {
                menuItems.forEach { item ->
                    Box(modifier = Modifier
                        .clickable {
                            item.onClick()
                            onDismissRequest()
                        }
                    ) {
                        GuideText(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 90.dp, top = 12.dp, bottom = 12.dp),
                            color = Black,
                            text = item.text,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}