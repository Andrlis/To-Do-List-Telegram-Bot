package bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TodolistBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        /*return "tdlist_bot"*/;   //Затереть перед коммитом
    }

    @Override
    public String getBotToken() {
        return /*"427974260:AAF32bx7FZNdN0iRIxbtSoElQ_XVAnrjEZY"*/ "";     //Затереть перед коммитом
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String message = update.getMessage().getText();

            if (message.equals("/start") || message.equals("/show")) //В начале работы высвечиваются все списки (в функции надо сделать проверку)
                showTaskLists(update.getMessage().getChatId());

            if (message.equals("/add")){                             //надо отделить команду от названия
                String nameList = "";
                String[] wordArray = message.split(" ");          //преобразую сообщения в массив строк

                for(int i = 1; i <= wordArray.length; i++){
                    if (wordArray[i].equals(" "))                    // на тот случай если будет два пробела между словами
                        continue;

                    if(i != wordArray.length)
                        nameList += wordArray[i] + " ";              // конкатенация собственно... если последнее то без пробела
                    else
                        nameList += wordArray[i];
                }

                if(nameList.isEmpty()){
                    SendMessage errorMessage = new SendMessage()        //проверка на то, что хоть что-то ввели
                            .setChatId(update.getMessage().getChatId())
                            .setText("Введите имя списка");
                }


                addNewTaskList(update.getMessage().getChatId(),
                               nameList);
            }



            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void showTaskLists(int user_id){ //user_id это ChatID, если я правильно понял, тогда не int, а long
        //ToDo
    }

    private void deleteTaskList(int list_id){
        //ToDo
    }

    private void changeTaskListName(int list_id, String newName){
        //ToDo
    }

    private void addNewTaskList(int user_id, String listName){
        //ToDo
    }

    private void showTasks(int list_id){
        //ToDo
    }

    private void changeTaskStatus(int task_id){
        //ToDo
    }

    private void deleteTask(int task_id){
        //ToDo
    }

    private void changeTaskText(int task_id, String newTaskText){
        //ToDo
    }

    private void addNewTask(int list_id, String taskDescription){
        //ToDo
    }

    private void selectCommand(String command){
        //ToDO
    }
}