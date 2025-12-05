package com.ooad_kd_yz.polywordic.factory;

import com.ooad_kd_yz.polywordic.state.GameStateContext;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MediumPolywordicGameFactoryTest {
    @Test
    void testCreateGame() {
        MediumPolywordicGameFactory factory = new MediumPolywordicGameFactory();
        GameStateContext game = factory.createGame("g2", "HOUSE");
        assertNotNull(game);
        assertEquals(5, factory.getMaxAttempts());
        assertEquals("MEDIUM", factory.getDifficulty());
    }
}