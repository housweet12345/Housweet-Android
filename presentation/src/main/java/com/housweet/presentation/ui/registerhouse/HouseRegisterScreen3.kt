package com.housweet.presentation.ui.registerhouse

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.housweet.presentation.R
import com.housweet.presentation.model.RegisterModel
import com.housweet.presentation.ui.common.StepIndicator
import com.housweet.presentation.ui.common.TopBarWithBackButton
import com.housweet.presentation.viewmodel.registerhouse.HouseRegisterViewModelBase
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.launch

@Composable
fun HouseRegisterScreen3(
    mode: RegisterModel,
    postingId: Int?,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: HouseRegisterViewModelBase
) {
    BackHandler {
        onBackClick()
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val selectedBitmap = viewModel.imageBitmap

    // 다이얼로그 상태
    var showDialog by remember { mutableStateOf(false) }
    // (필요하면 메시지 커스터마이즈할 수 있도록)
    val missingMessage = "사진을 선택해주세요."

    BackHandler {
        if (selectedBitmap != null) viewModel.clearImages()
        onBackClick()
    }

    //단일 이미지 선택 런처
    val singlePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult

        // 비트맵 디코드
        val decoded = runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val src = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(src)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        }.getOrNull()

        // 기존 선택 초기화 후 설정
        viewModel.clearImages()
        decoded?.let { viewModel.setImageBitmap(it) }

        // 서버 업로드 → URL 저장(단일)
        scope.launch {
            val url: String? = viewModel.uploadUris(context, listOf(uri))
            if (url != null) viewModel.setImageUrl(url)
        }
    }

    Scaffold (
        topBar = {
            TopBarWithBackButton(
                title = if (mode == RegisterModel.EDIT) "글 수정하기" else "하우스 올리기",
                currentStep = 3,
                onBackClick = {
                    if (selectedBitmap != null) viewModel.clearImages()
                    onBackClick()
                },
            )
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "실제 사진을 첨부해주세요.",
                    color = Color(0xFF6C4DFF),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "사진 첨부",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFFCBCDD2), shape = RoundedCornerShape(12.dp))
                    .clickable{
                        singlePicker.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedBitmap == null) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera_icon_gray),
                        contentDescription = "사진 선택",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                } else {
                    Image(
                        bitmap = selectedBitmap.asImageBitmap(),
                        contentDescription = "선택 이미지",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val hasSelectedImage = viewModel.imageBitmap != null

                    if (!hasSelectedImage) {
                        showDialog = true
                        return@Button
                    }

                    onNextClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF665ED3),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "다음",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                tonalElevation = 2.dp,
                border = BorderStroke(1.dp, Color(0xFF665ED3))
            ) {
                Column(
                    modifier = Modifier.padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = missingMessage,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    TextButton(onClick = { showDialog = false }) { Text("확인") }
                }
            }
        }
    }
}

// region Preview

@Preview(showBackground = true)
@Composable
fun HouseRegisterScreen3Preview() {
    val context = LocalContext.current

    // ✅ 1x1 흰색 비트맵 (간단한 프리뷰용)
    val dummyBitmap = remember {
        createBitmap(1, 1).apply {
            eraseColor(android.graphics.Color.LTGRAY) // 회색
        }
    }

    val fakeViewModel = remember { PreviewHouseRegisterViewModel3() }

    HouseRegisterScreen3(
        mode = RegisterModel.CREATE,
        postingId = 1,
        onNextClick = {},
        onBackClick = {},
        viewModel = fakeViewModel
    )
}

class PreviewHouseRegisterViewModel3 : HouseRegisterViewModelBase()