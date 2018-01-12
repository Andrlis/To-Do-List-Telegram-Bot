import bot.TodolistBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {
    public static void main(String [] args){
        ApiContextInitializer.init();
        TelegramBotsApi botApi = new TelegramBotsApi();
        try{
            botApi.registerBot(new TodolistBot());
        }catch (TelegramApiException exc){
            exc.printStackTrace();
        }
    }
}