package com.ooad_kd_yz.polywordic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PolywordicWordTest {

    @Nested
    @DisplayName("PolywordicWord Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create PolywordicWord with valid 6-letter word")
        void testCreateValidWord() {
            PolywordicWord word = new PolywordicWord("CASTLE");

            assertEquals("CASTLE", word.getWord());
            assertEquals(6, word.wordLength());
            assertNotNull(word.getLetters());
        }

        @Test
        @DisplayName("Should convert lowercase to uppercase")
        void testUppercaseConversion() {
            PolywordicWord word = new PolywordicWord("castle");

            assertEquals("CASTLE", word.getWord());
            assertEquals('C', word.charAt(0));
            assertEquals('E', word.charAt(5));
        }

        @Test
        @DisplayName("Should handle mixed case input")
        void testMixedCaseInput() {
            PolywordicWord word = new PolywordicWord("CaStLe");
            assertEquals("CASTLE", word.getWord());
        }

        @ParameterizedTest
        @ValueSource(strings = {"APPLE", "CAT", "CASTLES", "A", ""})
        @DisplayName("Should throw exception for invalid word length")
        void testInvalidWordLength(String invalidWord) {
            assertThrows(IllegalArgumentException.class, () -> {
                new PolywordicWord(invalidWord);
            });
        }

        @Test
        @DisplayName("Should throw exception for null word")
        void testNullWord() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PolywordicWord(null)
            );

            assertTrue(exception.getMessage().contains("6 letters"));
        }

        @Test
        @DisplayName("Should initialize all letters with UNKNOWN status")
        void testInitialLetterStatus() {
            PolywordicWord word = new PolywordicWord("CASTLE");

            for (int i = 0; i < word.wordLength(); i++) {
                assertEquals(LetterStatus.UNKNOWN,
                        word.getLetter(i).getStatus());
            }
        }

        @Test
        @DisplayName("Should initialize letters with correct positions")
        void testLetterPositions() {
            PolywordicWord word = new PolywordicWord("CASTLE");

            for (int i = 0; i < word.wordLength(); i++) {
                assertEquals(i, word.getLetter(i).getPosition());
            }
        }
    }

    @Nested
    @DisplayName("Letter Access Tests")
    class LetterAccessTests {

        private PolywordicWord word;

        @BeforeEach
        void setUp() {
            word = new PolywordicWord("CASTLE");
        }

        @Test
        @DisplayName("Should get character at valid position")
        void testCharAt() {
            assertEquals('C', word.charAt(0));
            assertEquals('A', word.charAt(1));
            assertEquals('S', word.charAt(2));
            assertEquals('T', word.charAt(3));
            assertEquals('L', word.charAt(4));
            assertEquals('E', word.charAt(5));
        }

        @Test
        @DisplayName("Should get letter object at position")
        void testGetLetterAt() {
            PolywordicLetter letter = word.getLetter(0);

            assertNotNull(letter);
            assertEquals('C', letter.getLetter());
            assertEquals(0, letter.getPosition());
            assertEquals(LetterStatus.UNKNOWN, letter.getStatus());
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -5, 6, 7, 100})
        @DisplayName("Should throw exception for invalid position")
        void testInvalidPosition(int position) {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                word.charAt(position);
            });
        }

        @Test
        @DisplayName("Should get all letters as list")
        void testGetLetters() {
            List<PolywordicLetter> letters = word.getLetters();

            assertEquals('C', letters.get(0).getLetter());
            assertEquals('E', letters.get(5).getLetter());
        }

        @Test
        @DisplayName("Should get word length")
        void testLength() {
            assertEquals(6, word.wordLength());
        }
    }

    @Nested
    @DisplayName("Letter Status Tests")
    class LetterStatusTests {

        private PolywordicWord word;

        @BeforeEach
        void setUp() {
            word = new PolywordicWord("CASTLE");
        }

        @Test
        @DisplayName("Should update letter status at position")
        void testUpdateLetterStatus() {
            word.updateLetterStatus(0, LetterStatus.CORRECT_POSITION);
            word.updateLetterStatus(1, LetterStatus.INCORRECT_POSITION);
            word.updateLetterStatus(2, LetterStatus.NOT_IN_WORD);

            assertEquals(LetterStatus.CORRECT_POSITION,
                    word.getLetter(0).getStatus());
            assertEquals(LetterStatus.INCORRECT_POSITION,
                    word.getLetter(1).getStatus());
            assertEquals(LetterStatus.NOT_IN_WORD,
                    word.getLetter(2).getStatus());
        }

        @Test
        @DisplayName("Should not downgrade from CORRECT_POSITION")
        void testStatusPriorityCorrectPosition() {
            word.updateLetterStatus(0, LetterStatus.CORRECT_POSITION);

            word.updateLetterStatus(0, LetterStatus.INCORRECT_POSITION);
            assertEquals(LetterStatus.CORRECT_POSITION,
                    word.getLetter(0).getStatus());

            word.updateLetterStatus(0, LetterStatus.NOT_IN_WORD);
            assertEquals(LetterStatus.CORRECT_POSITION,
                    word.getLetter(0).getStatus());

            word.updateLetterStatus(0, LetterStatus.UNKNOWN);
            assertEquals(LetterStatus.CORRECT_POSITION,
                    word.getLetter(0).getStatus());
        }

        @Test
        @DisplayName("Should upgrade from WRONG_POSITION to CORRECT_POSITION")
        void testStatusUpgradeWrongToCorrect() {
            word.updateLetterStatus(1, LetterStatus.INCORRECT_POSITION);
            assertEquals(LetterStatus.INCORRECT_POSITION,
                    word.getLetter(1).getStatus());

            word.updateLetterStatus(1, LetterStatus.CORRECT_POSITION);
            assertEquals(LetterStatus.CORRECT_POSITION,
                    word.getLetter(1).getStatus());
        }

        @Test
        @DisplayName("Should not downgrade from WRONG_POSITION to NOT_IN_WORD")
        void testStatusPriorityWrongPosition() {
            word.updateLetterStatus(2, LetterStatus.INCORRECT_POSITION);
            word.updateLetterStatus(2, LetterStatus.NOT_IN_WORD);

            assertEquals(LetterStatus.INCORRECT_POSITION,
                    word.getLetter(2).getStatus());
        }

        @Test
        @DisplayName("Should upgrade from NOT_IN_WORD to better statuses")
        void testStatusUpgradeFromNotInWord() {
            word.updateLetterStatus(3, LetterStatus.NOT_IN_WORD);

            word.updateLetterStatus(3, LetterStatus.INCORRECT_POSITION);
            assertEquals(LetterStatus.INCORRECT_POSITION,
                    word.getLetter(3).getStatus());

            word.updateLetterStatus(3, LetterStatus.CORRECT_POSITION);
            assertEquals(LetterStatus.CORRECT_POSITION,
                    word.getLetter(3).getStatus());
        }

        @Test
        @DisplayName("Should reset all letter statuses to UNKNOWN")
        void testResetStatuses() {
            word.updateLetterStatus(0, LetterStatus.CORRECT_POSITION);
            word.updateLetterStatus(1, LetterStatus.INCORRECT_POSITION);
            word.updateLetterStatus(2, LetterStatus.NOT_IN_WORD);

            word.resetStatuses();

            for (int i = 0; i < word.wordLength(); i++) {
                assertEquals(LetterStatus.UNKNOWN,
                        word.getLetter(i).getStatus());
            }
        }

        @Test
        @DisplayName("Should check if fully guessed")
        void testIsGuessedCorrectly() {
            assertFalse(word.isGuessedCorrectly());

            // Mark some letters as correct
            word.updateLetterStatus(0, LetterStatus.CORRECT_POSITION);
            word.updateLetterStatus(1, LetterStatus.CORRECT_POSITION);
            assertFalse(word.isGuessedCorrectly());

            // Mark all letters as correct
            for (int i = 0; i < word.wordLength(); i++) {
                word.updateLetterStatus(i, LetterStatus.CORRECT_POSITION);
            }
            assertTrue(word.isGuessedCorrectly());
        }
    }

    @Nested
    @DisplayName("Word Matching Tests")
    class WordMatchingTests {

        @Test
        @DisplayName("Should match identical words")
        void testMatchesIdentical() {
            PolywordicWord word1 = new PolywordicWord("CASTLE");
            PolywordicWord word2 = new PolywordicWord("CASTLE");

            assertTrue(word1.matches(word2));
            assertTrue(word1.matches("CASTLE"));
        }

        @Test
        @DisplayName("Should match case-insensitive")
        void testMatchesCaseInsensitive() {
            PolywordicWord word = new PolywordicWord("CASTLE");

            assertTrue(word.matches("castle"));
            assertTrue(word.matches("CaStLe"));
            assertTrue(word.matches("CASTLE"));
        }

        @Test
        @DisplayName("Should not match different words")
        void testNotMatches() {
            PolywordicWord word1 = new PolywordicWord("CASTLE");
            PolywordicWord word2 = new PolywordicWord("BRIDGE");

            assertFalse(word1.matches(word2));
            assertFalse(word1.matches("BRIDGE"));
            assertFalse(word1.matches("PALACE"));
        }

        @Test
        @DisplayName("Should handle null in string match")
        void testMatchesNullString() {
            PolywordicWord word = new PolywordicWord("CASTLE");
            assertFalse(word.matches((String) null));
        }

        @Test
        @DisplayName("Same words are equal")
        void testWordEqualsSameWord() {
            PolywordicWord w1 = new PolywordicWord("MONKEY");
            PolywordicWord w2 = new PolywordicWord("MONKEY");
            assertEquals(w1, w2);
        }

        @Test
        @DisplayName("Different words not equal")
        void testWordEqualsDifferentWord() {
            PolywordicWord w1 = new PolywordicWord("MONKEY");
            PolywordicWord w2 = new PolywordicWord("DONKEY");
            assertNotEquals(w1, w2);
        }

        @Test
        @DisplayName("Hash code same for same word")
        void testHashCodeSameForEqualWords() {
            PolywordicWord w1 = new PolywordicWord("ORANGE");
            PolywordicWord w2 = new PolywordicWord("ORANGE");
            assertEquals(w1.hashCode(), w2.hashCode());
        }

        @Test
        @DisplayName("Hash code different for different word")
        void testHashCodeDifferentForDifferentWords() {
            PolywordicWord w1 = new PolywordicWord("ORANGE");
            PolywordicWord w2 = new PolywordicWord("PURPLE");
            assertNotEquals(w1.hashCode(), w2.hashCode());
        }

    }

    @Test
    @DisplayName("Should evaluate guess against target word")
    void testEvaluateGuess() {
        PolywordicWord target = new PolywordicWord("BANANA");
        PolywordicWord guess  = new PolywordicWord("CANNON");

        List<LetterStatus> results = target.evaluateGuess(guess);

        // B A N A N A
        // C A S T L E

        assertEquals(6, results.size());

        // Position-by-position expectations:
        // 0: C vs B
        assertEquals(LetterStatus.NOT_IN_WORD, results.get(0));

        // 1: A vs A → CORRECT_POSITION
        assertEquals(LetterStatus.CORRECT_POSITION, results.get(1));

        // 2: N vs N
        assertEquals(LetterStatus.CORRECT_POSITION, results.get(2));

        // 3: N va A
        assertEquals(LetterStatus.INCORRECT_POSITION, results.get(3));

        // 4: O vs N
        assertEquals(LetterStatus.NOT_IN_WORD, results.get(4));

        // 5: N vs A -> already have 2 N guess for the 2 Ns in word, should be NOT_IN_WORD
        assertEquals(LetterStatus.NOT_IN_WORD, results.get(5));
    }

}
