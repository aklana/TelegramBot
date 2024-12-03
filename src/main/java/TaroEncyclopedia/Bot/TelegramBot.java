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
import java.util.Timer;
import java.util.TimerTask;


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

    private void silentSendMessage(SendMessage message) {
        try {
            execute(message); // Отправляем сообщение
        } catch (Exception e) {
            e.printStackTrace(); // Обрабатываем возможные исключения
        }
    }

    public static class Feedback {
        private final String text;
        private final int rating;

        public Feedback(String text, int rating) {
            this.text = text;
            this.rating = rating;
        }

        public String getText() {
            return text;
        }

        public int getRating() {
            return rating;
        }
    }



    //Классы и методы для обратной связи и рейтинга
    public void saveFeedback(Feedback feedback) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("feedbacks.txt", true))) {
            bw.write(feedback.getText() + " | Оценка: " + feedback.getRating());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scheduleTask(Runnable task, int delayInSeconds) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, delayInSeconds * 1000L);
    }


    @Override
    public String getBotUsername() {
        return "TarotsSecretsBot";
    }

    @Override
    public String getBotToken() {
        return "8171701068:AAFJYsRWyScn7R2MNgRNJgBd8hmI_Ieos2k";//System.getenv("token");
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
        silentSendMessage(message);
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
                silentSendMessage(message);
            }
            else if (cards.containsKey(messageText)) {
                message.setChatId(String.valueOf(chatId));
                String information=cards.get(messageText).info();
                message.setText(information);
                silentSendMessage(message);
                sendPhoto(chatId, cards.get(messageText).getPicture());
                sendMenu(chatId);
            }

            else if (messageText.equals("Ежедневное предсказание")) {
                message.setChatId(String.valueOf(chatId));
                String[] result=cards.Take3();
                message.setText(result[0]);
                silentSendMessage(message);
                //scheduleTask(() -> silentSendMessage(message), 5); //Выводим ежедневное предсказание на 5 сек позже
                for (int i=1;i<4;i++)
                    sendPhoto(chatId, result[i]);
                sendMenu(chatId);
            }
            else if (isFeedbackMode) {
                if (feedbackList.isEmpty()) {
                    feedbackList.add(messageText);
                    message.setChatId(String.valueOf(chatId));
                    message.setText("Спасибо! Теперь, пожалуйста, оцените работу бота от 1 до 5:");
                    silentSendMessage(message);
                } else {
                    try {
                        int rating = Integer.parseInt(messageText);
                        if (rating < 1 || rating > 5) {
                            throw new NumberFormatException();
                        }
                        Feedback feedback = new Feedback(feedbackList.getFirst(), rating); // Создаем объект отзыва
                        saveFeedback(feedback);
                        feedbackList.clear();
                        isFeedbackMode = false;
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Спасибо за ваш отзыв и оценку!");
                        silentSendMessage(message);
                        sendMenu(chatId);
                    } catch (NumberFormatException e) {
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Пожалуйста, введите корректный рейтинг от 1 до 5.");
                        silentSendMessage(message);
                    }
                }

            } else if (messageText.equals("Читать отзывы")) {
                message.setChatId(String.valueOf(chatId));
                StringBuilder feedbacks = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new FileReader("feedbacks.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        feedbacks.append(line).append("\n");
                    }
                    if (feedbacks.isEmpty()) {
                        message.setText("Отзывов пока нет.");
                    } else {
                        message.setText(feedbacks.toString());
                    }
                } catch (IOException e) {
                    message.setText("Ошибка при чтении отзывов.");
                    e.printStackTrace();
                }
                silentSendMessage(message);
                sendMenu(chatId);
            }

        }
    }
}