package TaroEncyclopedia.Bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TelegramBot extends TelegramLongPollingBot {
    private final List<String> feedbackList = new ArrayList<>();
    private boolean isFeedbackMode = false;
    private final TarotCards cards;
    private final TaroHashMap answers;
    public TelegramBot() throws FileNotFoundException {
        super();
        cards = new TarotCards("data.txt");
        answers = new TaroHashMap("data2.txt");
    }

    private void TryCatchMessage(SendMessage message) {
        try {
            execute(message); // Отправляем сообщение
        } catch (Exception e) {
            e.printStackTrace(); // Обрабатываем возможные исключения
        }
    }

    private void saveFeedback(String feedback) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("feedbacks.txt", true))) {
            writer.write(feedback);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении отзыва: " + e.getMessage());
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

    private @Nullable String sendPhoto(long chatId, String filePath) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(String.valueOf(chatId));
        photo.setPhoto(new InputFile(new java.io.File(filePath)));

        try {
            Message message = this.execute(photo);
            // Получаем список фотографий из сообщения
            List<PhotoSize> photos = message.getPhoto();
            if (photos != null && !photos.isEmpty()) {
                // Возвращаем file_id самой большой фотографии
                return photos.get(photos.size() - 1).getFileId();
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null; // Возвращаем null, если не удалось получить ID
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
        row.add("Обратная связь");
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
                sendMenu(chatId); // Отправляем основное меню

            }
            else if (answers.containsKey(messageText)) {
                message.setChatId(String.valueOf(chatId));
                message.setText(answers.get(messageText).replace("#", "\n"));
                TryCatchMessage(message);
            }
            else if (cards.containsKey(messageText)) {
                message.setChatId(String.valueOf(chatId));
                String information=cards.get(messageText).info();
                message.setText(information);
                TryCatchMessage(message);
            }

            else if (messageText.equals("Ежедневное предсказание")) {
                message.setChatId(String.valueOf(chatId));
                message.setText(cards.Take3());
                TryCatchMessage(message);
                sendMenu(chatId);
                sendPhoto(chatId, "C://Users//user//Downloads//e775e9ffc37f9bc9826e580f61811a5a.jpg");

            }
            else if (isFeedbackMode) {
                feedbackList.add(messageText);
                saveFeedback(messageText);
                isFeedbackMode = false;
                message.setChatId(String.valueOf(chatId));
                message.setText("Спасибо за ваш отзыв!");
                TryCatchMessage(message);
                sendMenu(chatId);

            } else if (messageText.equals("Читать отзывы")) {
                message.setChatId(String.valueOf(chatId));

                StringBuilder feedbacks = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new FileReader("feedbacks.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        feedbacks.append(line).append("\n");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    feedbacks.append("Файл с отзывами не найден.");
                } catch (IOException e) {
                    e.printStackTrace();
                    feedbacks.append("Ошибка при чтении отзывов.");
                }

                message.setText(feedbacks.toString());
                TryCatchMessage(message);
            }

        }
    }
}