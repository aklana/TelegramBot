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

    public String Take3() {
        Map<String,String> first_card,second_card,third_card;
        String first_card_info,third_card_info,second_card_info;
        var cards = new ArrayList<>(dictionary.keySet());
        Collections.shuffle(cards);
        var take3 = cards.stream().limit(3).toList();
        first_card=dictionary.get(take3.getFirst()).card_position();
        second_card=dictionary.get(take3.get(1)).card_position();
        third_card=dictionary.get(take3.getLast()).card_position();
        first_card_info=first_card.get("name")+"\n"+first_card.get("meaning")+"\n"+first_card.get("picture");
        second_card_info=second_card.get("name")+"\n"+second_card.get("meaning")+"\n"+second_card.get("picture");
        third_card_info=third_card.get("name")+"\n"+third_card.get("meaning")+"\n"+third_card.get("picture");
        var result=first_card_info+"\n\n"+second_card_info+"\n\n"+third_card_info;
        return result;
    }
}