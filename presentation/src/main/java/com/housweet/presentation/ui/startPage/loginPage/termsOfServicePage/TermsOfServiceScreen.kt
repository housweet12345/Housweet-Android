package com.housweet.presentation.ui.startPage.loginPage.termsOfServicePage

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.housweet.presentation.R
import com.housweet.presentation.ui.startPage.BottomButton
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.startPage.LoadingScreen
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_7D7D7D
import com.housweet.presentation.ui.theme.Gray_7E7E7E
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.Gray_D9D9D9
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TermsOfServiceScreen(
    modifier: Modifier,
    termsOfServiceViewModel: TermsOfServiceViewModel = hiltViewModel(),
    onNextScreen: () -> Unit,
    onDetailTermsConditionsPoliciesClick: () -> Unit,
    onDetailTermsPrivacyPoliciesClick: () -> Unit,
    onDetailTermsLocationInformationPoliesClick: () -> Unit
) {
    val uiState: TermsOfServiceState by termsOfServiceViewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var checkAll by remember { mutableStateOf(false) }
    var checkTerm1 by remember { mutableStateOf(false) }
    var checkTerm2 by remember { mutableStateOf(false) }
    var checkTerm3 by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        termsOfServiceViewModel.event.collect { event ->
            when (event) {
                TermsOfServiceEvent.Success -> {
                    onNextScreen()
                }

                TermsOfServiceEvent.Error -> {
                    snackBarHostState.showSnackbar(
                        message = "오류가 발생했습니다.",
                        actionLabel = "닫기",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    when(uiState) {
        TermsOfServiceState.Idle -> {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) {
                TermsOfServiceContent(
                    checkAll = checkAll,
                    checkTerm1 = checkTerm1,
                    checkTerm2 = checkTerm2,
                    checkTerm3 = checkTerm3,
                    onPermitBtnClick = {
                        termsOfServiceViewModel.agreeTerms()
                    },
                    onAllCheckedChange = {
                        checkAll = !checkAll
                        checkTerm1 = checkAll
                        checkTerm2 = checkAll
                        checkTerm3 = checkAll
                    },
                    onTerm1CheckedChange = {
                        checkTerm1 = !checkTerm1
                        checkAll = checkTerm1 && checkTerm2 && checkTerm3
                    },
                    onTerm2CheckedChange = {
                        checkTerm2 = !checkTerm2
                        checkAll = checkTerm1 && checkTerm2 && checkTerm3
                    },
                    onTerm3CheckedChange = {
                        checkTerm3 = !checkTerm3
                        checkAll = checkTerm1 && checkTerm2 && checkTerm3
                    },
                    onDetailTermsConditionsPoliciesClick = onDetailTermsConditionsPoliciesClick,
                    onDetailTermsPrivacyPoliciesClick = onDetailTermsPrivacyPoliciesClick,
                    onDetailTermsLocationInformationPoliesClick = onDetailTermsLocationInformationPoliesClick
                )
            }
        }

        TermsOfServiceState.IsLoading -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun TermsOfServiceContent(
    checkAll: Boolean,
    checkTerm1: Boolean,
    checkTerm2: Boolean,
    checkTerm3: Boolean,
    onPermitBtnClick:() -> Unit,
    onAllCheckedChange: () -> Unit,
    onTerm1CheckedChange: () -> Unit,
    onTerm2CheckedChange: () -> Unit,
    onTerm3CheckedChange: () -> Unit,
    onDetailTermsConditionsPoliciesClick: () -> Unit,
    onDetailTermsPrivacyPoliciesClick: () -> Unit,
    onDetailTermsLocationInformationPoliesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Spacer(modifier = Modifier.height(height = 117.dp))

        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = Purple,
            text = "약관 동의가 필요해요.",
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            lineHeight = 30.sp,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(height = 55.dp))

        TermsOfServiceAllAgree(
            checked = checkAll,
        ) {
            onAllCheckedChange()
        }

        Spacer(modifier = Modifier.height(height = 5.dp))

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            thickness = 0.5.dp,
            color = Color(0xFFCBCBCB)
        )

        Spacer(modifier = Modifier.height(height = 13.dp))

        TermsOfServiceMenu(
            termName = "개인정보처리방침",
            checked = checkTerm1,
            onCheckedChange = onTerm1CheckedChange,
            onDetailClick = onDetailTermsPrivacyPoliciesClick
        )

        TermsOfServiceMenu(
            termName = "서비스이용약관",
            checked = checkTerm2,
            onCheckedChange = onTerm2CheckedChange,
            onDetailClick = onDetailTermsConditionsPoliciesClick
        )

        TermsOfServiceMenu(
            termName = "위치정보이용약관",
            checked = checkTerm3,
            onCheckedChange = onTerm3CheckedChange,
            onDetailClick = onDetailTermsLocationInformationPoliesClick
        )

        Spacer(modifier = Modifier.weight(weight = 1f))

        BottomButton(text = "시작하기") {
            if (checkTerm1 && checkTerm2 && checkTerm3) {
                onPermitBtnClick()
            }
        }
    }
}

@Composable
private fun TermsOfServiceAllAgree(checked: Boolean, onCheckedChange: () -> Unit) {
    Row(
        modifier = Modifier.padding(start = 20.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = if (checked) R.drawable.check else R.drawable.uncheck),
            contentDescription = "check",
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    onCheckedChange()
                }
        )

        GuideText(
            modifier = Modifier.padding(start = 7.dp),
            color = Black,
            text = "전체 동의하기",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun TermsOfServiceMenu(
    termName: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    onDetailClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(start = 20.dp, end = 10.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = if (checked) R.drawable.check else R.drawable.uncheck),
            contentDescription = "check",
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    onCheckedChange()
                }
        )

        GuideText(
            modifier = Modifier
                .padding(start = 7.dp)
                .drawBehind {
                    val strokeWidthPx = 0.5.dp.toPx()
                    val verticalOffset = size.height
                    drawLine(
                        color = Black,
                        strokeWidth = strokeWidthPx,
                        start = Offset(0f, verticalOffset),
                        end = Offset(size.width, verticalOffset)
                    )
                }
                .clickable { onDetailClick() },
            color = Black,
            text = termName,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
@Preview
private fun TermsOfServicePreviewScreen() {
    TermsOfServiceContent(
        checkAll = false,
        checkTerm1 = false,
        checkTerm2 = false,
        checkTerm3 = false,
        onPermitBtnClick = {},
        onAllCheckedChange = {},
        onTerm1CheckedChange = {},
        onTerm2CheckedChange = {},
        onTerm3CheckedChange = {},
        onDetailTermsConditionsPoliciesClick = {},
        onDetailTermsPrivacyPoliciesClick = {},
        onDetailTermsLocationInformationPoliesClick = {}
    )
}