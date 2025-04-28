import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
public class DictionaryUtil {
    public static List<String> generateRandomWords(int count) {
        List<String> words = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int length = random.nextInt(5) + 3; // cuvinte între 3 și 7 litere
            StringBuilder word = new StringBuilder();
            for (int j = 0; j < length; j++) {
                char c = (char) (random.nextInt(26) + 'a');
                word.append(c);
            }
            words.add(word.toString());
        }
        return words;
    }

    public static List<String> searchParallel(List<String> words, String prefix) {
        return words.parallelStream()
                .filter(word -> word.startsWith(prefix))
                .collect(Collectors.toList());
    }
}
