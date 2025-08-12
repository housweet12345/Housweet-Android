//package com.housweet.data.network.dto
//
//import com.housweet.domain.model.AppSettingCategory
//import com.housweet.domain.model.AppSettingItem
//
//fun AppSettingResponseDto.toDomain(): List<AppSettingCategory> {
//    return categories.map { category ->
//        AppSettingCategory(
//            categoryId = category.categoryId,
//            categoryName = category.categoryName,
//            settings = category.settings.map {
//                AppSettingItem(
//                    id = it.id,
//                    templateId = it.templateId,
//                    key = it.key,
//                    displayName = it.displayName,
//                    isEnabled = it.isEnabled
//                )
//            }
//        )
//    }
//}
//
//fun AppSettingItemDto.toDomain(): AppSettingItem = AppSettingItem(
//    id = id,
//    templateId = templateId,
//    key = key,
//    displayName = displayName,
//    isEnabled = isEnabled
//)

package com.housweet.data.network.dto

import com.housweet.domain.model.AppSettingCategory
import com.housweet.domain.model.AppSettingItem

// AppSettingResponseDto → Domain 변환
fun AppSettingResponseDto.toDomain(): List<AppSettingCategory> {
    return categories.map { category ->
        AppSettingCategory(
            categoryId = category.categoryId,
            categoryName = category.categoryName,
            settings = category.settings.map { it.toDomain() }
        )
    }
}

// AppSettingItemDto → Domain 변환
fun AppSettingItemDto.toDomain(): AppSettingItem = AppSettingItem(
    id = id,
    templateId = templateId,
    key = key,
    displayName = displayName,
    isEnabled = isEnabled
)