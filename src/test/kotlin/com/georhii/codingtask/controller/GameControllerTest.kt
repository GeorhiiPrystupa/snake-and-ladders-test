package com.georhii.codingtask.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.georhii.codingtask.objects.Constants
import com.georhii.codingtask.domain.Game
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.Cookie

@SpringBootTest
class GameControllerTest {

    val mvc: MockMvc by lazy {
        MockMvcBuilders
                .webAppContextSetup(context)
                .build()
    }

    val objectMapper: ObjectMapper by lazy {
        ObjectMapper().registerModule(KotlinModule())
    }

    @Autowired
    lateinit var context: WebApplicationContext

    lateinit var initialGame: Game
    lateinit var initialResponse: MockHttpServletResponse

    var playerCookie: Cookie? = null
    var gameCookie: Cookie? = null

    @BeforeEach
    fun  initEach() {
        initialResponse = mvc.perform(MockMvcRequestBuilders.post("/game/start")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk).andReturn().response

        initialGame = objectMapper.readValue(initialResponse.contentAsString, Game::class.java)
        playerCookie = initialResponse.cookies.find { it.name == Constants.PLAYER_ID_COOKIE_NAME }
        gameCookie = initialResponse.cookies.find { it.name == Constants.GAME_ID_COOKIE_NAME }
    }

    @Test
    fun startGameTest() {
        Assertions.assertNotNull(initialGame)
        Assertions.assertEquals(1, initialGame.players.size)
        Assertions.assertEquals(1, initialGame.players[0].token.position)
        Assertions.assertNotNull(playerCookie)
        Assertions.assertNotNull(gameCookie)
    }

    @Test
    fun moveTokenTest() {
        val response = mvc.perform(MockMvcRequestBuilders.post("/game/move")
                .cookie(playerCookie, gameCookie)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk).andReturn().response

        val game = objectMapper.readValue(response.contentAsString, Game::class.java)

        Assertions.assertNotNull(game)
        Assertions.assertNotNull(playerCookie)
        Assertions.assertNotNull(gameCookie)
        Assertions.assertNotEquals(1, game.players[0].token.position)
    }

    @Test
    fun moveTokenWithoutCookieTest() {
        mvc.perform(MockMvcRequestBuilders.post("/game/move")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
                .andExpect { it.resolvedException is ResponseStatusException }
    }




}