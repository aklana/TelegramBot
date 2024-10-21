package org.example;

import org.example.Bot.TarotCards;
import org.example.Bot.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        var cardsMap = new HashMap<String,String>();
        //var cards = new ArrayList<>(cardsMap.keySet());
        var cards = new ArrayList<>(List.of("Sun", "Fool", "Death", "Lovers", "Hanget"));
        Collections.shuffle(cards);
        var take3 = cards.stream().limit(3).toList();
        var lol=take3.getFirst()+"\n" +take3.getLast()+"\n"+take3.get(1);
        System.out.println(lol);

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TelegramBot());
        try {
            TarotCards name=new TarotCards("text.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
//public class Main{
   // public static void main(String[] args) throws FileNotFoundException {
      //  TarotCards name=new TarotCards("text.txt");

   // }
//}