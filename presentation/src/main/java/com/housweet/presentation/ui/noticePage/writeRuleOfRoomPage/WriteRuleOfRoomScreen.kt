package com.housweet.presentation.ui.noticePage.writeRuleOfRoomPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.R
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_7E7E7E
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.nanumSquareFontFamily

@Composable
fun WriteRuleOfRoomScreen() {
    var contentValue by remember { mutableStateOf(TextFieldValue("")) }
    WriteRuleOfRoomContent(
        contentValue = contentValue
    ) {
        contentValue = it
    }
}

@Composable
private fun WriteRuleOfRoomContent(
    contentValue: TextFieldValue,
    onContentTextChanged: (TextFieldValue) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            WriteRuleOfRoomTopBar()
        },
        containerColor = White
    ) { innerPadding ->
        RuleOfRoomContent(
            modifier = Modifier.padding(innerPadding),
            contentValue = contentValue
        ) {
            onContentTextChanged(it)
        }
    }
}

@Composable
private fun WriteRuleOfRoomTopBar() {
    Row(
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 15.dp, bottom = 17.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "back",
            modifier = Modifier
                .clip(CircleShape)
                .clickable {},
            tint = Black
        )

        GuideText(
            color = Black,
            text = "방의 규칙",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Center
        )

        GuideText(
            color = Black,
            text = "완료",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RuleOfRoomContent(
    modifier: Modifier,
    contentValue: TextFieldValue,
    onContentTextChanged: (TextFieldValue) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 20.dp)
    ) {
        BasicTextField(
            value = contentValue,
            onValueChange = {
                onContentTextChanged(it)
            },
            modifier = Modifier.padding(start = 10.dp),
            textStyle = TextStyle(
                color = Black,
                fontFamily = nanumSquareFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None
                ),
                letterSpacing = 0.sp,
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (contentValue.text.isEmpty()) {
                    GuideText(
                        color = Gray_7E7E7E,
                        text = "규칙을 입력해주세요.",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        textAlign = TextAlign.Start
                    )
                }
                innerTextField()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WriteRuleOfRoomScreenPreview() {
    WriteRuleOfRoomScreen()
}