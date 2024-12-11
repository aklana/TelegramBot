package TaroEncyclopedia.Bot;
import java.util.*;

public class TarotCard {
    private final String name;
    private final String meaning;
    private final String invertedMeaning;
    private final String picture;
    private final String invertedPicture;
    private final Random random=new Random();

    public TarotCard(String _name, String _meaning,String _invMeaning, String _pic,String _invPic)
    {
        this.name=_name;
        this.meaning=_meaning;
        this.invertedMeaning=_invMeaning;
        this.picture=_pic;
        this.invertedPicture=_invPic;
    }
    public Map<String, String> cardPosition()
    {
        Map<String, String> cardDetails = new HashMap<>();
        cardDetails.put("name", this.name);
        if (random.nextBoolean()) {
            cardDetails.put("meaning", this.meaning);
            cardDetails.put("picture", this.picture);
        }
        else{
            cardDetails.put("meaning", this.invertedMeaning);
            cardDetails.put("picture", this.invertedPicture);
        }
        return cardDetails;
    }
    public String info(){
        String info=name+"\n"+meaning+"\n"+invertedMeaning;
        return info.replace("<b>", "").replace("</b>", "");
    }
    public String getPicture(){
        return picture;
    }
}
