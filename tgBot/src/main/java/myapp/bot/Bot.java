package myapp.bot;

import myapp.model.Owner;
import myapp.model.Resume;
import myapp.model.ResumesFromWebsiteHolder;
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

public class Bot extends TelegramLongPollingBot {

    private static final String TOKEN = "1116825574:AAHqbkDw9HiiAcN8avgb4yHXIjVobhMV72g";
    private static final String USERNAME = "VladimirBot";
    private Owner owner = Owner.getInstance();
    private HashMap<Long, Integer> stageOfResumeByChatId = new HashMap<>();
    private HashMap<Long, Resume> resumeByChatId = new HashMap<>();

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "Вопросы об анкете.":
                    stageOfResumeByChatId.put(message.getChatId(), 0);
                    sendMsg(message, "Требования в заполнении анкеты.\n" +
                            "В первом пункте нужно указать ФИО(фамилию, имя и отчество)\n" +
                            "Во втором пункте полный возраст(дату рождения). Так как работаем исключительно со совершеннолетними 18+.\n" +
                            "В третьем пункте нужно указать ваш действующий (рабочий) номер телефона чтобы с вами смог связаться наш оператор.\n" +
                            "В четвертом пункте указать логин в Skype. \n" +
                            "Вы спросите для чего нужен Skype?\n" +
                            "Всё очень просто, так как работа удаленная, собеседование с нашим специалистом соответственно тоже проходит удаленно, и так как Skype это надёжная и лучшая  программа для ведения корпоративного бизнеса, наши специалисты держут обратную связь с сотрудниками через Skype.\n");
                    break;
                case "Краткая информация о работе.":
                    stageOfResumeByChatId.put(message.getChatId(), 0);
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
                    else if (message.getChatId().equals(owner.getId()))
                        sendResumesFromWebsite();
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

    private void sendMsgWithoutKeyboard(Message message, String reply) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(reply);
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
                sendMsgWithoutKeyboard(message, "Приступим к заполнению анкеты.");
                sendMsgWithoutKeyboard(message, "1. Ваше полное имя (ФИО).");
                break;
            case 2:
                resumeByChatId.get(chatId).setFullName(text);
                stageOfResumeByChatId.put(chatId, 3);
                sendMsgWithoutKeyboard(message, "2. Полный возраст.");
                break;
            case 3:
                resumeByChatId.get(chatId).setAge(text);
                stageOfResumeByChatId.put(chatId, 4);
                sendMsgWithoutKeyboard(message, "3. Действующий номер телефона.");
                break;
            case 4:
                resumeByChatId.get(chatId).setTelephone(text);
                stageOfResumeByChatId.put(chatId, 5);
                sendMsgWithoutKeyboard(message, "4. Логин в Skype.");
                break;
            case 5:
                resumeByChatId.get(chatId).setSkypeLogin(text);
                stageOfResumeByChatId.put(chatId, 0);
                sendResumeToOwner(resumeByChatId.get(chatId));
                sendMsg(message, "Анкета заполнена!");
                break;
            default:
                sendMsg(message,"Помощь в заполнении анкеты.");
                break;
        }
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

    private void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

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

    private void sendResumesFromWebsite(){
        for (Resume r:ResumesFromWebsiteHolder.getResumes()) {
            sendResumeToOwner(r);
        }
        ResumesFromWebsiteHolder.getResumes().clear();
    }

    public String getBotUsername() {
        return USERNAME;
    }

    public String getBotToken() {
        return TOKEN;
    }
}
//mvn appengine:deploy