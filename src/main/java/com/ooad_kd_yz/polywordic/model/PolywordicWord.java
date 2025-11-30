package com.ooad_kd_yz.polywordic.model;

// Represents a 5-letter Polywordic word with individual letter GuessResult tracking.
// Implements the Iterator Pattern to allow iteration over letters and their states.

import com.ooad_kd_yz.polywordic.model.iterator.PWIterable;
import com.ooad_kd_yz.polywordic.model.iterator.PolywordicWordIterator;

import java.util.*;

public class PolywordicWord implements PWIterable {
    private final String word;
    private final List<PolywordicLetter> letters;

    public PolywordicWord(String word) {
        if (word == null || word.length() != 5) {
            throw new IllegalArgumentException("Word must be exactly 5 letters");
        }

        this.word = word.toUpperCase();
        this.letters = new ArrayList<>();

        for (int i = 0; i < this.word.length(); i++) {
            letters.add(new PolywordicLetter(this.word.charAt(i), i));
        }
    }

    public String getWord() {
        return word;
    }

    public int wordLength() {
        return word.length();
    }

    public PolywordicLetter getLetter(int position) {
        if (position < 0 || position >= this.wordLength()) {
            throw new IndexOutOfBoundsException("Position must be between 0 and " + (this.wordLength() - 1));
        }
        return letters.get(position);
    }

    public char charAt(int position) {
        return getLetter(position).getLetter();
    }

    public void updateLetterStatus(int position, LetterStatus status) {
        // Only update if new status has higher priority
        // word color on keyboard represents highest priority status
        PolywordicLetter letter = getLetter(position);
        if (letter.shouldUpdateStatus(letter.getStatus(), status)) {
            letter.setStatus(status);
        }
    }

    // Get all letters and their current status
    public List<PolywordicLetter> getLetters() {
        return letters;
    }

    // Returns true if word has been guessed correctly (all letters in word have status CORRECT_POSITION)
    public boolean isGuessedCorrectly() {
        for (PolywordicLetter letter : letters) {
            if (letter.getStatus() != LetterStatus.CORRECT_POSITION) {
                return false;
            }
        }
        return true;
    }

    public boolean matches(String other) {
        return this.word.equalsIgnoreCase(other);
    }

    public boolean matches(PolywordicWord other) {
        return this.word.equals(other.word);
    }

    // Iterator Pattern - create iterator object
    @Override
    public PolywordicWordIterator createIterator() {
        return new PolywordicWordIterator(letters);
    }

    // Count frequency of each letter in word using custom iterator
    private Map<Character, Integer> getLetterFrequencyMap() {
        Map<Character, Integer> letterCounts = new HashMap<>();
        PolywordicWordIterator iterator = this.createIterator();
        while (iterator.hasNext()) {
            char letter = iterator.next().getLetter();
            letterCounts.put(letter, letterCounts.getOrDefault(letter, 0) + 1);
        }

        return letterCounts;
    }

    // Evaluates a guess against the target word (this), and updates letter status
    public List<LetterStatus> evaluateGuess(PolywordicWord guess) {
        if (guess.wordLength() != this.wordLength()) {
            throw new IllegalArgumentException("Guess must be same length as target word");
        }

        List<LetterStatus> results = new ArrayList<>();

        Map<Character, Integer> targetWordLetterCounts = this.getLetterFrequencyMap();

        // First pass: mark correct positions using custom iterators
        LetterStatus[] guessResults = new LetterStatus[this.wordLength()];
        PolywordicWordIterator guessWordIterator = guess.createIterator();
        PolywordicWordIterator targetWordIterator = this.createIterator();

        while (guessWordIterator.hasNext() && targetWordIterator.hasNext()) {
            PolywordicLetter guessLetter = guessWordIterator.next();
            PolywordicLetter targetLetter = targetWordIterator.next();
            int position = guessLetter.getPosition();

            if (guessLetter.getLetter() == targetLetter.getLetter()) {
                guessResults[position] = LetterStatus.CORRECT_POSITION;
                guessLetter.setStatus(LetterStatus.CORRECT_POSITION);
                targetWordLetterCounts.put(guessLetter.getLetter(),
                        targetWordLetterCounts.get(guessLetter.getLetter()) - 1);
            }
        }

        // Second pass: mark present and absent using custom iterator
        guessWordIterator.reset(); // resets iterator to first element in guess word
        while (guessWordIterator.hasNext()) {
            PolywordicLetter guessLetter = guessWordIterator.next();
            int position = guessLetter.getPosition();

            if (guessResults[position] == null) { // letters NOT in correct position
                char letter = guessLetter.getLetter();
                if (targetWordLetterCounts.getOrDefault(letter, 0) > 0) {
                    guessResults[position] = LetterStatus.INCORRECT_POSITION;
                    guessLetter.setStatus(LetterStatus.INCORRECT_POSITION);
                    targetWordLetterCounts.put(letter, targetWordLetterCounts.get(letter) - 1);
                } else {
                    guessResults[position] = LetterStatus.NOT_IN_WORD;
                    guessLetter.setStatus(LetterStatus.NOT_IN_WORD);
                }
            }
            results.add(guessResults[position]);
        }

        return results;
    }

    public void resetStatuses() {
        PolywordicWordIterator iterator = this.createIterator();
        while (iterator.hasNext()) {
            PolywordicLetter letter = iterator.next();
            letter.setStatus(LetterStatus.UNKNOWN);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolywordicWord that = (PolywordicWord) o;
        return word.equals(that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

}
