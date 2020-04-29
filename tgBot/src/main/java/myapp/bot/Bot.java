package myapp.bot;

import myapp.model.Resume;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private static final String TOKEN = "787874764:AAH6ddP1juftDlTNynqUXHoYZYu7MJzdZfQ";
    private static final String USERNAME = "AndryushaReshetilovskiyBot";
    private static final long OWNER_ID = 588484700L;
    private HashMap<Long, Integer> stageOfResumeByChatId = new HashMap<>();
    private HashMap<Long, Resume> resumeByChatId = new HashMap<>();

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "Вопросы об анкете.":
                    sendMsg(message, "Требования в заполнении анкеты.\n" +
                            "В первом пункте нужно указать ФИО(фамилию, имя и отчество)\n" +
                            "Во втором пункте полный возраст(дату рождения). Так как работаем исключительно со совершеннолетними 18+.\n" +
                            "В третьем пункте нужно указать ваш действующий (рабочий) номер телефона чтобы с вами смог связаться наш оператор.\n" +
                            "В четвертом пункте указать логин в Skype. \n" +
                            "Вы спросите для чего нужен Skype?\n" +
                            "Всё очень просто, так как работа удаленная, собеседование с нашим специалистом соответственно тоже проходит удаленно, и так как Skype это надёжная и лучшая  программа для ведения корпоративного бизнеса, наши специалисты держут обратную связь с сотрудниками через Skype.\n");
                    break;
                case "Краткая информация о работе.":
                    sendMsg(message, "Мы предлагаем вам удалённую работу(дополнительный заработок) в свободное время. Работа в 20-ти различных направлениях.\n" +
                            "Одними из них являются:\n" +
                            "1. Работа с рекламой(это её просмотр и распространение).\n" +
                            "2. Обработка и поиск, данных и информации.\n" +
                            "3. Копирайтинг.\n" +
                            "4. SSM франшиза.\n" +
                            "На данный момент это одни из самых популярных направлений.\n" +
                            "Направление индивидуально подбирается с нашими специалистами, исходя из ваших навыков и трудового опыта.\n" +
                            "Работа не требует уделять себе много времени. На первоначальном этапе в среднем это 15-20 минут в день, 3-4 раза в неделю.\n" +
                            "В дальнейшем вы можете уделять работе больше времени, тем самым это положительно скажется на вашем доходе.\n" +
                            "Все что от вас требуется, это:\n" +
                            "1. Свободное время.\n" +
                            "2. Доступ к интернету.\n" +
                            "3. Желание работать.\n" +
                            "Никаких вложений и предоплат с вашей стороны не требуется.");
                    break;
                case "Заполнить анкету.":
                    stageOfResumeByChatId.put(message.getChatId(), 1);
                    getResume(message);
                    break;
                default:
                    if (stageOfResumeByChatId.get(message.getChatId()) != null && stageOfResumeByChatId.get(message.getChatId()) > 0)
                        getResume(message);
                    else
                        sendMsg(message, "Помощь в заполнении анкеты");
                    break;
            }
        }
    }


    private void sendMsg(Message message, String reply) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(reply);
        setButtons(sendMessage);
        executeSendMessage(sendMessage);
    }

    private void getResume(Message message) {
        long chatId = message.getChatId();
        String text = message.getText();
        switch (stageOfResumeByChatId.get(chatId)) {
            case 1:
                resumeByChatId.put(chatId, new Resume());
                resumeByChatId.get(chatId).setTgUsername(getUsernameFromMessage(message));
                stageOfResumeByChatId.put(chatId, 2);
                sendMsg(message, "Приступим к заполнению анкеты.");
                sendMsg(message, "1. Ваши инициалы (ФИО).");
                break;
            case 2:
                resumeByChatId.get(chatId).setFullName(text);
                stageOfResumeByChatId.put(chatId, 3);
                sendMsg(message, "2. Полный возраст.");
                break;
            case 3:
                resumeByChatId.get(chatId).setAge(text);
                stageOfResumeByChatId.put(chatId, 4);
                sendMsg(message, "3. Действующий номер телефона.");
                break;
            case 4:
                resumeByChatId.get(chatId).setTelephone(text);
                stageOfResumeByChatId.put(chatId, 5);
                sendMsg(message, "4. Логин в Skype.");
                break;
            case 5:
                resumeByChatId.get(chatId).setSkypeLogin(text);
                stageOfResumeByChatId.put(chatId, 0);
                sendResumeToOwner(resumeByChatId.get(chatId));
                sendMsg(message, "Резюме готово!");
                break;
            default:
                sendMsg(message,"Помощь в заполнении анкеты.");
                break;
        }
    }

    public void sendResumeToOwner(Resume resume) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(OWNER_ID);
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

    private void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Заполнить анкету."));
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Краткая информация о работе."));
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Вопросы об анкете."));
        keyboardRows.add(keyboardFirstRow);
        keyboardRows.add(keyboardSecondRow);
        keyboardRows.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

    private String getUsernameFromMessage(Message message){
        if (message.getFrom().getUserName()!=null)
            return message.getFrom().getUserName();
        if (message.getFrom().getFirstName()!=null)
            return message.getFrom().getFirstName();
        return message.getFrom().toString();
    }

    public String getBotUsername() {
        return USERNAME;
    }

    public String getBotToken() {
        return TOKEN;
    }
}