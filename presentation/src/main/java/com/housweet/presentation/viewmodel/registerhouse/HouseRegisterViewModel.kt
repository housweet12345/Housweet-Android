package com.housweet.presentation.viewmodel.registerhouse

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
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
import javax.inject.Inject

@HiltViewModel
class HouseRegisterViewModel @Inject constructor(
    private val houseRegisterRepository: HouseRegisterRepository,
    private val roomLocalDataSource: RoomLocalDataSource,
    private val imageUploadRepository: ImageUploadRepository
) : HouseRegisterViewModelBase() {

    override var currentPostingId by mutableStateOf<Int?>(null)

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

    // Step 3
    override suspend fun uploadUris(context: Context, uris: List<Uri>): String? {
        if (uris.isEmpty()) return null
        return withContext(Dispatchers.IO) {
            try {
                val uri = uris.first()
                val mime: String = context.contentResolver.getType(uri) ?: "application/octet-stream"
                val ext: String = when {
                    mime.endsWith("jpeg") || mime.endsWith("jpg") -> ".jpg"
                    mime.endsWith("png") -> ".png"
                    else -> ".jpg"
                }
                val tmp: File = File.createTempFile("house_image_", ext)
                context.contentResolver.openInputStream(uri).use { input ->
                    tmp.outputStream().use { output -> input?.copyTo(output) }
                }
                imageUploadRepository.uploadImage(tmp)
            } catch (e: Exception) {
                Log.e("ImageUpload", "upload failed: ${uris.first()}", e)
                null
            }
        }
    }

    //JPEG 저장이 필요할 때만 사용
    private suspend fun convertBitmapToFile(bitmap: Bitmap, quality: Int = 95): File =
        withContext(Dispatchers.IO) {
            val file = File.createTempFile("house_image", ".jpg")
            file.outputStream().use { out -> bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out) }
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

                val bitmap: Bitmap = imageBitmap ?: throw Exception("이미지 없음: 최소 한 장을 선택해주세요.")

                // 업로드
                val imageFile = convertBitmapToFile(bitmap)
                val uploadedImageUrl = imageUploadRepository.uploadImage(imageFile)
                setImageUrl(uploadedImageUrl)

                val url = imageUrl ?: throw Exception("이미지 URL이 없습니다. 이미지를 첨부해 주세요.")

                val model = HouseRegisterModel(
                    room = roomId,
                    si = region?.sidoCode ?: throw Exception("시 정보 누락"),
                    gu = region?.sigunguCode ?: throw Exception("구 정보 누락"),
                    dong = region?.dongCode ?: throw Exception("동 정보 누락"),
                    title = title.ifBlank { throw Exception("제목 누락") },
                    content = description.ifBlank { throw Exception("설명 누락") },
                    imageUri = url,
//                    images = urls,
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

    // 편집용 상세 불러오기
    override suspend fun loadForEdit(postingId: Int) {
        currentPostingId = postingId
        val detail = houseRegisterRepository.getPostingDetail(postingId) // 아래 3) 참고

        // 서버 응답을 ViewModel 상태에 프리필
        // (응답 예시를 기반으로 매핑)
        region = Region(
            sidoCode = detail.si,
            sigunguCode = detail.gu,
            dongCode = detail.dong,
            sido = "",
            sigungu = "",
            dong = "",
            // 나머지 Region 필드는 내부 정의에 맞게 보정
        )
        title = detail.title.orEmpty()
        description = detail.content.orEmpty()
        deposit = detail.deposit?.toString().orEmpty()
        monthlyRent = detail.rent?.toString().orEmpty()
        managementFee = detail.managementFee?.toString().orEmpty()
        moveInDate = detail.availableFrom.orEmpty()

        // 태그: 서버 스키마에 맞춰 합치거나 분리
        houseTags = (detail.trafficTags ?: emptyList()) +
                (detail.infraTags ?: emptyList())
        preferredTags = (detail.personalityTags ?: emptyList())

        //단일 필드만 있으면 리스트로 변환해서 보여주기(서버가 배열 지원 전까지 임시)
        val existing = detail.imageUri
//        val existing = detail.images
        if (!existing.isNullOrBlank()) {
            clearImages()
            setImageUrl(existing)
        }
    }

    // 편집 PATCH
    override fun submitEdit(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                val postingId = currentPostingId ?: throw IllegalStateException("postingId 없음")

                val finalUrl: String = if (imageBitmap != null) {
                    val file = convertBitmapToFile(imageBitmap!!)
                    imageUploadRepository.uploadImage(file)
                } else {
                    imageUrl ?: houseRegisterRepository.getPostingDetail(postingId).imageUri
                    ?: throw Exception("편집에 사용할 이미지 URL이 없습니다.")
                }

                val model = buildModel(finalUrl)
                houseRegisterRepository.updateHouse(postingId, model)

                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    private fun buildModel(url: String) = HouseRegisterModel(
        room = currentPostingId ?: 0, // 서버 스펙에 맞게 조정 필요
        si = region?.sidoCode ?: 0,
        gu = region?.sigunguCode ?: 0,
        dong = region?.dongCode ?: 0,
        title = title,
        content = description,
        imageUri = url,     // ✅ 배열
//        images = urls,

        trafficTags = houseTags,
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
}
