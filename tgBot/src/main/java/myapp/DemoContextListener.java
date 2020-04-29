package myapp;

import myapp.bot.Bot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotSession;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DemoContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Bot tgBot;
        ApiContextInitializer.init();
        TelegramBotsApi bot = new TelegramBotsApi();
        try {
            BotSession botSession = bot.registerBot(tgBot=new Bot());
            servletContextEvent.getServletContext().setAttribute("bot",tgBot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
