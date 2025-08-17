package com.housweet.presentation.viewmodel.registerhouse

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.housweet.presentation.model.Region

@Stable
open class HouseRegisterViewModelBase : ViewModel() {
    open var currentPostingId by mutableStateOf<Int?>(null)

    companion object {
        //추후 기능 고도화 시, 10장으로 변경
//        const val MAX_IMAGES = 10
        const val MAX_IMAGES = 1
    }

    // Step 1
    open var houseTags by mutableStateOf<List<String>>(emptyList())
        protected set

    open fun updateHouseTags(tags: List<String>) {
        houseTags = tags
    }

    open fun logRoomId() {}

    // Step 2
    open var region: Region? by mutableStateOf(null)
    open var title by mutableStateOf("")
    open var description by mutableStateOf("")
    open var deposit by mutableStateOf("")
    open var monthlyRent by mutableStateOf("")
    open var managementFee by mutableStateOf("")
    open var moveInDate by mutableStateOf("")

    open fun setStep2Data(
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
    }

    // Step 3 - 이미지 비트맵 처리
    private var _imageBitmap by mutableStateOf<Bitmap?>(null)
    val imageBitmap: Bitmap? get() = _imageBitmap

    private var _imageUrl by mutableStateOf<String?>(null)
    val imageUrl: String? get() = _imageUrl

    open fun setImageBitmap(bitmap: Bitmap?) {_imageBitmap = bitmap}
    open fun setImageUrl(url: String?) {_imageUrl = url}

    open fun clearImages() {
        _imageBitmap = null
        _imageUrl = null
    }

    // 업로드 훅: 구현체(VM)에서 실제 업로드 로직을 오버라이드
    open suspend fun uploadUris(context: Context, uris: List<Uri>): String? = null

    // Step 4 (예정용)
    open var preferredTags by mutableStateOf<List<String>>(emptyList())
        protected set

    open fun updatePreferredTags(tags: List<String>) {
        preferredTags = tags
    }
    open fun submitHouseRegister(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        // 기본 구현은 아무 것도 안 함 (실제 ViewModel에서는 override)
    }

    open suspend fun loadForEdit(postingId: Int) { /* base는 no-op */ }
    open fun submitEdit(onSuccess: () -> Unit, onError: (Throwable) -> Unit) { /* no-op */ }
}