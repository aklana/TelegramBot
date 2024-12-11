package TaroEncyclopedia.Bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TarotCards {
    private final Map<String, TarotCard> dictionary;

    public TarotCards(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        dictionary = new HashMap<>();
        while (scanner.hasNextLine()) {
            String name = scanner.nextLine();
            String meaning = scanner.nextLine();
            String invertedMeaning = scanner.nextLine();
            String picture = scanner.nextLine();
            String invertedPicture = scanner.nextLine();
            dictionary.put(name.replace("<b>", "").replace("</b>", ""), new TarotCard(name, meaning, invertedMeaning, picture, invertedPicture));
        }
        scanner.close();
    }

    public TarotCard get(String key) {
        return dictionary.get(key);
    }

    public boolean containsKey(String key) {
        return dictionary.containsKey(key);
    }

    public String[] take3() {
        Map<String, String> firstCard, secondCard, thirdCard;
        String firstCardInfo, thirdCardInfo, secondCardInfo;
        var cards = new ArrayList<>(dictionary.keySet());
        Collections.shuffle(cards);
        var take3 = cards.stream().limit(3).toList();
        firstCard = dictionary.get(take3.getFirst()).cardPosition();
        secondCard = dictionary.get(take3.get(1)).cardPosition();
        thirdCard = dictionary.get(take3.getLast()).cardPosition();
        firstCardInfo = firstCard.get("name") + "\n" + firstCard.get("meaning");
        secondCardInfo = secondCard.get("name") + "\n" + secondCard.get("meaning");
        thirdCardInfo = thirdCard.get("name") + "\n" + thirdCard.get("meaning");
        var result = firstCardInfo + "\n\n" + secondCardInfo + "\n\n" + thirdCardInfo;

        return new String[]{result, firstCard.get("picture"), secondCard.get("picture"), thirdCard.get("picture")};
    }
}