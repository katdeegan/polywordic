package com.ooad_kd_yz.polywordic.factory;

import com.ooad_kd_yz.polywordic.state.GameStateContext;

// Factory Pattern - Abstract Factory
// Defines the interface (Abstract Creator Class) that all concrete factories must implement
// (i.e. methods related to instantiating a new Polywordic game)

public interface IPolywordicGameFactory {
    GameStateContext createGame(String gameId, String targetWord); // factory method
    int getMaxAttempts();
    String getDifficulty();
}
