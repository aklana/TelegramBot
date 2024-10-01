package org.example;

import org.example.Bot.TarotCards;
import org.example.Bot.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
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