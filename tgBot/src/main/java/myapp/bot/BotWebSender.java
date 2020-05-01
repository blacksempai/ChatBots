package myapp.bot;

import myapp.model.Owner;
import myapp.model.Resume;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BotWebSender extends TelegramLongPollingBot {

    private static final String TOKEN = "1229015107:AAF5PYdJuazQ0ULZh9jidYMmcUPl2HPfDvw";
    private static final String USERNAME = "ResumeSenderBot";
    private Owner owner = Owner.getInstance();

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(message.getChatId()!=owner.getId())
            sendMsg(message,"This is a private bot for non-commercial use.");
        else
        sendMsg(message,"Бот работает.\n Новых резюме нет.");
    }


    private void sendMsg(Message message, String reply) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(reply);
        executeSendMessage(sendMessage);
    }

    public void sendResumeToOwner(Resume resume) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(owner.getId());
        sendMessage.setText(resume.toString());
        executeSendMessage(sendMessage);
    }

    private void executeSendMessage(SendMessage sendMessage) {
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