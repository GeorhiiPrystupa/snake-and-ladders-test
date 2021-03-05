package com.georhii.codingtask.domain

import java.util.*

data class Player(val name: String, val id: String = UUID.randomUUID().toString()) {

    val token: Token = Token()
}