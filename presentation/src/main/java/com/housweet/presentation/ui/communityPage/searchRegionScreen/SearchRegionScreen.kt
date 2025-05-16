package com.housweet.presentation.ui.communityPage.searchRegionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.housweet.domain.model.Coordinate
import com.housweet.presentation.R
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.startPage.LoadingScreen
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.nanumSquareFontFamily
import com.housweet.presentation.utils.RegionUtils

@Composable
fun SearchRegionScreen(
    modifier: Modifier,
    searchRegionViewModel: SearchRegionViewModel = hiltViewModel(),
    onMapScreen: (Coordinate) -> Unit,
    onBackBtnClick: () -> Unit
) {
    val uiState: SearchRegionUiState by searchRegionViewModel.uiState.collectAsState(initial = SearchRegionUiState.Idle)
    val snackBarHostState = remember { SnackbarHostState() }
    var inputText by remember { mutableStateOf("") }
    var autoCompleteTextList by remember { mutableStateOf(listOf<String>()) }
    val context = LocalContext.current
    val regionUtils = RegionUtils(context)

    LaunchedEffect(Unit) {
        searchRegionViewModel.event.collect { event ->
            when (event) {
                SearchRegionEvent.Error -> {
                    snackBarHostState.showSnackbar(
                        message = "지역명을 정확히 입력해주세요.",
                        actionLabel = "닫기",
                        duration = SnackbarDuration.Short
                    )
                }

                is SearchRegionEvent.Success -> {
                    onMapScreen(event.coordinate)
                }
            }
        }
    }

    when (uiState) {
        SearchRegionUiState.Idle -> {
            SearchRegionContent(
                modifier = modifier,
                snackBarHostState = snackBarHostState,
                inputText = inputText,
                autoCompleteTextList = autoCompleteTextList,
                onInputTextChanged = {
                    inputText = it
                    autoCompleteTextList = regionUtils.getFullAddress(it.replace(Regex(" $"), ""))
                },
                onAutoCompleteTextClicked = {
                    inputText = it
                    autoCompleteTextList = regionUtils.getFullAddress(it.replace(Regex(" $"), ""))
                },
                onSearchResultClicked = {
                    val isValidAddress = regionUtils.isValidFullAddress(inputText.trim())
                    if (isValidAddress) {
                        searchRegionViewModel.geoCodingWithNaver(inputText.trim())
                    } else {
                        searchRegionViewModel.error()
                    }
                },
                onBackBtnClick = onBackBtnClick
            )
        }

        SearchRegionUiState.IsLoading -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun SearchRegionContent(
    modifier: Modifier,
    snackBarHostState: SnackbarHostState,
    inputText: String,
    autoCompleteTextList: List<String>,
    onInputTextChanged: (String) -> Unit,
    onAutoCompleteTextClicked: (String) -> Unit,
    onSearchResultClicked: () -> Unit,
    onBackBtnClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SearchRegionTopBar(
                inputText = inputText,
                onInputTextChanged = onInputTextChanged,
                onSearchResultClicked = onSearchResultClicked,
                onBackBtnClick = onBackBtnClick
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        containerColor = White
    ) { innerPadding ->
        SearchRegionResultListView(
            modifier = Modifier.padding(innerPadding),
            inputText = inputText,
            autoCompleteTextList = autoCompleteTextList
        ) { completeText ->
            onAutoCompleteTextClicked(completeText)
        }
    }
}

@Composable
private fun SearchRegionTopBar(
    inputText: String,
    onInputTextChanged: (String) -> Unit,
    onSearchResultClicked: () -> Unit,
    onBackBtnClick: () -> Unit
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
            modifier = Modifier
                .padding(start = 20.dp)
                .clip(CircleShape)
                .clickable {
                    onBackBtnClick()
                },
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
            modifier = Modifier
                .padding(end = 20.dp)
                .clip(CircleShape)
                .clickable {
                    onSearchResultClicked()
                },
            tint = White
        )
    }
}

@Composable
private fun SearchRegionResultListView(
    modifier: Modifier,
    inputText: String,
    autoCompleteTextList: List<String>,
    onAutoCompleteTextClicked: (String) -> Unit
) {
    LazyColumn(modifier = modifier
        .fillMaxWidth()
        .padding(start = 20.dp, top = 20.dp)
    ) {
        items(
            count = autoCompleteTextList.size,
            key = { index -> autoCompleteTextList[index] },
        ) {
            val inputTextTrimEnd = inputText.trimEnd()
            val autoCompleteText = autoCompleteTextList[it]
            val startIndex = autoCompleteText.indexOf(inputTextTrimEnd, ignoreCase = true)
            val endIndex = startIndex + inputTextTrimEnd.length

            SearchRegionResultItem(
                autoCompleteText = autoCompleteText,
                startIndex = startIndex,
                endIndex = endIndex,
            ) { completeText ->
                onAutoCompleteTextClicked(completeText)
            }
        }
    }
}

@Composable
private fun SearchRegionResultItem(
    autoCompleteText: String,
    startIndex: Int,
    endIndex: Int,
    onAutoCompleteTextClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onAutoCompleteTextClicked(autoCompleteText)
            }
    ) {
        if (startIndex > 0) {
            GuideText(
                color = Black,
                text = autoCompleteText.substring(0, startIndex),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        GuideText(
            color = Purple,
            text = autoCompleteText.substring(startIndex, endIndex),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Center
        )

        if (endIndex < autoCompleteText.length) {
            GuideText(
                color = Black,
                text = autoCompleteText.substring(endIndex),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}

@Preview
@Composable
private fun SearchRegionScreenPreview() {
    SearchRegionContent(
        modifier = Modifier,
        snackBarHostState = SnackbarHostState(),
        inputText = "송파구",
        autoCompleteTextList = listOf("서울특별시 송파구 문정동", "서울특별시 송파구 가락동"),
        onInputTextChanged = {},
        onAutoCompleteTextClicked = {},
        onSearchResultClicked = {},
        onBackBtnClick = {}
    )
}

