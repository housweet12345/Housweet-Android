package com.housweet.presentation.ui.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentaion.R
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

@Composable
private fun SelectionButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp) // 원하는 정확한 높이 지정
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) ColorGroup.Primary else ColorGroup.White_F8F8F8)
            .border(
                width = 0.2.dp,
                color = ColorGroup.Gray_CBCBCB,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else ColorGroup.Gray_7E7E7E,
            fontSize = 12.sp,
            maxLines = 1 // 한 줄로 제한
        )
    }
}

@Composable
fun SimpleSegmentedControl(
    option1: String,
    option2: String,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // 전체 컨테이너
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(ColorGroup.White_F8F8F8)
            .border(
                width = 0.2.dp,
                color = ColorGroup.Gray_CBCBCB,
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        // 옵션 레이아웃
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // 첫 번째 옵션
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (selectedOption == option1) ColorGroup.Primary else ColorGroup.White_F8F8F8
                    )
                    .clickable { onOptionSelected(option1) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option1,
                    color = if (selectedOption == option1) Color.White else ColorGroup.Gray_7E7E7E,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }

            // 두 번째 옵션
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (selectedOption == option2) ColorGroup.Primary else ColorGroup.White_F8F8F8
                    )
                    .clickable { onOptionSelected(option2) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option2,
                    color = if (selectedOption == option2) Color.White else ColorGroup.Gray_7E7E7E,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }
    }
}


@Composable
fun ToggleButtonGroup(
    option1: String,
    option2: String,
    selectedOption: Int,
    modifier: Modifier = Modifier,
    onOptionSelected: (Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SelectionButton(
            text = option1,
            isSelected = selectedOption == 1,
            modifier = Modifier.weight(1f),
            onClick = { onOptionSelected(1) }
        )

        SelectionButton(
            text = option2,
            isSelected = selectedOption == 2,
            modifier = Modifier.weight(1f),
            onClick = { onOptionSelected(2) }
        )
    }
}

@Composable
fun ProfileEditNameTextField(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.CenterStart,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(6.dp),
            )
            .clip(RoundedCornerShape(6.dp))
            .border(
                width = 0.2.dp,
                color = ColorGroup.Gray_CBCBCB,
                shape = RoundedCornerShape(6.dp)
            )
            .background(Color.White),
        textStyle = TextStyle(
            fontSize = 14.sp,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.padding(horizontal = 8.dp),
                contentAlignment = contentAlignment
            ) {
                Column {
                    Spacer(Modifier.height(8.dp))
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                        innerTextField()
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    )
}

@Composable
fun InfoMessage(
    modifier: Modifier = Modifier,
    message: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 정보 아이콘 (원형 i 아이콘)
        Icon(
            painter = painterResource(id = R.drawable.ic_caution),
            contentDescription = "caution",
            tint = ColorGroup.Primary,
            modifier = Modifier.size(12.dp)
        )

        // 아이콘과 텍스트 사이 간격
        Spacer(modifier = Modifier.width(4.dp))

        // 메시지 텍스트
        Text(
            text = message,
            color = ColorGroup.Primary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun BottomButton(
    text: String = "다음",
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), // 버튼 높이 설정
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorGroup.Primary
        ),
        shape = RectangleShape
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileEditCaseNumberPreview() {
    ProfileEditCaseNumber(isCurrent = true, number = 1, description = "프로필 입력")
}

@Preview(showBackground = true)
@Composable
private fun ProfileEditCaseIsnotCurrentNumberPreview() {
    ProfileEditCaseNumber(isCurrent = false, number = 2, description = "키워드 선택")
}

@Preview
@Composable
private fun ProfileEditNameTextFieldPreview() {
    ProfileEditNameTextField(
        hint = "이름을 입력해주세요",
        value = "",
        onValueChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun InfoMessagePreview() {
    InfoMessage(message = "나이와 성별은 한 번 선택 후 변경이 불가능합니다.\n신중하게 선택해 주세요.")
}

@Preview(showBackground = true)
@Composable
fun BottomButtonPreview() {
    BottomButton()
}

@Preview(showBackground = true)
@Composable
fun ToggleButtonGroupPreview() {
    var selectedOption by remember { mutableIntStateOf(1) }

    ToggleButtonGroup(
        option1 = "남자",
        option2 = "여자",
        selectedOption = selectedOption,
        onOptionSelected = { selectedOption = it }
    )
}


// 프리뷰
@Composable
@Preview(showBackground = true)
fun SimpleSegmentedControlPreview() {
    var selected by remember { mutableStateOf("I") }

    SimpleSegmentedControl(
        option1 = "E",
        option2 = "I",
        selectedOption = selected,
        onOptionSelected = { selected = it },
    )
}