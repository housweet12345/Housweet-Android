package com.housweet.data.mapper

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull

object FlexibleBooleanSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FlexibleBoolean", PrimitiveKind.BOOLEAN)

    override fun deserialize(decoder: Decoder): Boolean {
        // JsonDecoder면 원시 요소를 보고 타입 유연하게 처리
        if (decoder is JsonDecoder) {
            val elem = decoder.decodeJsonElement()
            if (elem is JsonPrimitive) {
                elem.booleanOrNull?.let { return it }             // true/false
                elem.intOrNull?.let { return it != 0 }            // 0/1 숫자
                val s = elem.content.lowercase()
                if (s == "true" || s == "1") return true           // "true"/"1"
                if (s == "false" || s == "0") return false         // "false"/"0"
            }
            throw SerializationException("Cannot decode boolean from $elem")
        }
        // 그 외엔 기본 불리언으로 시도
        return decoder.decodeBoolean()
    }

    override fun serialize(encoder: Encoder, value: Boolean) {
        // 서버가 0/1을 원한다면 아래줄 유지
        // encoder.encodeInt(if (value) 1 else 0)

        // 서버가 true/false를 기대하면 아래줄 사용
        encoder.encodeBoolean(value)
    }
}