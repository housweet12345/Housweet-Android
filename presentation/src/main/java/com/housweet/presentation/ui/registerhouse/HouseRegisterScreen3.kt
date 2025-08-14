package com.housweet.presentation.ui.registerhouse

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.R
import com.housweet.presentation.model.RegisterModel
import com.housweet.presentation.ui.common.StepIndicator
import com.housweet.presentation.ui.common.TopBarWithBackButton
import com.housweet.presentation.viewmodel.registerhouse.HouseRegisterViewModelBase
import androidx.core.graphics.createBitmap
import kotlin.collections.mapNotNull

@Composable
fun HouseRegisterScreen3(
    mode: RegisterModel,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: HouseRegisterViewModelBase
) {

    val context = LocalContext.current
    val images = viewModel.imageBitmaps
    val max = HouseRegisterViewModelBase.MAX_IMAGES

    BackHandler {
        if (images.isNotEmpty()) viewModel.clearImages()
        onBackClick()
    }


    // 멀티 이미지 선택 런처
    val multiPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        val bitmaps = uris.mapNotNull { uri ->
            runCatching {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val src = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(src)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
            }.getOrNull()
        }
        viewModel.addImages(bitmaps)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        TopBarWithBackButton(
            title = if (mode == RegisterModel.EDIT) "글 수정하기" else "하우스 올리기",
            onBackClick = {
                if (images.isNotEmpty()) viewModel.clearImages()
                onBackClick()
            }
        )

        StepIndicator(currentStep = 3)

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
                modifier = Modifier.padding(vertical = 8.dp)
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
                .clickable(enabled = images.size < max) {
                    multiPicker.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.camera_icon_gray),
                contentDescription = "사진 선택",
                tint = Color.Gray,
                modifier = Modifier.size(48.dp)
            )
        }

        // ▼▼ 박스 하단 썸네일 영역 ▼▼
        if (images.isNotEmpty() || images.size < max) {
            Spacer(Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(images) { index, bmp ->
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = "선택 이미지 $index",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNextClick,
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
        onNextClick = {},
        onBackClick = {},
        viewModel = fakeViewModel
    )
}

class PreviewHouseRegisterViewModel3 : HouseRegisterViewModelBase()