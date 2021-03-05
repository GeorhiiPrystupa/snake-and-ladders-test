package com.georhii.codingtask.domain

import java.util.*

data class Game(val id: String = UUID.randomUUID().toString(),
                val boardSize: Int = 100,
                val players: MutableList<Player> = mutableListOf(),
                var state: GameState = GameState.RUNNING,
                var winnerId: String? = null) {

}

enum class GameState {
    RUNNING, FINISHED
}