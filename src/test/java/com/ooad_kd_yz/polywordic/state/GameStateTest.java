package com.ooad_kd_yz.polywordic.state;

import com.ooad_kd_yz.polywordic.model.LetterStatus;
import com.ooad_kd_yz.polywordic.model.PolywordicWord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Nested
    @DisplayName("Active State Tests")
    class ActiveStateTests {

        private GameStateContext context;

        @BeforeEach
        void setUp() {
            context = new GameStateContext("test-1", "RIGHT", 6);
        }

        @Test
        @DisplayName("Should start in Active state")
        void testInitialState() {
            assertEquals("ACTIVE", context.getStateName());
            assertFalse(context.isGameOver());
            assertFalse(context.isWon());
        }

        @Test
        @DisplayName("Should allow guesses in Active state")
        void testMakeGuessInActiveState() {
            PolywordicWord result = context.makeGuess("WRONG");

            assertNotNull(result);
            assertEquals("WRONG", result.getWord());
            assertEquals(1, context.getCurrentAttempt());
            assertEquals(5, context.getRemainingAttempts());
        }

        @Test
        @DisplayName("Should remain in Active state after wrong guess")
        void testRemainActiveAfterWrongGuess() {
            context.makeGuess("WRONG");

            assertEquals("ACTIVE", context.getStateName());
            assertFalse(context.isGameOver());
            assertFalse(context.isWon());
        }

        @Test
        @DisplayName("Should transition to Won state on correct guess")
        void testTransitionToWonState() {
            context.makeGuess("RIGHT");

            assertEquals("WON", context.getStateName());
            assertTrue(context.isGameOver());
            assertTrue(context.isWon());
        }

        @Test
        @DisplayName("Should transition to Lost state after max attempts")
        void testTransitionToLostState() {
            context.makeGuess("SUPER");
            context.makeGuess("LIGHT");
            context.makeGuess("TIGHT");
            context.makeGuess("WRONG");
            context.makeGuess("HELLO");
            context.makeGuess("SIGHT");

            assertEquals("LOST", context.getStateName());
            assertTrue(context.isGameOver());
            assertFalse(context.isWon());
        }
    }

    @Nested
    @DisplayName("Won State Tests")
    class WonStateTests {

        private GameStateContext context;

        @BeforeEach
        void setUp() {
            context = new GameStateContext("test-2", "RIGHT", 6);
            context.makeGuess("RIGHT");
        }

        @Test
        @DisplayName("Should be in Won state")
        void testWonState() {
            assertEquals("WON", context.getStateName());
            assertTrue(context.isGameOver());
            assertTrue(context.isWon());
        }

        @Test
        @DisplayName("Should not allow guesses in Won state")
        void testCannotGuessInWonState() {
            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> context.makeGuess("WRONG")
            );

            assertTrue(exception.getMessage().contains("already won"));
        }

        @Test
        @DisplayName("Should track number of guesses to win")
        void testGuessCountInWonState() {
            assertEquals(1, context.getCurrentAttempt());
        }

        @Test
        @DisplayName("Should win on last attempt")
        void testWinOnLastAttempt() {
            GameStateContext ctx = new GameStateContext("test-3", "SUPER", 3);
            ctx.makeGuess("FIGHT");
            ctx.makeGuess("TIGHT");
            ctx.makeGuess("SUPER"); // Win on last attempt

            assertEquals("WON", ctx.getStateName());
            assertTrue(ctx.isWon());
            assertEquals(3, ctx.getCurrentAttempt());
        }
    }

    @Nested
    @DisplayName("Lost State Tests")
    class LostStateTests {

        private GameStateContext context;

        @BeforeEach
        void setUp() {
            context = new GameStateContext("test-3", "SUPER", 3);
            context.makeGuess("FIGHT");
            context.makeGuess("TIGHT");
            context.makeGuess("LIGHT");
        }

        @Test
        @DisplayName("Should be in Lost state")
        void testLostState() {
            assertEquals("LOST", context.getStateName());
            assertTrue(context.isGameOver());
            assertFalse(context.isWon());
        }

        @Test
        @DisplayName("Should not allow guesses in Lost state")
        void testCannotGuessInLostState() {
            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> context.makeGuess("WRONG")
            );

            assertTrue(exception.getMessage().contains("over") ||
                    exception.getMessage().contains("used all"));
        }

        @Test
        @DisplayName("Should track all attempts in Lost state")
        void testGuessCountInLostState() {
            assertEquals(3, context.getCurrentAttempt());
            assertEquals(0, context.getRemainingAttempts());
        }
    }

    @Nested
    @DisplayName("State Transition Tests")
    class StateTransitionTests {

        @Test
        @DisplayName("Should transition Active -> Won")
        void testActiveToWon() {
            GameStateContext context = new GameStateContext("test-4", "FIGHT", 6);

            assertEquals("ACTIVE", context.getStateName());
            context.makeGuess("FIGHT");
            assertEquals("WON", context.getStateName());
        }

        @Test
        @DisplayName("Should transition Active -> Lost")
        void testActiveToLost() {
            GameStateContext context = new GameStateContext("test-5", "FLIPS", 2);

            assertEquals("ACTIVE", context.getStateName());
            context.makeGuess("WRONG");
            assertEquals("ACTIVE", context.getStateName());
            context.makeGuess("TIGHT");
            assertEquals("LOST", context.getStateName());
        }

        @Test
        @DisplayName("Should not transition from Won state")
        void testCannotTransitionFromWon() {
            GameStateContext context = new GameStateContext("test-6", "FLIPS", 6);
            context.makeGuess("FLIPS");

            assertEquals("WON", context.getStateName());

            // Try to make another guess - should throw exception
            assertThrows(IllegalStateException.class,
                    () -> context.makeGuess("SWEET"));

            // Should still be in Won state
            assertEquals("WON", context.getStateName());
        }

        @Test
        @DisplayName("Should not transition from Lost state")
        void testCannotTransitionFromLost() {
            GameStateContext context = new GameStateContext("test-7", "RIGHT", 1);
            context.makeGuess("WRONG");

            assertEquals("LOST", context.getStateName());

            // Try to make another guess - should throw exception
            assertThrows(IllegalStateException.class,
                    () -> context.makeGuess("FLIPS"));

            // Should still be in Lost state
            assertEquals("LOST", context.getStateName());
        }

        @Test
        @DisplayName("Should handle multiple guesses before win")
        void testMultipleGuessesBeforeWin() {
            GameStateContext context = new GameStateContext("test-8", "SUPER", 6);

            context.makeGuess("WRONG");
            assertEquals("ACTIVE", context.getStateName());

            context.makeGuess("MIGHT");
            assertEquals("ACTIVE", context.getStateName());

            context.makeGuess("SUPER");
            assertEquals("WON", context.getStateName());
        }
    }

    @Nested
    @DisplayName("Context Behavior Tests")
    class ContextBehaviorTests {

        @Test
        @DisplayName("Should maintain guess history")
        void testGuessHistory() {
            GameStateContext context = new GameStateContext("test-9", "RIGHT", 6);

            context.makeGuess("WRONG");
            context.makeGuess("SUPER");
            context.makeGuess("TIGHT");

            assertEquals(3, context.getGuesses().size());
            assertEquals("WRONG", context.getGuesses().get(0).getWord());
            assertEquals("SUPER", context.getGuesses().get(1).getWord());
            assertEquals("TIGHT", context.getGuesses().get(2).getWord());
        }

        @Test
        @DisplayName("Should track remaining attempts")
        void testRemainingAttempts() {
            GameStateContext context = new GameStateContext("test-10", "SUPER", 6);

            assertEquals(6, context.getRemainingAttempts());

            context.makeGuess("RIGHT");
            assertEquals(5, context.getRemainingAttempts());

            context.makeGuess("WRONG");
            assertEquals(4, context.getRemainingAttempts());
        }

        @Test
        @DisplayName("Should aggregate letter statuses")
        void testAggregateLetterStatuses() {
            GameStateContext context = new GameStateContext("test-11", "LINES", 6);

            context.makeGuess("LIGHT");

            var statuses = context.getAggregateLetterStatuses();

            assertNotNull(statuses);
            assertEquals(26, statuses.size()); // All letters A-Z

            // L & I should be CORRECT_POSITION (positions 0 & 1 in both words)
            assertEquals(LetterStatus.CORRECT_POSITION, statuses.get('L'));
            assertEquals(LetterStatus.CORRECT_POSITION, statuses.get('I'));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle single attempt game")
        void testSingleAttemptGame() {
            GameStateContext context = new GameStateContext("test-13", "SUPER", 1);

            assertEquals(1, context.getMaxAttempts());

            context.makeGuess("WRONG");
            assertEquals("LOST", context.getStateName());
        }

        @Test
        @DisplayName("Should win on first guess with single attempt")
        void testWinOnFirstGuessWithSingleAttempt() {
            GameStateContext context = new GameStateContext("test-14", "TIGHT", 1);

            context.makeGuess("TIGHT");
            assertEquals("WON", context.getStateName());
            assertEquals(1, context.getCurrentAttempt());
        }

        @Test
        @DisplayName("Should handle case-insensitive guesses")
        void testCaseInsensitiveGuesses() {
            GameStateContext context = new GameStateContext("test-15", "TENSE", 6);

            PolywordicWord result = context.makeGuess("tense");
            assertEquals("TENSE", result.getWord());
        }
    }
}