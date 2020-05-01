package myapp;

import myapp.bot.Bot;
import myapp.bot.BotWebSender;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DemoContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        BotWebSender tgBot;
        ApiContextInitializer.init();
        TelegramBotsApi bot = new TelegramBotsApi();
        try {
            bot.registerBot(new Bot());
            bot.registerBot(tgBot=new BotWebSender());//to delete
            servletContextEvent.getServletContext().setAttribute("bot",tgBot);//to delete
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        servletContextEvent.getServletContext().setAttribute("admin_login","root");
        servletContextEvent.getServletContext().setAttribute("admin_password","hvosttruboy");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
