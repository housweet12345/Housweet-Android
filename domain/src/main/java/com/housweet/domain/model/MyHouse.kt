package com.housweet.domain.model

data class MyHouse(
    val id: Int,
    val name: String,
    val roomLeaderId: Int,
    val inviteCode: String,
    val dateOfCreated: String,
    val dateOfJoined: String
)
