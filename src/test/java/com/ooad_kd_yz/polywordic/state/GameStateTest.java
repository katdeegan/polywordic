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
            context = new GameStateContext("test-1", "CASTLE", 6);
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
            PolywordicWord result = context.makeGuess("BRIDGE");

            assertNotNull(result);
            assertEquals("BRIDGE", result.getWord());
            assertEquals(1, context.getCurrentAttempt());
            assertEquals(5, context.getRemainingAttempts());
        }

        @Test
        @DisplayName("Should remain in Active state after wrong guess")
        void testRemainActiveAfterWrongGuess() {
            context.makeGuess("BRIDGE");

            assertEquals("ACTIVE", context.getStateName());
            assertFalse(context.isGameOver());
            assertFalse(context.isWon());
        }

        @Test
        @DisplayName("Should transition to Won state on correct guess")
        void testTransitionToWonState() {
            context.makeGuess("CASTLE");

            assertEquals("WON", context.getStateName());
            assertTrue(context.isGameOver());
            assertTrue(context.isWon());
        }

        @Test
        @DisplayName("Should transition to Lost state after max attempts")
        void testTransitionToLostState() {
            context.makeGuess("BRIDGE");
            context.makeGuess("PALACE");
            context.makeGuess("CHANGE");
            context.makeGuess("ORANGE");
            context.makeGuess("FRANCE");
            context.makeGuess("PLACES");

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
            context = new GameStateContext("test-2", "CASTLE", 6);
            context.makeGuess("CASTLE");
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
                    () -> context.makeGuess("BRIDGE")
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
            GameStateContext ctx = new GameStateContext("test-3", "CASTLE", 3);
            ctx.makeGuess("BRIDGE");
            ctx.makeGuess("PALACE");
            ctx.makeGuess("CASTLE"); // Win on last attempt

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
            context = new GameStateContext("test-3", "CASTLE", 3);
            context.makeGuess("BRIDGE");
            context.makeGuess("PALACE");
            context.makeGuess("ORANGE");
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
                    () -> context.makeGuess("CASTLE")
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
            GameStateContext context = new GameStateContext("test-4", "CASTLE", 6);

            assertEquals("ACTIVE", context.getStateName());
            context.makeGuess("CASTLE");
            assertEquals("WON", context.getStateName());
        }

        @Test
        @DisplayName("Should transition Active -> Lost")
        void testActiveToLost() {
            GameStateContext context = new GameStateContext("test-5", "CASTLE", 2);

            assertEquals("ACTIVE", context.getStateName());
            context.makeGuess("BRIDGE");
            assertEquals("ACTIVE", context.getStateName());
            context.makeGuess("PALACE");
            assertEquals("LOST", context.getStateName());
        }

        @Test
        @DisplayName("Should not transition from Won state")
        void testCannotTransitionFromWon() {
            GameStateContext context = new GameStateContext("test-6", "CASTLE", 6);
            context.makeGuess("CASTLE");

            assertEquals("WON", context.getStateName());

            // Try to make another guess - should throw exception
            assertThrows(IllegalStateException.class,
                    () -> context.makeGuess("BRIDGE"));

            // Should still be in Won state
            assertEquals("WON", context.getStateName());
        }

        @Test
        @DisplayName("Should not transition from Lost state")
        void testCannotTransitionFromLost() {
            GameStateContext context = new GameStateContext("test-7", "CASTLE", 1);
            context.makeGuess("BRIDGE");

            assertEquals("LOST", context.getStateName());

            // Try to make another guess - should throw exception
            assertThrows(IllegalStateException.class,
                    () -> context.makeGuess("CASTLE"));

            // Should still be in Lost state
            assertEquals("LOST", context.getStateName());
        }

        @Test
        @DisplayName("Should handle multiple guesses before win")
        void testMultipleGuessesBeforeWin() {
            GameStateContext context = new GameStateContext("test-8", "CASTLE", 6);

            context.makeGuess("BRIDGE");
            assertEquals("ACTIVE", context.getStateName());

            context.makeGuess("PALACE");
            assertEquals("ACTIVE", context.getStateName());

            context.makeGuess("CASTLE");
            assertEquals("WON", context.getStateName());
        }
    }

    @Nested
    @DisplayName("Context Behavior Tests")
    class ContextBehaviorTests {

        @Test
        @DisplayName("Should maintain guess history")
        void testGuessHistory() {
            GameStateContext context = new GameStateContext("test-9", "CASTLE", 6);

            context.makeGuess("BRIDGE");
            context.makeGuess("PALACE");
            context.makeGuess("ORANGE");

            assertEquals(3, context.getGuesses().size());
            assertEquals("BRIDGE", context.getGuesses().get(0).getWord());
            assertEquals("PALACE", context.getGuesses().get(1).getWord());
            assertEquals("ORANGE", context.getGuesses().get(2).getWord());
        }

        @Test
        @DisplayName("Should track remaining attempts")
        void testRemainingAttempts() {
            GameStateContext context = new GameStateContext("test-10", "CASTLE", 6);

            assertEquals(6, context.getRemainingAttempts());

            context.makeGuess("BRIDGE");
            assertEquals(5, context.getRemainingAttempts());

            context.makeGuess("PALACE");
            assertEquals(4, context.getRemainingAttempts());
        }

        @Test
        @DisplayName("Should aggregate letter statuses")
        void testAggregateLetterStatuses() {
            GameStateContext context = new GameStateContext("test-11", "CASTLE", 6);

            context.makeGuess("CRANES");

            var statuses = context.getAggregateLetterStatuses();

            assertNotNull(statuses);
            assertEquals(26, statuses.size()); // All letters A-Z

            // C should be CORRECT_POSITION (position 0 in both words)
            assertEquals(LetterStatus.CORRECT_POSITION, statuses.get('C'));
        }

        @Test
        @DisplayName("Should return defensive copy of guesses")
        void testDefensiveCopyOfGuesses() {
            GameStateContext context = new GameStateContext("test-12", "CASTLE", 6);

            context.makeGuess("BRIDGE");

            var guesses1 = context.getGuesses();
            var guesses2 = context.getGuesses();

            assertNotSame(guesses1, guesses2);
            assertEquals(guesses1.size(), guesses2.size());
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle single attempt game")
        void testSingleAttemptGame() {
            GameStateContext context = new GameStateContext("test-13", "CASTLE", 1);

            assertEquals(1, context.getMaxAttempts());

            context.makeGuess("BRIDGE");
            assertEquals("LOST", context.getStateName());
        }

        @Test
        @DisplayName("Should win on first guess with single attempt")
        void testWinOnFirstGuessWithSingleAttempt() {
            GameStateContext context = new GameStateContext("test-14", "CASTLE", 1);

            context.makeGuess("CASTLE");
            assertEquals("WON", context.getStateName());
            assertEquals(1, context.getCurrentAttempt());
        }

        @Test
        @DisplayName("Should handle case-insensitive guesses")
        void testCaseInsensitiveGuesses() {
            GameStateContext context = new GameStateContext("test-15", "CASTLE", 6);

            PolywordicWord result = context.makeGuess("castle");
            assertEquals("CASTLE", result.getWord());
        }
    }
}