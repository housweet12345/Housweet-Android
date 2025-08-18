package com.housweet.presentation.ui.communityPage.searchRegionScreen

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.housweet.domain.model.Coordinate
import com.housweet.presentation.R
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.nanumSquareFontFamily

@Composable
fun SearchRegionScreen(
    searchRegionViewModel: SearchRegionViewModel = hiltViewModel(),
    onMapScreen: (Coordinate) -> Unit,
    onBackBtnClick: () -> Unit
) {
    val uiState: SearchRegionUiState by searchRegionViewModel.uiState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val snackBarHostState = remember { SnackbarHostState() }
    var inputTextValue by remember { mutableStateOf(TextFieldValue(text = "", selection = TextRange.Zero)) }
    var autoCompleteTextList by remember { mutableStateOf(listOf<String>()) }

    BackHandler {
        onBackBtnClick()
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            searchRegionViewModel.event.collect { event ->
                when (event) {
                    SearchRegionEvent.Error -> {
                        snackBarHostState.showSnackbar(
                            message = "지역명을 정확히 입력해주세요.",
                            actionLabel = "닫기",
                            duration = SnackbarDuration.Short
                        )
                    }

                    is SearchRegionEvent.AutoCompleteSuccess -> {
                        autoCompleteTextList = event.addressList
                    }

                    is SearchRegionEvent.GeoCodingSuccess -> {
                        onMapScreen(Coordinate(event.latLng.longitude, event.latLng.latitude))
                    }

                    is SearchRegionEvent.IsValidAddress -> {
                        if (event.isValid) {
                            searchRegionViewModel.geoCoding(inputTextValue.text.trim())
                        } else {
                            searchRegionViewModel.error()
                        }
                    }
                }
            }
        }
    }

    when (uiState) {
        SearchRegionUiState.Idle -> {
            SearchRegionContent(
                snackBarHostState = snackBarHostState,
                inputTextValue = inputTextValue,
                autoCompleteTextList = autoCompleteTextList,
                onInputTextChanged = {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    inputTextValue = it
                    searchRegionViewModel.getFullAddress(it.text.replace(Regex(" $"), ""))
                },
                onAutoCompleteTextClicked = {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    inputTextValue = it
                    searchRegionViewModel.getFullAddress(it.text.replace(Regex(" $"), ""))
                },
                onSearchResultClicked = {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    searchRegionViewModel.isValidFullAddress(inputTextValue.text.trim())
                },
                onBackBtnClick = onBackBtnClick
            )
        }
    }
}

@Composable
private fun SearchRegionContent(
    snackBarHostState: SnackbarHostState,
    inputTextValue: TextFieldValue,
    autoCompleteTextList: List<String>,
    onInputTextChanged: (TextFieldValue) -> Unit,
    onAutoCompleteTextClicked: (TextFieldValue) -> Unit,
    onSearchResultClicked: () -> Unit,
    onBackBtnClick: () -> Unit
) {
    Scaffold(
        topBar = {
            SearchRegionTopBar(
                inputTextValue = inputTextValue,
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
            inputText = inputTextValue.text,
            autoCompleteTextList = autoCompleteTextList
        ) { completeText ->
            onAutoCompleteTextClicked(completeText)
        }
    }
}

@Composable
private fun SearchRegionTopBar(
    inputTextValue: TextFieldValue,
    onInputTextChanged: (TextFieldValue) -> Unit,
    onSearchResultClicked: () -> Unit,
    onBackBtnClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(Purple)
            .padding(vertical = 7.dp)
            .fillMaxWidth(),
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
            value = inputTextValue,
            onValueChange = {
                onInputTextChanged(it)
            },
            modifier = Modifier.padding(start = 10.dp),
            textStyle = TextStyle(
                color = White,
                fontFamily = nanumSquareFontFamily,
                fontWeight = FontWeight.Bold,
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
                if (inputTextValue.text.isEmpty()) {
                    GuideText(
                        color = Gray_CBCBCB,
                        text = "지역명을 입력해주세요.",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        textAlign = TextAlign.Start
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
    onAutoCompleteTextClicked: (TextFieldValue) -> Unit
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
    onAutoCompleteTextClicked: (TextFieldValue) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onAutoCompleteTextClicked(TextFieldValue(text = autoCompleteText, selection = TextRange(autoCompleteText.length)))
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
    var textValue = TextFieldValue(text = "서울특별시 송파구")
    SearchRegionContent(
        snackBarHostState = SnackbarHostState(),
        inputTextValue = textValue,
        autoCompleteTextList = listOf("서울특별시 송파구 문정동", "서울특별시 송파구 가락동"),
        onInputTextChanged = {
            textValue = it
        },
        onAutoCompleteTextClicked = {},
        onSearchResultClicked = {},
        onBackBtnClick = {}
    )
}

