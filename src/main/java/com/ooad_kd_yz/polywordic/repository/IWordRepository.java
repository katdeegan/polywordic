package com.ooad_kd_yz.polywordic.repository;

import java.util.List;

public interface IWordRepository {
    String getRandomWord();
    boolean isValidWord(String word);
    List<String> getAllWords();
}
