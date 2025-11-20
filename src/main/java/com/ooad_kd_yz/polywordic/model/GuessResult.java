package com.ooad_kd_yz.polywordic.model;

// Represents the result of the player's letter guess in Polywordic
// GuessResult values include:
//  CORRECT: Letter in correct position (green tile)
//  PRESENT: Letter in word, but wrong position (yellow tile)
//  ABSENT: Letter not in word (gray tile)

public enum GuessResult {
    CORRECT,
    PRESENT,
    ABSENT
}
