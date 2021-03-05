package com.georhii.codingtask.controller


import com.georhii.codingtask.objects.Constants
import com.georhii.codingtask.domain.Game
import com.georhii.codingtask.domain.Player
import com.georhii.codingtask.service.GameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(value = ["/game"])
class GameController(@Autowired val gameService: GameService) {

    @PostMapping(value = ["/start"])
    fun startGame(@RequestParam("name", defaultValue = "noname") name: String,
                  response: HttpServletResponse): Game {

        val player = Player(name)
        val game = gameService.startGame(player)
        val playerCookie = Cookie(Constants.PLAYER_ID_COOKIE_NAME, player.id)
        val gameCookie = Cookie(Constants.GAME_ID_COOKIE_NAME, game.id)
        response.addCookie(playerCookie)
        response.addCookie(gameCookie)

        return game
    }

    @PostMapping(value = ["/move"])
    fun moveToken(@CookieValue(value = Constants.PLAYER_ID_COOKIE_NAME) playerId: String?,
                  @CookieValue(value = Constants.GAME_ID_COOKIE_NAME) gameId: String?,
                  response: HttpServletResponse): Game {


        if (playerId == null || gameId == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No cookie")
        }

        gameService.makeMove(gameId, playerId)

        return gameService.getGameById(gameId)!!
    }

}