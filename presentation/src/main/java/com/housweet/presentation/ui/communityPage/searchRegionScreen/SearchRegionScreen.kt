package com.housweet.presentation.ui.communityPage.searchRegionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.R
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.nanumSquareFontFamily

@Composable
fun SearchRegionScreen(modifier: Modifier) {
    var inputText by remember { mutableStateOf("") }
    var searchResultList by remember { mutableStateOf(listOf<String>()) }
    SearchRegionContent(
        modifier = modifier,
        inputText = inputText
    ) {
        inputText = it

    }
}

@Composable
private fun SearchRegionContent(
    modifier: Modifier,
    inputText: String,
    onInputTextChanged: (String) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SearchRegionTopBar(inputText) {
                onInputTextChanged(it)
            }
        },
        containerColor = White
    ) { innerPadding ->
        SearchRegionResultListView(
            inputText = inputText,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun SearchRegionTopBar(
    inputText: String,
    onInputTextChanged: (String) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .background(Purple)
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment =Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "back",
            modifier = Modifier.padding(start = 20.dp),
            tint = White
        )

        BasicTextField(
            value = inputText,
            onValueChange = {
                onInputTextChanged(it)
            },
            modifier = Modifier.padding(start = 10.dp),
            textStyle = TextStyle(
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = nanumSquareFontFamily,
                color = White
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (inputText.isEmpty()) {
                    Text(
                        text = "지역명을 입력해주세요.", style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = nanumSquareFontFamily,
                            color = Gray_CBCBCB
                        )
                    )
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = "search",
            modifier = Modifier.padding(end = 20.dp),
            tint = White
        )
    }
}

@Composable
private fun SearchRegionResultListView(
    inputText: String,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier
        .fillMaxWidth()
        .padding(start = 20.dp, top = 20.dp)
    ) {
        items(25) {
            val fullText = "서울특별시 송파구 문정동"

            if (inputText.isNotEmpty() && fullText.contains(inputText, ignoreCase = true)) {
                val startIndex = fullText.indexOf(inputText, ignoreCase = true)
                val endIndex = startIndex + inputText.length
                Row {
                    if (startIndex > 0) {
                        GuideText(
                            color = Black,
                            text = fullText.substring(0, startIndex),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    GuideText(
                        color = Purple,
                        text = fullText.substring(startIndex, endIndex),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    if (endIndex < fullText.length) {
                        GuideText(
                            color = Black,
                            text = fullText.substring(endIndex),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview
@Composable
private fun SearchRegionScreenPreview() {
    SearchRegionContent(
        modifier = Modifier,
        inputText = "송파구"
    )
}

