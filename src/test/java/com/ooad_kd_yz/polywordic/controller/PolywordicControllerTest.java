package com.ooad_kd_yz.polywordic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ooad_kd_yz.polywordic.model.PolywordicWord;
import com.ooad_kd_yz.polywordic.service.PolywordicGameService;
import com.ooad_kd_yz.polywordic.state.GameStateContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(PolywordicController.class)
public class PolywordicControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PolywordicGameService gameService;

    private GameStateContext mockGame;

    @BeforeEach
    void setUp() {
        mockGame = new GameStateContext("test-game-id", "APPLE", 6);
    }

    @Test
    @DisplayName("Should serve index page")
    void testIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    // Create new game tests - POST /api/game/new?difficulty=EASY
    @Test
    @DisplayName("Should create new game with default difficulty of EASY")
    void testCreateNewGameDefault() throws Exception {
        when(gameService.createNewGame("EASY")).thenReturn(mockGame);

        mockMvc.perform(post("/api/game/new"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value("test-game-id"))
                .andExpect(jsonPath("$.difficulty").value("EASY"))
                .andExpect(jsonPath("$.maxAttempts").value(6))
                .andExpect(jsonPath("$.remainingAttempts").value(6));

        verify(gameService, times(1)).createNewGame("EASY");
    }

    @Test
    @DisplayName("Should create new game with specified difficulty of EASY")
    void testCreateNewGameEasy() throws Exception {
        GameStateContext easyGame = new GameStateContext("easy-game", "BEACH", 6);
        when(gameService.createNewGame("EASY")).thenReturn(easyGame);

        mockMvc.perform(post("/api/game/new")
                        .param("difficulty", "EASY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value("easy-game"))
                .andExpect(jsonPath("$.difficulty").value("EASY"))
                .andExpect(jsonPath("$.maxAttempts").value(6));

        verify(gameService, times(1)).createNewGame("EASY");
    }

    @Test
    @DisplayName("Should create new game with specified difficulty of MEDIUM")
    void testCreateNewGameMedium() throws Exception {
        GameStateContext mediumGame = new GameStateContext("medium-game", "BEACH", 5);
        when(gameService.createNewGame("MEDIUM")).thenReturn(mediumGame);

        mockMvc.perform(post("/api/game/new")
                        .param("difficulty", "MEDIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value("medium-game"))
                .andExpect(jsonPath("$.difficulty").value("MEDIUM"))
                .andExpect(jsonPath("$.maxAttempts").value(5));

        verify(gameService, times(1)).createNewGame("MEDIUM");
    }

    @Test
    @DisplayName("Should create new game with specified difficulty of HARD")
    void testCreateNewGameHard() throws Exception {
        GameStateContext hardGame = new GameStateContext("hard-game", "BEACH", 4);
        when(gameService.createNewGame("HARD")).thenReturn(hardGame);

        mockMvc.perform(post("/api/game/new")
                        .param("difficulty", "HARD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value("hard-game"))
                .andExpect(jsonPath("$.difficulty").value("HARD"))
                .andExpect(jsonPath("$.maxAttempts").value(4));

        verify(gameService, times(1)).createNewGame("HARD");
    }

    @Test
    @DisplayName("Should handle error when creating game")
    void testCreateNewGameError() throws Exception {
        when(gameService.createNewGame(any())).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/game/new")
                        .param("difficulty", "EASY"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    // Get current game state tests
    @Test
    @DisplayName("Should get game state")
    void testGetGame() throws Exception {
        when(gameService.getGame("test-game-id")).thenReturn(mockGame);

        mockMvc.perform(get("/api/game/test-game-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value("test-game-id"))
                .andExpect(jsonPath("$.maxAttempts").value(6))
                .andExpect(jsonPath("$.currentAttempt").value(0))
                .andExpect(jsonPath("$.remainingAttempts").value(6))
                .andExpect(jsonPath("$.gameOver").value(false))
                .andExpect(jsonPath("$.won").value(false))
                .andExpect(jsonPath("$.stateName").value("ACTIVE"))
                .andExpect(jsonPath("$.targetWord").doesNotExist()); // Should not reveal word yet

        verify(gameService, times(1)).getGame("test-game-id");
    }

    @Test
    @DisplayName("Should return 404 when game not found")
    void testGetGameNotFound() throws Exception {
        when(gameService.getGame("nonexistent")).thenThrow(new IllegalArgumentException("Game not found"));

        mockMvc.perform(get("/api/game/nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Game not found"));
    }

    @Test
    @DisplayName("Should reveal target word when game is over")
    void testGetGameRevealWordWhenOver() throws Exception {
        GameStateContext completedGame = new GameStateContext("completed-game", "APPLE", 6);
        completedGame.makeGuess("APPLE"); // Win the game
        when(gameService.getGame("completed-game")).thenReturn(completedGame);

        mockMvc.perform(get("/api/game/completed-game"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameOver").value(true))
                .andExpect(jsonPath("$.won").value(true))
                .andExpect(jsonPath("$.targetWord").value("APPLE")); // Should reveal word
    }

    // Make guess tests
    // POST /api/game/{gameId}/guess, Body: { "guess": "APPLE" }
    @Test
    @DisplayName("Should make a valid guess")
    void testMakeGuess() throws Exception {
        PolywordicWord guessResult = new PolywordicWord("BEACH");
        when(gameService.makeGuess("test-game-id", "BEACH")).thenReturn(guessResult);
        when(gameService.getGame("test-game-id")).thenReturn(mockGame);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("guess", "BEACH");

        mockMvc.perform(post("/api/game/test-game-id/guess")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guess").value("BEACH"))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.gameOver").value(false))
                .andExpect(jsonPath("$.won").value(false))
                .andExpect(jsonPath("$.stateName").value("ACTIVE"));

        verify(gameService, times(1)).makeGuess("test-game-id", "BEACH");
    }

    @Test
    @DisplayName("Should reject invalid word")
    void testMakeGuessInvalidWord() throws Exception {
        when(gameService.makeGuess("test-game-id", "ZZZZZ"))
                .thenThrow(new IllegalArgumentException("Not a valid word: ZZZZZ"));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("guess", "ZZZZZ");

        mockMvc.perform(post("/api/game/test-game-id/guess")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Not a valid word: ZZZZZ"));
    }

    @Test
    @DisplayName("Should reject guess when game is over")
    void testMakeGuessGameOver() throws Exception {
        when(gameService.makeGuess("test-game-id", "BEACH"))
                .thenThrow(new IllegalStateException("Game is already won"));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("guess", "BEACH");

        mockMvc.perform(post("/api/game/test-game-id/guess")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("Should handle winning guess")
    void testMakeGuessWin() throws Exception {
        // Arrange
        PolywordicWord winningGuess = new PolywordicWord("APPLE");
        GameStateContext wonGame = new GameStateContext("win-game", "APPLE", 6);
        wonGame.makeGuess("APPLE");

        when(gameService.makeGuess("win-game", "APPLE")).thenReturn(winningGuess);
        when(gameService.getGame("win-game")).thenReturn(wonGame);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("guess", "APPLE");

        // Act & Assert
        mockMvc.perform(post("/api/game/win-game/guess")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameOver").value(true))
                .andExpect(jsonPath("$.won").value(true))
                .andExpect(jsonPath("$.stateName").value("WON"))
                .andExpect(jsonPath("$.targetWord").value("APPLE"));
    }

    // Test Delete game
    @Test
    @DisplayName("Should delete game")
    void testDeleteGame() throws Exception {
        mockMvc.perform(delete("/api/game/test-game-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Game deleted successfully"));

        verify(gameService, times(1)).deleteGame("test-game-id");
    }

    // Get available difficulties
    @Test
    @DisplayName("Should get available difficulties")
    void testGetAvailableDifficulties() throws Exception {
        when(gameService.getAvailableDifficulties()).thenReturn(Set.of("EASY", "MEDIUM", "HARD"));

        mockMvc.perform(get("/api/difficulties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$", containsInAnyOrder("EASY", "MEDIUM", "HARD")));
    }
}
