package com.ooad_kd_yz.polywordic.factory;

import com.ooad_kd_yz.polywordic.state.GameStateContext;

// Factory Method Pattern - Concrete Creator for creating Easy Games (6 guess attempts)

public class EasyPolywordicGameFactory implements IPolywordicGameFactory {

    private static final int MAX_ATTEMPTS = 6;

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
        return "EASY";
    }
}
