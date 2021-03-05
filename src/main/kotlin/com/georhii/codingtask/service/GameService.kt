package com.georhii.codingtask.service

import com.georhii.codingtask.domain.Game
import com.georhii.codingtask.domain.GameState
import com.georhii.codingtask.domain.Player
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class GameService {

    private val games: MutableMap<String, Game> = mutableMapOf()

    fun startGame(initialPlayer: Player): Game {
        val game = Game()
        game.players.add(initialPlayer)
        initialPlayer.token.position = 1
        games[game.id] = game
        return game
    }

    fun makeMove(gameId: String, playerId: String): Int {
        val diceResult = rollDice()
        val game = games[gameId]!!
        if (game.state == GameState.FINISHED)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "The game is finished")
        val player = game.players.find { it.id == playerId }

        moveToken(player!!, gameId, diceResult)

        return diceResult
    }

    fun moveToken(player: Player, gameId: String, distance: Int) {
        val game = games[gameId]!!
        val initialPosition = player.token.position
        player.token.position += distance

        if (player.token.position > game.boardSize) {
            player.token.position = initialPosition
        } else if (player.token.position == game.boardSize) {
            finishGame(gameId, player.id)
        }
    }

    fun rollDice() : Int {
        return (1..6).random()
    }

    private fun finishGame(gameId: String, winnerId: String) {
        val game = games[gameId]!!
        game.state = GameState.FINISHED
        game.winnerId = winnerId
    }

}