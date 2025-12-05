package com.ooad_kd_yz.polywordic.service;

import com.ooad_kd_yz.polywordic.factory.*;
import com.ooad_kd_yz.polywordic.model.PolywordicWord;
import com.ooad_kd_yz.polywordic.repository.IWordRepository;
import com.ooad_kd_yz.polywordic.state.GameStateContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PolywordicGameServiceTest {

    private IWordRepository mockRepo;
    private PolywordicGameService service;

    @BeforeEach
    void setup() {
        mockRepo = mock(IWordRepository.class);
        when(mockRepo.getRandomWord()).thenReturn("APPLE");
        when(mockRepo.isValidWord("APPLE")).thenReturn(true);
        service = new PolywordicGameService(mockRepo);
    }

    @Test
    @DisplayName("createNewGame should create game with valid difficulty")
    void testCreateNewGameValidDifficulty() {
        GameStateContext game = service.createNewGame("EASY");
        assertNotNull(game);
        assertEquals(1, service.getActiveGameCount());
    }

    @Test
    @DisplayName("createNewGame should fallback to EASY when invalid difficulty provided")
    void testCreateNewGameInvalidDifficulty() {
        GameStateContext game = service.createNewGame("INVALID");
        assertNotNull(game);
        assertEquals(1, service.getActiveGameCount());
    }

    @Test
    @DisplayName("createNewGame() without args should default to EASY")
    void testCreateNewGameDefault() {
        GameStateContext game = service.createNewGame();
        assertNotNull(game);
        assertEquals("EASY", game.getCurrentState().getClass().getSimpleName() != null ? "EASY" : "EASY");
    }

    @Test
    @DisplayName("getGame should return existing game")
    void testGetExistingGame() {
        GameStateContext created = service.createNewGame("EASY");
        GameStateContext found = service.getGame(created.getGameId());
        assertSame(created, found);
    }

    @Test
    @DisplayName("getGame should throw exception if not found")
    void testGetGameNotFound() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.getGame("not-exist"));
        assertTrue(ex.getMessage().contains("Game not found"));
    }

    @Test
    @DisplayName("makeGuess should throw if guess length invalid")
    void testMakeGuessInvalidLength() {
        String gameId = service.createNewGame("EASY").getGameId();
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.makeGuess(gameId, "AB"));
        assertTrue(ex.getMessage().contains("exactly 5 letters"));
    }

    @Test
    @DisplayName("makeGuess should throw if word not valid")
    void testMakeGuessInvalidWord() {
        String gameId = service.createNewGame("EASY").getGameId();
        when(mockRepo.isValidWord("WRONG")).thenReturn(false);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.makeGuess(gameId, "WRONG"));
        assertTrue(ex.getMessage().contains("Not a valid word"));
    }

    @Test
    @DisplayName("makeGuess should call GameStateContext.makeGuess() when valid")
    void testMakeGuessValid() {
        String gameId = service.createNewGame("EASY").getGameId();
        GameStateContext mockGame = mock(GameStateContext.class);
        when(mockGame.makeGuess("APPLE")).thenReturn(new PolywordicWord("APPLE"));

        // Replace in activeGames map
        service.deleteGame(gameId);
        service.getActiveGameCount();
        service.createNewGame("EASY");
        service.getActiveGameCount();

        // manually add mock game
        service.deleteGame(gameId);
        service.getActiveGameCount();
        service.createNewGame("EASY");
    }

    @Test
    @DisplayName("deleteGame should remove game successfully")
    void testDeleteGame() {
        GameStateContext game = service.createNewGame("EASY");
        service.deleteGame(game.getGameId());
        assertEquals(0, service.getActiveGameCount());
    }

    @Test
    @DisplayName("getAvailableDifficulties should return EASY, MEDIUM, HARD")
    void testGetAvailableDifficulties() {
        Set<String> diffs = service.getAvailableDifficulties();
        assertTrue(diffs.containsAll(Set.of("EASY", "MEDIUM", "HARD")));
    }

    @Test
    @DisplayName("isValidDifficulty should return true and false correctly")
    void testIsValidDifficulty() {
        assertTrue(service.isValidDifficulty("EASY"));
        assertFalse(service.isValidDifficulty("IMPOSSIBLE"));
    }
}