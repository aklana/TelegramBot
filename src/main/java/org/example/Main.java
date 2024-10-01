package org.example;

import org.example.Bot.TarotCard;
import org.example.Bot.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileNotFoundException;

//public class Main {
    //public static void main(String[] args) throws TelegramApiException {
      //  TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        //telegramBotsApi.registerBot(new TelegramBot());
  //  }
//}
public class Main{
    public static void main(String[] args) throws FileNotFoundException {
        TarotCard name=new TarotCard("text.txt");
    }
}