package com.ooad_kd_yz.polywordic.model;

// Represents the result of the player's guess for a PolywordicLetter

public enum LetterStatus {
    UNKNOWN, // Letter not guessed yet
    CORRECT_POSITION, // Letter in word, in correct position
    INCORRECT_POSITION, // Letter is in word, but position incorrect
    NOT_IN_WORD
}
