package TaroEncyclopedia.Bot;
import java.util.*;

public class TarotCard {
    private String name;
    private String meaning;
    private String inverted_meaning;
    private String picture;
    private String inverted_picture;

    public TarotCard(String _name, String _meaning,String _inv_meaning, String _pic,String _inv_pic)
    {
        this.name=_name;
        this.meaning=_meaning;
        this.inverted_meaning=_inv_meaning;
        this.picture=_pic;
        this.inverted_picture=_inv_pic;
    }
    public Map<String, String> card_position()
    {
        Map<String, String> card_details = new HashMap<>();
        card_details.put("name", this.name);
        if (Math.random() < 0.5) {
            card_details.put("meaning", this.meaning);
            card_details.put("picture", this.picture);

        }
        else{
            card_details.put("meaning", this.inverted_meaning);
            card_details.put("picture", this.inverted_picture);
        }
        return card_details;
    }
}
