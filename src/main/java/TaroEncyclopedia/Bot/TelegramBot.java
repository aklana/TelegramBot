package TaroEncyclopedia.Bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TelegramBot extends TelegramLongPollingBot {
    private List<String> feedbackList = new ArrayList<>();
    private boolean isFeedbackMode = false;

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
                sendMenu(chatId); // Отправляем основное меню
            } else if (messageText.equals("Изучение карт")) {
                message.setChatId(String.valueOf(chatId));
                message.setText("Кубки \n" +
                        "Мечи \n" +
                        "Жезлы \n" +
                        "Пентакли \n" +
                        "Старшие арканы");
                TryCatchMessage(message);
                sendMenu(chatId);
            } else if (messageText.equals("Ежедневное предсказание")) {
                message.setChatId(String.valueOf(chatId));
                message.setText(Cards.Take3());
                TryCatchMessage(message);
                sendMenu(chatId);

            } else if (messageText.equals("Обратная связь")) {
                message.setChatId(String.valueOf(chatId));
                message.setText("Выберите действие:\nОставить отзыв\nЧитать отзывы");
                TryCatchMessage(message);

            } else if (messageText.equals("Оставить отзыв")) {
                isFeedbackMode = true;
                message.setChatId(String.valueOf(chatId));
                message.setText("Пожалуйста, напишите ваш отзыв:");
                TryCatchMessage(message);

            } else if (isFeedbackMode) {
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

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText(feedbacks.toString());

                try {
                    execute(sendMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}