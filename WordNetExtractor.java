import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class WordNetExtractor {
    public static void main(String[] args) {
        ///System.out.println("Current working directory: " + new File(".").getAbsolutePath());
        String inputFile = "C:\\Users\\User\\Downloads\\WNdb-3.0\\dict\\index.noun";
        String outputFile = "words.txt";

        Set<String> words = new HashSet<>();
        /*
          BufferedReader reader → deschide fisierul inputFile pentru citire.
        BufferedWriter writer → pentru scriere
        try-with-resources → Java închide automat fișierele când termină.
         */
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty() && Character.isLetter(line.charAt(0))) {
                    String word = line.split(" ")[0];
                    if (!word.contains("_")) {
                        if (word.length() >= 3 && word.length() <= 7) {
                            words.add(word.toUpperCase());
                        }
                    }
                }
            }

            for (String word : words) {
                writer.write(word);
                writer.newLine();
            }

            System.out.println("Extraction completed! Total words: " + words.size());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}