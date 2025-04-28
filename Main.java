import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Generating words...");
        List<String> words = DictionaryUtil.generateRandomWords(1_000_000);

        PrefixTree prefixTree = new PrefixTree();
        System.out.println("Building prefix tree...");
        for (String word : words) {
            prefixTree.insert(word);
        }

        String prefix = "abc";

        System.out.println("\nSearching using parallel streams...");
        long start = System.currentTimeMillis();
        List<String> result1 = DictionaryUtil.searchParallel(words, prefix);
        long end = System.currentTimeMillis();
        System.out.println("Parallel search found " + result1.size() + " words in " + (end - start) + " ms");

        System.out.println("\nSearching using prefix tree...");
        start = System.currentTimeMillis();
        boolean foundInTree = prefixTree.searchPrefix(prefix);
        end = System.currentTimeMillis();
        System.out.println("Prefix tree search result: " + foundInTree + " in " + (end - start) + " ms");
    }
}
