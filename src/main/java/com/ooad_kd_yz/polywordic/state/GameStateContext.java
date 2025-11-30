package com.ooad_kd_yz.polywordic.state;

import com.ooad_kd_yz.polywordic.model.LetterStatus;
import com.ooad_kd_yz.polywordic.model.PolywordicLetter;
import com.ooad_kd_yz.polywordic.model.PolywordicWord;
import com.ooad_kd_yz.polywordic.model.iterator.PolywordicWordIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// State Pattern - Context Object
// Maintains an instance of IGameState object and
// delegates state-specific behavior to the current state.

public class GameStateContext {

    private final String gameId;
    private final PolywordicWord targetWord;
    private final List<PolywordicWord> guesses;
    private final int maxAttempts;
    private IGameState currentState;

    // TODO: we could implement singleton pattern here, but with a map (i.e. make sure only one game exists with a given game ID)
    public GameStateContext(String gameId, String targetWord, int maxAttempts) {
        this.gameId = gameId;
        this.targetWord = new PolywordicWord(targetWord);
        this.maxAttempts = maxAttempts;
        this.guesses = new ArrayList<>();
        this.currentState = new ActiveGameState(); // Initial state
    }

    public String getGameId() { return gameId; }

    public PolywordicWord getTargetWord() { return targetWord; }

    public String getTargetWordString() { return targetWord.getWord();}

    public int getMaxAttempts() { return maxAttempts;}

    public int getCurrentAttempt() { return guesses.size(); }

    public int getRemainingAttempts() { return maxAttempts - guesses.size(); }

    public List<PolywordicWord> getGuesses() { return new ArrayList<>(guesses);}

    void addGuess(PolywordicWord guess) { guesses.add(guess); }

    void setState(IGameState state) { this.currentState = state; }

    public IGameState getCurrentState() { return currentState; }

    // Delegate behaviors to IGameState object
    public PolywordicWord makeGuess(String guess) {
        return currentState.handle(this, guess);
    }

    public boolean isGameOver() { return currentState.isGameOver(); }

    public boolean isWon() { return currentState.gameWon(); }

    public String getStateName() { return currentState.getStateName(); }

    // aggregate letter status across all guesses (use for updating keyboard display)
    public Map<Character, LetterStatus> getAggregateLetterStatuses() {
        Map<Character, LetterStatus> statusMap = new HashMap<>();

        for (char c = 'A'; c <= 'Z'; c++) {
            statusMap.put(c, LetterStatus.UNKNOWN);
        }

        // Iterate through all guessed words and update letter statuses
        for (PolywordicWord guess : guesses) {
            PolywordicWordIterator iterator = guess.createIterator(); // Iterator Pattern
            while (iterator.hasNext()) {
                PolywordicLetter letter = iterator.next();
                char c = letter.getLetter();
                LetterStatus currentStatus = statusMap.get(c);
                LetterStatus newStatus = letter.getStatus();

                // Update if new status is better (higher priority)
                if (letter.shouldUpdateStatus(currentStatus, newStatus)) {
                    statusMap.put(c, newStatus);
                }
            }
        }

        return statusMap;
    }

}