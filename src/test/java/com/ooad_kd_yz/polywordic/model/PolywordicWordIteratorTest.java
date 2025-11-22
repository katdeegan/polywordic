package com.ooad_kd_yz.polywordic.model;

import com.ooad_kd_yz.polywordic.model.iterator.PWIterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PolywordicWordIteratorTest {
    @Test
    @DisplayName("PolywordicWordIterator should be implement PWIterator Interface")
    void testCustomIteratorInterface() {
        PolywordicWord word = new PolywordicWord("CASTLE");
        PWIterator<PolywordicLetter> iterator = word.createIterator();

        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertDoesNotThrow(() -> iterator.reset());
    }

    @Test
    @DisplayName("PolywordicWordIterator should be able to reset")
    void testIteratorResets() {
        PolywordicWord word = new PolywordicWord("CASTLE");
        PWIterator<PolywordicLetter> iterator = word.createIterator();

        PolywordicLetter currentLetter = iterator.next();
        assertEquals('C', currentLetter.getLetter());
        assertEquals(0, currentLetter.getPosition());

        PolywordicLetter nextLetter = iterator.next();
        assertEquals('A', nextLetter.getLetter());
        assertEquals(1, nextLetter.getPosition());

        iterator.reset();
        PolywordicLetter currentLetterAfterReset = iterator.next();
        assertEquals('C', currentLetterAfterReset.getLetter());
        assertEquals(0, currentLetterAfterReset.getPosition());


    }
}
