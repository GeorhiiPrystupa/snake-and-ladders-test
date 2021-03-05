package com.georhii.codingtask.controller


import com.georhii.codingtask.objects.Constants
import com.georhii.codingtask.domain.Game
import com.georhii.codingtask.service.GameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(value = ["/game"])
class GameController(@Autowired val gameService: GameService) {

    @PostMapping(value = ["/start"])
    fun startGame(@RequestParam("name", defaultValue = "noname") name: String,
                  response: HttpServletResponse): Game {

        return Game()
    }

    @PostMapping(value = ["/move"])
    fun moveToken(@CookieValue(value = Constants.PLAYER_ID_COOKIE_NAME) playerId: String?,
                  @CookieValue(value = Constants.GAME_ID_COOKIE_NAME) gameId: String?,
                  response: HttpServletResponse): Game {


        return Game()
    }

}