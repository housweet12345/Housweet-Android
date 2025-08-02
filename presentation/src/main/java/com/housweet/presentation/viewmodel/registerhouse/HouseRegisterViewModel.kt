package com.housweet.presentation.viewmodel.registerhouse

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.local.RoomLocalDataSource
import com.housweet.domain.model.HouseRegisterModel
import com.housweet.domain.repository.HouseRegisterRepository
import com.housweet.domain.repository.ImageUploadRepository
import com.housweet.presentation.model.Region
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class HouseRegisterViewModel @Inject constructor(
    private val houseRegisterRepository: HouseRegisterRepository,
    private val roomLocalDataSource: RoomLocalDataSource,
    private val imageUploadRepository: ImageUploadRepository
) : ViewModel() {

    // Room ID 로깅용 함수
    fun logRoomId() {
        viewModelScope.launch {
            val roomId = roomLocalDataSource.getRoomId()
            Log.d("HouseRegister", "Room ID: $roomId")
        }
    }

    // Step 1: 교통/인프라 등 태그
    var houseTags by mutableStateOf<List<String>>(emptyList())
        private set
    fun updateHouseTags(tags: List<String>) {
        houseTags = tags
    }

    // Step 2: 주소, 제목, 설명, 금액 등
    var region: Region? by mutableStateOf(null)
        private set
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var deposit by mutableStateOf("")
    var monthlyRent by mutableStateOf("")
    var managementFee by mutableStateOf("")
    var moveInDate by mutableStateOf("")

    fun setStep2Data(
        region: Region?,
        title: String,
        desc: String,
        deposit: String,
        rent: String,
        fee: String,
        moveIn: String
    ) {
        this.region = region
        this.title = title
        this.description = desc
        this.deposit = deposit
        this.monthlyRent = rent
        this.managementFee = fee
        this.moveInDate = moveIn

        Log.d("HouseRegister", "✅ setStep2Data 호출됨")
        Log.d("HouseRegister", "📌 region: $region")
        Log.d("HouseRegister", "📌 title: $title")
        Log.d("HouseRegister", "📌 description: $description")
        Log.d("HouseRegister", "📌 deposit: $deposit")
        Log.d("HouseRegister", "📌 monthlyRent: $monthlyRent")
        Log.d("HouseRegister", "📌 managementFee: $managementFee")
        Log.d("HouseRegister", "📌 moveInDate: $moveInDate")
    }

    // Step 3: Bitmap
    private val _imageBitmap = mutableStateOf<Bitmap?>(null)

    fun updateImageBitmap(bitmap: Bitmap) {
        _imageBitmap.value = bitmap
    }

    // Step 3: Bitmap → File 변환 (data URI 형식 포함)
    private suspend fun convertBitmapToFile(bitmap: Bitmap): File = withContext(Dispatchers.IO) {
        val file = File.createTempFile("house_image", ".jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        file
    }

    // Step 4: 선호 태그
    var preferredTags by mutableStateOf<List<String>>(emptyList())
        private set
    fun updatePreferredTags(tags: List<String>) {
        preferredTags = tags
    }

    // 등록 요청
    fun submitHouseRegister(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val roomId = roomLocalDataSource.getRoomId()
                    ?: throw Exception("Room ID not found")

                val bitmap = _imageBitmap.value
                    ?: throw Exception("이미지 없음: bitmap이 null입니다.")

                val imageFile = convertBitmapToFile(bitmap)
                val uploadedImageUrl = imageUploadRepository.uploadImage(imageFile)


                val model = HouseRegisterModel(
                    room = roomId,
                    si = region?.sidoCode ?: throw Exception("시 정보 누락"),
                    gu = region?.sigunguCode ?: throw Exception("구 정보 누락"),
                    dong = region?.dongCode ?: throw Exception("동 정보 누락"),
                    title = title.ifBlank { throw Exception("제목 누락") },
                    content = description.ifBlank { throw Exception("설명 누락") },
                    imageUri = uploadedImageUrl,
                    trafficTags = houseTags, // 용도에 따라 분리 필요하면 분기
                    sizeOfHouseTags = emptyList(),
                    infraTags = emptyList(),
                    lifePatternTags = emptyList(),
                    tidyingUpHabitTags = emptyList(),
                    personalityTags = preferredTags,
                    rent = monthlyRent.toIntOrNull() ?: 0,
                    deposit = deposit.toIntOrNull() ?: 0,
                    managementFee = managementFee.toIntOrNull() ?: 0,
                    availableFrom = moveInDate
                )
                Log.d("HouseRegister", "🚀 submit 직전 title: $title")
                Log.d("HouseRegister", "🚀 submit 직전 description: $description")
                Log.d("HouseRegister", "🚀 submit 직전 region: $region")


                Log.d("HouseRegister", "보내는 데이터: " + Json.encodeToString(HouseRegisterModel.serializer(), model))


                houseRegisterRepository.registerHouse(model)
                Log.d("HouseRegister", "하우스 등록 성공: $model")
                onSuccess()
            } catch (e: Exception) {
                Log.e("HouseRegister", "등록 실패", e)
                onError(e)
            }
        }
    }
}
