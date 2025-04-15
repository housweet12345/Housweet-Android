package com.housweet.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.housweet.presentation.ui.theme.ColorGroup

@Composable
fun ProfileEditCaseNumber(
    isCurrent: Boolean = true,
    number: Int,
    description: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val currentColor = if (isCurrent) ColorGroup.Primary else ColorGroup.Gray_CBCBCB
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(currentColor)
                .padding(horizontal = 5.dp, vertical = 1.dp)
        ) {
            Text(
                text = "$number",
                style = TextStyle(
                    color = ColorGroup.White_F8F8F8,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = description,
            color = currentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileEditCaseNumberPreview() {
    ProfileEditCaseNumber(isCurrent = true, number = 1, description = "프로필 입력")
}