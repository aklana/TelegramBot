package TaroEncyclopedia.Bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.io.FileNotFoundException;
import java.util.*;


public class TelegramBot extends TelegramLongPollingBot {


    private final TarotCards cards;
    private final TaroHashMap answers;
    public TelegramBot() throws FileNotFoundException {
        super();
        cards = new TarotCards("data.txt");
        answers = new TaroHashMap("data2.txt");
    }
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
        TryCatchMessage(message);
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        message.enableHtml(true);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                sendMenu(chatId);
            }
            else if (answers.containsKey(messageText)) {
                    message.setChatId(String.valueOf(chatId));
                    message.setText(answers.get(messageText).replace("#","\n"));
                    TryCatchMessage(message);

            }

            else if (messageText.equals("Ежедневное предсказание")) {
                message.setChatId(String.valueOf(chatId));
                message.setText(cards.Take3());
                TryCatchMessage(message);
            }
        }
    }
}