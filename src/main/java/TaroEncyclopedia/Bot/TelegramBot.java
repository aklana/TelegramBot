package TaroEncyclopedia.Bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.io.FileNotFoundException;
import java.util.*;

public class TelegramBot extends TelegramLongPollingBot {

    private void TryCatchMessage(SendMessage message){
    try {
        execute(message); // Отправляем сообщение
    } catch (Exception e) {
        e.printStackTrace(); // Обрабатываем возможные исключения
    }
}
    @Override
    public String getBotUsername() {
        return "TarotsSecretsBot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("token");
    }
    private void sendMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите действие:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Изучение карт");
        row.add("Ежедневное предсказание");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);
        TryCatchMessage(message);
    }

    @Override
    public void onUpdateReceived(Update update) {
        TarotCards Cards;
        try {
            Cards = new TarotCards("data.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        SendMessage message = new SendMessage();
        message.enableHtml(true);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                sendMenu(chatId);
            }
            else if (messageText.equals("Изучение карт")) {
                message.setChatId(String.valueOf(chatId));

                message.setText("Кубки \n"+
                        "Мечи \n" +
                        "Жезлы \n" +
                        "Пентакли \n"+
                        "Старшие арканы");
                TryCatchMessage(message);
            }

            else if (messageText.equals("Ежедневное предсказание")) {
                message.setChatId(String.valueOf(chatId));
                message.setText(Cards.Take3());
                TryCatchMessage(message);
            }
        }
    }
}