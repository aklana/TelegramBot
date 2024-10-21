package org.example.Bot;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TarotCards {
    private Map<String, String> dictionary;
    public TarotCards(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        dictionary = new HashMap<>();
        while (scanner.hasNextLine()) {
            dictionary.put(scanner.nextLine(), scanner.nextLine());
        }
        scanner.close();
    }
    //public Set<String> getKeySet() {
     //   return new HashSet<>(dictionary.keySet());
    //}
    public String get(String key) {
        return dictionary.get(key);
    }

    public String Take3() {
        var cards = new ArrayList<>(dictionary.keySet());
        Collections.shuffle(cards);
        var take3 = cards.stream().limit(3).toList();
        var result=dictionary.get(take3.getFirst())+"\n\n"+ dictionary.get(take3.getLast())+"\n\n"+dictionary.get(take3.get(1));
        return result;
    }
}