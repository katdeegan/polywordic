package com.ooad_kd_yz.polywordic.state;

// State Pattern - State Interface for Polywordic Game States

import com.ooad_kd_yz.polywordic.model.PolywordicWord;

public interface IGameState {
    // handle
    PolywordicWord handle(GameStateContext context, String guess);

    boolean isGameOver();

    boolean gameWon();

    String getStateName();
}
