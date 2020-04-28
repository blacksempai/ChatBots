import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {

    private static final String TOKEN = "787874764:AAH6ddP1juftDlTNynqUXHoYZYu7MJzdZfQ";
    private static final String USERNAME = "AndryushaReshetilovskiyBot";
    private HashMap<Long, Integer> stageOfResumeByChatId = new HashMap<>();
    private HashMap<Long, String> resumeByChatId = new HashMap<>();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi bot = new TelegramBotsApi();
        try {
            bot.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (stageOfResumeByChatId.get(message.getChatId()) != null && stageOfResumeByChatId.get(message.getChatId()) > 0) {
            if (message != null && message.hasText()) {
                switch (message.getText()) {
                    case "/help":
                        stageOfResumeByChatId.put(message.getChatId(), 0);
                        sendMsg(message, "test passed");
                        break;
                    case "/resume":
                        stageOfResumeByChatId.put(message.getChatId(), 1);
                        getResume(message);
                        break;
                    default:
                        getResume(message);
                        break;
                }
            }
        } else {
            if (message != null && message.hasText()) {
                switch (message.getText()) {
                    case "/help":
                        sendMsg(message, "test passed");
                        break;
                    case "/resume":
                        stageOfResumeByChatId.put(message.getChatId(), 1);
                        getResume(message);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void sendMsg(Message message, String reply) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(reply);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void getResume(Message message) {
        long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        switch (stageOfResumeByChatId.get(chatId)) {
            case 1:
                sendMessage.setText("ПІБ");
                try {
                    execute(sendMessage);
                    stageOfResumeByChatId.put(chatId, 2);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                resumeByChatId.put(chatId, message.getText());
                sendMessage.setText("Полный возраст");
                try {
                    execute(sendMessage);
                    stageOfResumeByChatId.put(chatId, 3);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                resumeByChatId.put(chatId, resumeByChatId.get(chatId) + " : " + message.getText());
                sendMessage.setText("Действующий номер телефона");
                try {
                    execute(sendMessage);
                    stageOfResumeByChatId.put(chatId, 4);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                resumeByChatId.put(chatId, resumeByChatId.get(chatId) + " : " + message.getText());
                sendMessage.setText("Логин в Skype");
                try {
                    execute(sendMessage);
                    stageOfResumeByChatId.put(chatId, 5);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                resumeByChatId.put(chatId, resumeByChatId.get(chatId) + " : " + message.getText());
                sendMessage.setText("Резюме готово");
                sendResumeToOwner(chatId);
                try {
                    execute(sendMessage);
                    stageOfResumeByChatId.put(chatId, 0);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void sendResumeToOwner(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(588484700L);
        sendMessage.setText(resumeByChatId.get(chatId));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return USERNAME;
    }

    public String getBotToken() {
        return TOKEN;
    }
}