package com.ooad_kd_yz.polywordic.repository;

// In-memory implementation of IWordRepository to generate random 5-letter word for Polywordic games.

import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class WordRepository implements IWordRepository {
    private final List<String> words;
    private final Set<String> wordSet;
    private final Random random;

    public WordRepository() {
        this.random = new Random();
        List<String> loadedWords = loadWordsFromResource("wordlist/words.txt");

        if (loadedWords.isEmpty()) {
            // Keep original fallback list
            this.words = Arrays.asList(
                    "ABOUT", "ABOVE", "APPLE", "HOUSE", "LIGHT", "MUSIC", "TABLE", "TRAIN", "SMILE", "WATER"
            );
            System.err.println("Dictionary file not found or empty, using fallback list (" + words.size() + " words).");
        } else {
            this.words = loadedWords;
            System.out.println("Loaded " + words.size() + " words from dictionary file.");
        }

        this.wordSet = new HashSet<>(words); // HashSet ensures uniqueness (not thread-safe if multiple threads are trying to access its elements)
    }

    protected List<String> loadWordsFromResource(String filename) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(filename)),
                StandardCharsets.UTF_8))) {

            return reader.lines()
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .filter(w -> w.matches("^[A-Z]{5}$"))
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            System.err.println("Failed to load dictionary: " + exception.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

    @Override
    public boolean isValidWord(String word) {
        return word != null && wordSet.contains(word.toUpperCase()) && word.length() == 5;
    }

    @Override
    public List<String> getAllWords() {
        return new ArrayList<>(words);
    }
}
