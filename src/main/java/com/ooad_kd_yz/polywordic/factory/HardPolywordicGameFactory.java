package com.ooad_kd_yz.polywordic.factory;

import com.ooad_kd_yz.polywordic.state.GameStateContext;

// Factory Method Pattern - Concrete Creator for creating Hard Games (4 guess attempts)

public class HardPolywordicGameFactory implements IPolywordicGameFactory {

    private static final int MAX_ATTEMPTS = 4;

    @Override
    public GameStateContext createGame(String gameId, String targetWord) {
        return new GameStateContext(gameId, targetWord, MAX_ATTEMPTS);
    }

    @Override
    public int getMaxAttempts() {
        return MAX_ATTEMPTS;
    }

    @Override
    public String getDifficulty() {
        return "HARD";
    }
}
