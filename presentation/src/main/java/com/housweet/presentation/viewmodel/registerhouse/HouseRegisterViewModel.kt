package com.housweet.presentation.viewmodel.registerhouse

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.*
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
) : HouseRegisterViewModelBase() {

    // Room ID 로깅용 함수
    override fun logRoomId() {
        viewModelScope.launch {
            val roomId = roomLocalDataSource.getRoomId()
            Log.d("HouseRegister", "Room ID: $roomId")
        }
    }

    // Step 1: 교통/인프라 등 태그
    override var houseTags by mutableStateOf<List<String>>(emptyList())
        set
    override fun updateHouseTags(tags: List<String>) {
        houseTags = tags
    }

    // Step 2: 주소, 제목, 설명, 금액 등
    override var region: Region? by mutableStateOf(null)
        set
    override var title by mutableStateOf("")
    override var description by mutableStateOf("")
    override var deposit by mutableStateOf("")
    override var monthlyRent by mutableStateOf("")
    override var managementFee by mutableStateOf("")
    override var moveInDate by mutableStateOf("")

    override fun setStep2Data(
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

    // Step 3: Bitmap → File 변환 (data URI 형식 포함)
    private suspend fun convertBitmapToFile(bitmap: Bitmap): File = withContext(Dispatchers.IO) {
        val file = File.createTempFile("house_image", ".jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        file
    }

    // Step 4: 선호 태그
    override var preferredTags by mutableStateOf<List<String>>(emptyList())
        set
    override fun updatePreferredTags(tags: List<String>) {
        preferredTags = tags
    }

    // 등록 요청
    override fun submitHouseRegister(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val roomId = roomLocalDataSource.getRoomId()
                    ?: throw Exception("Room ID not found")

                val bitmaps: List<Bitmap> = imageBitmaps
                if (bitmaps.isEmpty()) throw Exception("이미지 없음: 최소 한 장을 선택해주세요.")

                //백엔드에서 1장만 받을 때, 첫 장만 업로드
                val first = bitmaps.first()
                val imageFile = convertBitmapToFile(first)
                val uploadedImageUrl = imageUploadRepository.uploadImage(imageFile)

                // 백엔드에서 여러 장 모두 업로드 가능하다면 아래 주석 해제. (루프 돌리기)
                // val urls = bitmaps.map { convertBitmapToFile(it) }
                //     .map { imageUploadRepository.uploadImage(it) }

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
