package com.ooad_kd_yz.polywordic.controller;

import com.ooad_kd_yz.polywordic.service.PolywordicGameService;
import com.ooad_kd_yz.polywordic.state.GameStateContext;
import com.ooad_kd_yz.polywordic.model.PolywordicWord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// MVC Pattern - Controller component
// Exposes REST endpoints to the frontend (java/resources/*), delegates business logic to PolywordicGameService

@Controller
public class PolywordicController {

    private final PolywordicGameService gameService;

    @Autowired
    public PolywordicController(PolywordicGameService gameService) {
        this.gameService = gameService;
    }

    // route to game homepage
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Create a new game of a specified difficulty (default to EASY is no param supplied)
    //  POST /api/game/new?difficulty=EASY
    @PostMapping("/api/game/new")
    @ResponseBody
    public ResponseEntity<?> createNewGame(
            @RequestParam(required = false, defaultValue = "EASY") String difficulty) {
        try {
            GameStateContext game = gameService.createNewGame(difficulty);

            Map<String, Object> response = new HashMap<>();
            response.put("gameId", game.getGameId());
            response.put("difficulty", difficulty.toUpperCase());
            response.put("maxAttempts", game.getMaxAttempts());
            response.put("remainingAttempts", game.getRemainingAttempts());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get current state of a game by ID
    @GetMapping("/api/game/{gameId}")
    @ResponseBody
    public ResponseEntity<?> getGame(@PathVariable String gameId) {
        try {
            GameStateContext game = gameService.getGame(gameId);

            Map<String, Object> response = new HashMap<>();
            response.put("gameId", game.getGameId());
            response.put("maxAttempts", game.getMaxAttempts());
            response.put("currentAttempt", game.getCurrentAttempt());
            response.put("remainingAttempts", game.getRemainingAttempts());
            response.put("gameOver", game.isGameOver());
            response.put("won", game.isWon());
            response.put("stateName", game.getStateName());
            response.put("guesses", game.getGuesses());
            response.put("letterStatuses", game.getAggregateLetterStatuses());

            if (game.isGameOver()) {
                response.put("targetWord", game.getTargetWordString()); // reveal targetWord if game is over
            }

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Make a guess in the game
    //  POST /api/game/{gameId}/guess
    //  Body: { "guess": "APPLE" }
    @PostMapping("/api/game/{gameId}/guess")
    @ResponseBody
    public ResponseEntity<?> makeGuess(@PathVariable String gameId,
                                       @RequestBody Map<String, String> payload) {
        try {
            String guess = payload.get("guess");

            PolywordicWord result = gameService.makeGuess(gameId, guess);

            GameStateContext game = gameService.getGame(gameId); // get updated game status

            Map<String, Object> response = new HashMap<>();
            response.put("guess", result.getWord());
            response.put("results", result.getLetters());
            response.put("gameOver", game.isGameOver());
            response.put("won", game.isWon());
            response.put("stateName", game.getStateName());
            response.put("currentAttempt", game.getCurrentAttempt());
            response.put("remainingAttempts", game.getRemainingAttempts());
            response.put("letterStatuses", game.getAggregateLetterStatuses());

            if (game.isGameOver()) {
                response.put("targetWord", game.getTargetWordString()); // reveal target word if game is over
            }

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete a game by gameId
    // DELETE /api/game/{gameId}
    @DeleteMapping("/api/game/{gameId}")
    @ResponseBody
    public ResponseEntity<?> deleteGame(@PathVariable String gameId) {
        try {
            gameService.deleteGame(gameId);
            return ResponseEntity.ok(Map.of("message", "Game deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all available difficulties
    // Response example: ["EASY", "MEDIUM", "HARD"]
    @GetMapping("/api/difficulties")
    @ResponseBody
    public ResponseEntity<?> getAvailableDifficulties() {
        return ResponseEntity.ok(gameService.getAvailableDifficulties());
    }
}
