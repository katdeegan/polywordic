package com.ooad_kd_yz.polywordic.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordRepositoryTest {

    @Test
    @DisplayName("Should load words successfully from resource file")
    void testLoadWordsFromFileSuccess() {
        WordRepository repo = new WordRepository();

        List<String> words = repo.getAllWords();
        assertNotNull(words);
        assertFalse(words.isEmpty());
        assertTrue(words.stream().allMatch(w -> w.length() == 5));
        assertTrue(repo.isValidWord("APPLE"));
        assertFalse(repo.isValidWord("XXXXX"));
    }

    @Test
    @DisplayName("Should return fallback list when file not found")
    void testFallbackListWhenFileMissing() {
        WordRepository repo = new WordRepository() {
            @Override
            protected List<String> loadWordsFromResource(String filename) {
                return List.of();
            }
        };

        List<String> fallback = repo.getAllWords();
        assertEquals(10, fallback.size());
        assertTrue(repo.isValidWord("APPLE"));
        assertFalse(repo.isValidWord("ABCDE"));
    }

    @Test
    @DisplayName("Should handle invalid words correctly")
    void testIsValidWordVariousCases() {
        WordRepository repo = new WordRepository();
        assertFalse(repo.isValidWord(null));          // null case
        assertFalse(repo.isValidWord("TOO"));         // length < 5
        assertFalse(repo.isValidWord("TOOLONG"));     // length > 5
        assertFalse(repo.isValidWord("ZZZZZ"));       // not in the wordlist
    }

    @Test
    @DisplayName("Should return a random word from the list")
    void testGetRandomWord() {
        WordRepository repo = new WordRepository();
        String randomWord = repo.getRandomWord();
        assertNotNull(randomWord);
        assertEquals(5, randomWord.length());
        assertTrue(repo.isValidWord(randomWord));
    }
}