package TaroEncyclopedia.Bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "TarotsSecretsBot";
    }

    @Override
    public String getBotToken() {
        return "8171701068:AAFJYsRWyScn7R2MNgRNJgBd8hmI_Ieos2k";
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

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void TryCatchMessage(SendMessage message){
        try {
            execute(message); // Отправляем сообщение
        } catch (Exception e) {
            e.printStackTrace(); // Обрабатываем возможные исключения
        }
    }
    @Override
    public void onUpdateReceived(Update update) {
            TarotCards Cards;
        try {
            Cards = new TarotCards("text.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        SendMessage message = new SendMessage();

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
            else if (messageText.equals("Мечи")) {
                message.setChatId(String.valueOf(chatId));
                message.setText("Туз мечей \n"+
                        "2 мечей \n" +
                        "3 мечей \n" +
                        "4 мечей \n"+
                        "5 мечей \n" +
                        "6 мечей \n" +
                        "7 мечей \n");
                TryCatchMessage(message);
            }//либо хэш мэп, либо подгрузка из ткст файла. грай кэтч своя функция
            else if (messageText.equals("Туз мечей")) {
                message.setChatId(String.valueOf(chatId));
                message.setText(Cards.get("Туз мечей"));
                TryCatchMessage(message);
            }
        }
    }
}