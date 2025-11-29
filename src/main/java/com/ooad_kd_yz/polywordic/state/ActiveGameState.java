package com.ooad_kd_yz.polywordic.state;

// State Pattern - Concrete State Object
// In ActiveGameState, a player can guess words. The game can then transition to Won or Lost states.

import com.ooad_kd_yz.polywordic.model.PolywordicWord;

public class ActiveGameState implements IGameState {
    @Override
    public PolywordicWord handle(GameStateContext context, String guess) {
        // validate player hasn't exceeded max guesses
        if (context.getCurrentAttempt() >= context.getMaxAttempts()) {
            throw new IllegalStateException("No more guesses allowed");
        }

        // evaluate the guesses word and update guess history
        PolywordicWord guessWord = new PolywordicWord(guess);
        context.getTargetWord().evaluateGuess(guessWord);
        context.addGuess(guessWord);

        // Check if game should transition to a different state
        if (context.getTargetWord().matches(guessWord)) {
            context.setState(new WonGameState());
        } else if (context.getCurrentAttempt() >= context.getMaxAttempts()) {
            context.setState(new LostGameState());
        }

        return guessWord;
    }

    @Override
    public boolean isGameOver() {return false;}

    @Override
    public boolean gameWon() {return false;}

    @Override
    public String getStateName() {return "ACTIVE";}
}
