package com.ooad_kd_yz.polywordic.service;

import com.ooad_kd_yz.polywordic.model.PolywordicWord;
import com.ooad_kd_yz.polywordic.repository.IWordRepository;
import com.ooad_kd_yz.polywordic.state.GameStateContext;
import com.ooad_kd_yz.polywordic.factory.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

// Service for managing Polywordic games (contains all business logic) - responsible for orchestrating communication between different components
// (word repository, game factories, game state, etc.), to effectively manages active games, validates guesses, and select which GameFactory to use based on difficulty level.

// Facade Pattern - provides a simplified interface for playing a game

@Service
public class PolywordicGameService {
    private final IWordRepository wordRepository;
    private final Map<String, GameStateContext> activeGames; // In-memory game storage
    private final Map<String, IPolywordicGameFactory> factories;

    public PolywordicGameService(IWordRepository wordRepository) {
        this.wordRepository = wordRepository;
        this.activeGames = new HashMap<>();
        this.factories = new HashMap<>();

        registerFactory(new EasyPolywordicGameFactory());
        registerFactory(new MediumPolywordicGameFactory());
        registerFactory(new HardPolywordicGameFactory());
    }

    private void registerFactory(IPolywordicGameFactory factory) {
        factories.put(factory.getDifficulty(), factory); // maps difficulty level to corresponding factory (i.e. "HARD" to HardPolywordiGameFactory)
    }

    // utilizes the Factory Method pattern to create appropriate game
    public GameStateContext createNewGame(String difficulty) {
        // Get the appropriate factory (default to EASY)
        IPolywordicGameFactory factory = factories.get(difficulty.toUpperCase());
        if (factory == null) {
            factory = factories.get("EASY");
        }

        String gameId = UUID.randomUUID().toString(); // generate unique game ID

        String targetWord = wordRepository.getRandomWord();

        GameStateContext game = factory.createGame(gameId, targetWord);

        activeGames.put(gameId, game); // keep track of all active games

        return game;
    }

    // constructor for default game
    public GameStateContext createNewGame() {
        return createNewGame("EASY");
    }

    public GameStateContext getGame(String gameId) {
        GameStateContext game = activeGames.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        return game;
    }

    public PolywordicWord makeGuess(String gameId, String guess) {
        // Validate input
        if (guess == null || guess.length() != 5) {
            throw new IllegalArgumentException("Guess must be exactly 5 letters");
        }

        // Validate word exists in dictionary
        if (!wordRepository.isValidWord(guess)) {
            throw new IllegalArgumentException("Not a valid word: " + guess);
        }

        // Get game and make guess
        GameStateContext game = getGame(gameId);
        return game.makeGuess(guess);
    }

    public void deleteGame(String gameId) {
        activeGames.remove(gameId);
    }

    public int getActiveGameCount() {
        return activeGames.size();
    }

    // TODO: possible helper method we may / may not need
    public Set<String> getAvailableDifficulties() {
        return factories.keySet();
    }

    public boolean isValidDifficulty(String difficulty) {
        return factories.containsKey(difficulty.toUpperCase());
    }
}
