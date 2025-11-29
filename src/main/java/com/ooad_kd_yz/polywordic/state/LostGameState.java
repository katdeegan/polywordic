package com.ooad_kd_yz.polywordic.state;

// State Pattern - Concrete State Object
// In LostGameState, a player has exhausted all attempts without guessing the word correctly.

import com.ooad_kd_yz.polywordic.model.PolywordicWord;

public class LostGameState implements IGameState {
    @Override
    public PolywordicWord handle(GameStateContext context, String guess) {
        throw new IllegalStateException(
                "Game is over! You've used all your guesses. The word was: " +
                        context.getTargetWord().getWord()
        );
    }

    @Override
    public boolean isGameOver() {return true;}

    @Override
    public boolean gameWon() {return false;}

    @Override
    public String getStateName() {return "LOST";}
}
