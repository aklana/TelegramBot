package TaroEncyclopedia.Bot;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class TaroHashMap {
    private final Map<String, String> answers;
    public TaroHashMap(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        answers = new HashMap<>();
        while (scanner.hasNextLine()) {
            answers.put(scanner.nextLine(), scanner.nextLine());
        }
        scanner.close();
    }
    public String get(String key) {
        return answers.get(key);
    }

    public boolean containsKey(String key){return answers.containsKey(key);}
}
