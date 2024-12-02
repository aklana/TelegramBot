package TaroEncyclopedia.Bot;
import java.util.*;

public class TarotCard {
    private final String name;
    private final String meaning;
    private final String invertedMeaning;
    private final String picture;
    private final String invertedPicture;
    private final Random random=new Random();

    public TarotCard(String _name, String _meaning,String _inv_meaning, String _pic,String _inv_pic)
    {
        this.name=_name;
        this.meaning=_meaning;
        this.invertedMeaning=_inv_meaning;
        this.picture=_pic;
        this.invertedPicture=_inv_pic;
    }
    public Map<String, String> card_position()
    {
        Map<String, String> card_details = new HashMap<>();
        card_details.put("name", this.name);
        if (random.nextBoolean()) {
            card_details.put("meaning", this.meaning);
            card_details.put("picture", this.picture);

        }
        else{
            card_details.put("meaning", this.invertedMeaning);
            card_details.put("picture", this.invertedPicture);
        }
        return card_details;
    }
    public String info(){
        String info;
        info=name+"\n"+meaning+"\n"+invertedMeaning+"\n"+picture;
        return info.replace("<b>", "").replace("</b>", "");
    }
}
