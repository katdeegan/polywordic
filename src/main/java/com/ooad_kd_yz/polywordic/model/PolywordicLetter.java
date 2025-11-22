package com.ooad_kd_yz.polywordic.model;

// Represents a single letter in the word with its GuessResult status

import java.util.Objects;

public class PolywordicLetter {
    private final char letter;
    private final int position; // position in PolywordicWord (0-5)
    private LetterStatus status;

    public PolywordicLetter(char letter, int position) {
        this.letter = letter;
        this.position = position;
        this.status = LetterStatus.UNKNOWN;
    }

    public char getLetter() {
        return letter;
    }

    public int getPosition() {
        return position;
    }

    public LetterStatus getStatus() {
        return status;
    }

    public void setStatus(LetterStatus status) {
        this.status = status;
    }

    protected boolean shouldUpdateStatus(LetterStatus current, LetterStatus newStatus) {
        if (current == LetterStatus.CORRECT_POSITION) {
            return false;
        }
        if (current == LetterStatus.INCORRECT_POSITION) {
            return newStatus == LetterStatus.CORRECT_POSITION;
        }
        if (current == LetterStatus.NOT_IN_WORD) {
            return newStatus == LetterStatus.CORRECT_POSITION ||
                    newStatus == LetterStatus.INCORRECT_POSITION;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%c[%s]", letter, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolywordicLetter that = (PolywordicLetter) o;
        return letter == that.letter && position == that.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, position);
    }
}
