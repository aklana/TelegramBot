package org.example.Bot;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TarotCards {
    private Map<String, String> dictionary;
    public TarotCards(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        dictionary = new HashMap<>();
        while (scanner.hasNextLine()) {
            dictionary.put(scanner.nextLine(), scanner.nextLine());
        }
        System.out.println(dictionary.get("Бабка"));
        System.out.println(dictionary.get("Дед"));
        scanner.close();
    }
    public String get(String key) {
        return dictionary.get(key);
    }
}