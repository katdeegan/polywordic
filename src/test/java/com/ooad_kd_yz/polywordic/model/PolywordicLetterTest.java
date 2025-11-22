package com.ooad_kd_yz.polywordic.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolywordicLetterTest {

    @Test
    @DisplayName("PolywordicLetter toString()")
    void testToStringFormat() {
        PolywordicLetter letter = new PolywordicLetter('A', 0);
        letter.setStatus(LetterStatus.CORRECT_POSITION);
        assertEquals("A[CORRECT_POSITION]", letter.toString());
    }

    @Test
    @DisplayName("PolywordicLetters are equal")
    void testEqualsSameLetterAndPosition() {
        PolywordicLetter a1 = new PolywordicLetter('A', 1);
        PolywordicLetter a2 = new PolywordicLetter('A', 1);
        assertEquals(a1, a2);
    }

    @Test
    @DisplayName("Different characters are not equal")
    void testEqualsDifferentLetter() {
        PolywordicLetter a = new PolywordicLetter('A', 1);
        PolywordicLetter b = new PolywordicLetter('B', 1);
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("Different positions are not equal")
    void testEqualsDifferentPosition() {
        PolywordicLetter a1 = new PolywordicLetter('A', 1);
        PolywordicLetter a2 = new PolywordicLetter('A', 2);
        assertNotEquals(a1, a2);
    }

    @Test
    @DisplayName("PolywordicLetters hash codes are the same for equal letters")
    void testHashCodeSameForEqualObjects() {
        PolywordicLetter a1 = new PolywordicLetter('A', 2);
        PolywordicLetter a2 = new PolywordicLetter('A', 2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    @DisplayName("PolywordicLetters hash codes are different for different letters")
    void testHashCodeDifferentForDifferentObjects() {
        PolywordicLetter a = new PolywordicLetter('A', 1);
        PolywordicLetter b = new PolywordicLetter('B', 1);
        assertNotEquals(a.hashCode(), b.hashCode());
    }
}
