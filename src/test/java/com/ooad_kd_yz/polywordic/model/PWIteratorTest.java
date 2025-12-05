package com.ooad_kd_yz.polywordic.model;

import com.ooad_kd_yz.polywordic.model.iterator.PWIterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PWIteratorTest {

    @Test
    @DisplayName("Default reset() should throw UnsupportedOperationException")
    void testDefaultResetThrows() {
        PWIterator<Integer> iterator = new PWIterator<>() {
            private int index = 0;
            private final int[] data = {1, 2};

            @Override
            public boolean hasNext() {
                return index < data.length;
            }

            @Override
            public Integer next() {
                return data[index++];
            }
        };

        // Initiate default method
        UnsupportedOperationException ex = assertThrows(
                UnsupportedOperationException.class,
                iterator::reset
        );
        assertEquals("Reset operation not supported", ex.getMessage());
    }

    @Test
    @DisplayName("Custom reset() override should not throw")
    void testCustomResetOverride() {
        PWIterator<Integer> iterator = new PWIterator<>() {
            private int index = 0;
            private final int[] data = {1, 2};

            @Override
            public boolean hasNext() {
                return index < data.length;
            }

            @Override
            public Integer next() {
                return data[index++];
            }

            @Override
            public void reset() {
                index = 0;
            }
        };

        iterator.next();
        iterator.reset();
        assertTrue(iterator.hasNext());
    }
}
