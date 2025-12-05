package com.ooad_kd_yz.polywordic.factory;

import com.ooad_kd_yz.polywordic.state.GameStateContext;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EasyPolywordicGameFactoryTest {

    @Test
    void testCreateGame() {
        EasyPolywordicGameFactory factory = new EasyPolywordicGameFactory();
        GameStateContext game = factory.createGame("g1", "APPLE");
        assertNotNull(game);
        assertEquals(6, factory.getMaxAttempts());
        assertEquals("EASY", factory.getDifficulty());
    }
}