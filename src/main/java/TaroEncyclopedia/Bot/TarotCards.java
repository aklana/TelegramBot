package TaroEncyclopedia.Bot;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TarotCards {
    private final Map<String, TarotCard> dictionary;
    public TarotCards(String filePath) throws FileNotFoundException {
        String name,meaning,inverted_meaning,picture,inverted_picture;
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        dictionary = new HashMap<>();
        while (scanner.hasNextLine()) {
            name=scanner.nextLine();
            meaning=scanner.nextLine();
            inverted_meaning=scanner.nextLine();
            picture=scanner.nextLine();
            inverted_picture=scanner.nextLine();
            dictionary.put(name.replace("<b>", "").replace("</b>", ""), new TarotCard(name,meaning,inverted_meaning,picture,inverted_picture));
        }
        scanner.close();
    }

    public TarotCard get(String key) {
        return dictionary.get(key);
    }

    public boolean containsKey(String key){return dictionary.containsKey(key);}

    public String[] Take3() {
        Map<String,String> firstCard,secondCard,thirdCard;
        String firstCardInfo,thirdCardInfo,secondCardInfo;
        var cards = new ArrayList<>(dictionary.keySet());
        Collections.shuffle(cards);
        var take3 = cards.stream().limit(3).toList();
        firstCard=dictionary.get(take3.getFirst()).card_position();
        secondCard=dictionary.get(take3.get(1)).card_position();
        thirdCard=dictionary.get(take3.getLast()).card_position();
        firstCardInfo=firstCard.get("name")+"\n"+firstCard.get("meaning");
        secondCardInfo=secondCard.get("name")+"\n"+secondCard.get("meaning");
        thirdCardInfo=thirdCard.get("name")+"\n"+thirdCard.get("meaning");
        var result=firstCardInfo+"\n\n"+secondCardInfo+"\n\n"+thirdCardInfo;

        return new String[]{result,firstCard.get("picture"),secondCard.get("picture"),thirdCard.get("picture")};
    }
}