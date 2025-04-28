import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    private final Set<String> words = new HashSet<>();

    public Dictionary(String filename) {
        loadWordsFromFile(filename);
    }

    // Constructor alternativ pentru Set direct
    public Dictionary(Set<String> initialWords) {
        words.addAll(initialWords);
    }

    private void loadWordsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            /// pentru optimizare iau primele 100 de cuvinte
            int wordCount = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    words.add(line.trim().toUpperCase());
                    System.out.println("Loaded word: " + line.trim().toUpperCase());
                }
            }
            System.out.println("Dictionary loaded. Total words: " + words.size());
        } catch (IOException e) {
            System.err.println("Error reading dictionary file: " + e.getMessage());
        }
    }

    public boolean containsWord(String word) {
        return words.contains(word.toUpperCase());
    }

    public Set<String> getWords() {
        return words;
    }
}
