package com.georhii.codingtask.service

import com.georhii.codingtask.domain.Game
import com.georhii.codingtask.domain.GameState
import com.georhii.codingtask.domain.Player
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@SpringBootTest
class GameServiceTest {

    @Autowired
    lateinit var gameService: GameService

    lateinit var initialPlayer: Player
    lateinit var game: Game

    @BeforeEach
    fun initEach() {
        initialPlayer = Player("TestPlayer")
        game = gameService.startGame(initialPlayer)
    }

    @Test
    fun givenGameIsStarted_WhenTheTokenPlaced_ThenTokenOn1() {

        Assertions.assertEquals(1, initialPlayer.token.position)
    }

    @Test
    fun givenTokenOn1_WhenTokenMove3_ThenTokenOn4() {
        Assertions.assertEquals(1, initialPlayer.token.position)
        gameService.moveToken(initialPlayer, game.id,3)
        Assertions.assertEquals(4, initialPlayer.token.position)
    }

    @Test
    fun givenTokenOn1_WhenTokenMove3_AndTokenMove4_ThenTokenOn8() {
        Assertions.assertEquals(1, initialPlayer.token.position)
        gameService.moveToken(initialPlayer, game.id,3)
        gameService.moveToken(initialPlayer, game.id,4)
        Assertions.assertEquals(8, initialPlayer.token.position)
    }

    @Test
    fun givenTokenOn97_WhenTokenMove3_ThenPlayerHasWon() {
        initialPlayer.token.position = 97
        Assertions.assertEquals(97, initialPlayer.token.position)
        gameService.moveToken(initialPlayer, game.id,3)
        Assertions.assertEquals(GameState.FINISHED, game.state)
        Assertions.assertEquals(initialPlayer.id, game.winnerId)

    }

    @Test
    fun givenTokenOn97_WhenTokenMove4_ThenPlayerHasNotWon() {
        initialPlayer.token.position = 97
        Assertions.assertEquals(97, initialPlayer.token.position)
        gameService.moveToken(initialPlayer, game.id,4)
        Assertions.assertEquals(97, initialPlayer.token.position)
        Assertions.assertEquals(GameState.RUNNING, game.state)
        Assertions.assertEquals(null, game.winnerId)

    }

    @Test
    fun givenGameIsStarted_WhenPlayerRollsDice_ThenResultIs1to6() {
        val result = gameService.rollDice()
        Assertions.assertTrue(result in 1..6)
    }

    @Test
    fun givenPlayerRollsDice_WhenMovingToken_ThenTokenMovedOnDiceResult() {
        val initialTokenPosition = initialPlayer.token.position
        val diceResult = gameService.makeMove(game.id, initialPlayer.id)

        Assertions.assertTrue(diceResult in 1..6)
        Assertions.assertEquals(initialTokenPosition + diceResult,
                initialPlayer.token.position)
    }

    @Test
    fun givenGameIsFinished_ThenPlayerCanNotMove() {
        game.state = GameState.FINISHED
        try {
            gameService.makeMove(game.id, initialPlayer.id)
        } catch (e: ResponseStatusException) {
            Assertions.assertTrue(e.status == HttpStatus.FORBIDDEN)
        }
    }
}