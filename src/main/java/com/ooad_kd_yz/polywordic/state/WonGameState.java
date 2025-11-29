package com.ooad_kd_yz.polywordic.state;

// State Pattern - Concrete State Object
// In WonGameState, a player has successfully guessed the word and no more guesses are allowed.

import com.ooad_kd_yz.polywordic.model.PolywordicWord;

public class WonGameState implements IGameState {
    @Override
    public PolywordicWord handle(GameStateContext context, String guess) {
        throw new IllegalStateException(
                "Game is already won! No more guesses allowed."
        );
    }

    @Override
    public boolean isGameOver() {return true;}

    @Override
    public boolean gameWon() {return true;}

    @Override
    public String getStateName() {return "WON";}
}
