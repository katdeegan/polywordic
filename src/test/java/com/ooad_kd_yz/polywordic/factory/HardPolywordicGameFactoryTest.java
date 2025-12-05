package com.ooad_kd_yz.polywordic.factory;

import com.ooad_kd_yz.polywordic.state.GameStateContext;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HardPolywordicGameFactoryTest {
    @Test
    void testCreateGame() {
        HardPolywordicGameFactory factory = new HardPolywordicGameFactory();
        GameStateContext game = factory.createGame("g3", "SMILE");
        assertNotNull(game);
        assertEquals(4, factory.getMaxAttempts());
        assertEquals("HARD", factory.getDifficulty());
    }
}
