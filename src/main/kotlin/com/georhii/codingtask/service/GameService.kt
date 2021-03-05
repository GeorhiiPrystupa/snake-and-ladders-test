package com.georhii.codingtask.service

import com.georhii.codingtask.domain.Game
import com.georhii.codingtask.domain.Player
import org.springframework.stereotype.Service

@Service
class GameService {

    private val games: MutableMap<String, Game> = mutableMapOf()

    fun startGame(initialPlayer: Player): Game {
        return Game()
    }

    fun moveToken(player: Player, gameId: String, distance: Int) {

    }

    fun rollDice(gameId: String, playerId: String) : Int {
      return 0
    }

    private fun finishGame(gameId: String, winnerId: String) {

    }

}