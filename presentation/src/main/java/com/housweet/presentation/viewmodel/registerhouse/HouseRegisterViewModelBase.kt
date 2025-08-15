package com.housweet.presentation.viewmodel.registerhouse

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.housweet.presentation.model.Region

@Stable
open class HouseRegisterViewModelBase : ViewModel() {
    companion object {
        const val MAX_IMAGES = 10
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
    private val _imageBitmaps = mutableStateListOf<Bitmap>()
    val imageBitmaps: List<Bitmap> get() = _imageBitmaps

    open fun addImages(bitmaps: List<Bitmap>) {
        if (bitmaps.isEmpty()) return
        val roomLeft = MAX_IMAGES - _imageBitmaps.size
        if (roomLeft <= 0) return
        _imageBitmaps.addAll(bitmaps.take(roomLeft))
    }

    open fun clearImages() = _imageBitmaps.clear()



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

}