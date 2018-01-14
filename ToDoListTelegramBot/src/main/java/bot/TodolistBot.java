package bot;

import bean.Task;
import bean.TaskList;
import dao.DAOException;
import dao.DAOFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodolistBot extends TelegramLongPollingBot {

    private Map<Integer, UserStatement> userStatementMap;

    public TodolistBot(){
        super();
        userStatementMap = new HashMap<Integer, UserStatement>();      //Временная реализация хранения состояния пользователя
    }

    @Override
    public String getBotUsername() {
        return "tdlist_bot";
    }

    @Override
    public String getBotToken() {
        return "427974260:AAF32bx7FZNdN0iRIxbtSoElQ_XVAnrjEZY";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            analizeMessage(update);

//            if (message.equals("/start") || message.equals("/show")) //В начале работы высвечиваются все списки (в функции надо сделать проверку)
//                showTaskLists(update.getMessage().getChatId());
//
//            if (message.equals("/add")){                             //надо отделить команду от названия
//                String nameList = "";
//                String[] wordArray = message.split(" ");          //преобразую сообщения в массив строк
//
//                for(int i = 1; i <= wordArray.length; i++){
//                    if (wordArray[i].equals(" "))                    // на тот случай если будет два пробела между словами
//                        continue;
//
//                    if(i != wordArray.length)
//                        nameList += wordArray[i] + " ";              // конкатенация собственно... если последнее то без пробела
//                    else
//                        nameList += wordArray[i];
//                }
//
//                if(nameList.isEmpty()){
//                    SendMessage errorMessage = new SendMessage()        //проверка на то, что хоть что-то ввели
//                            .setChatId(update.getMessage().getChatId())
//                            .setText("Введите имя списка");
//                }
//
//
//                addNewTaskList(update.getMessage().getChatId(),
//                               nameList);
//            }
//
//
//
//            SendMessage response = new SendMessage() // Create a SendMessage object with mandatory fields
//                    .setChatId(update.getMessage().getChatId())
//                    .setText(update.getMessage().getText());
//            try {
//                execute(response); // Call method to send the message
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void showTaskLists(Integer user_id, Long chat_id){ //user_id это ChatID, если я правильно понял, тогда не int, а long
        List<TaskList> taskLists = null;
        try {
            taskLists = DAOFactory.getTaskListDao().getTaskListsByUserID(user_id);
        }catch (DAOException exc){
            exc.printStackTrace();
        }

        String responseMsg = "";
        if(!taskLists.isEmpty()){
            for (TaskList taskList : taskLists){
                responseMsg += taskList.getListName() + "/n";
            }
        }

        SendMessage response = new SendMessage()
                .setChatId(chat_id)
                .setText(responseMsg);

        try {
            execute(response);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void deleteTaskList(int list_id, Long chat_id) throws DAOException {  //если я правильно понимаю то Index в листе и id в таблице одни и те же
         DAOFactory.getTaskListDao().removeTaskList(list_id);
    }

    private void changeTaskListName(int list_id, String newName, Integer user_id/*Long chat_id*/) throws DAOException {
        TaskList newList = DAOFactory.getTaskListDao().getTaskListByID(list_id);

        newList.setListName(newName);
        newList.setUserId(user_id);

        DAOFactory.getTaskListDao().updateTaskList(list_id, newList);
    }

    private void addNewTaskList(Integer user_id, String listName, Long chat_id) throws DAOException {
        TaskList newList = new TaskList();

        newList.setUserId(user_id);
        newList.setListName(listName);

    }

    private void showTasks(int list_id, Long chat_id){
        List<Task> tasks = null;
        try {
            tasks = DAOFactory.getTaskDao().getTasksByListID(list_id);
        }catch (DAOException exc){
            exc.printStackTrace();
        }

        String responseMsg = "";
        if(!tasks.isEmpty()) {
            for (Task task : tasks) {
                responseMsg+=task.toString();
            }
        }

        SendMessage response = new SendMessage()
                    .setChatId(chat_id)
                    .setText(responseMsg);
        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void changeTaskStatus(int task_id, Long chat_id, boolean status) throws DAOException {
        Task changedTask = DAOFactory.getTaskDao().getTaskByID(task_id);
        changedTask.setTaskStatus(status);

        DAOFactory.getTaskDao().updateTask(task_id, changedTask);
    }

    private void deleteTask(int task_id, Long chat_id) throws DAOException {
        DAOFactory.getTaskDao().removeTask(task_id);
    }

    private void changeTaskText(int task_id, String newTaskText, Long chat_id) throws DAOException {
        Task changedTask = DAOFactory.getTaskDao().getTaskByID(task_id);
        changedTask.setTaskDescription(newTaskText);

        DAOFactory.getTaskDao().updateTask(task_id, changedTask);
    }

    private void addNewTask(int list_id, String taskDescription, Long chat_id) throws DAOException {
        Task newTask = new Task();

        newTask.setTaskDescription(taskDescription);
        newTask.setTaskStatus(false);
        newTask.setListId(list_id);

        DAOFactory.getTaskDao().saveTask(newTask);
    }

    private void analizeMessage(Update update){
        String message = update.getMessage().getText();

        if (message.toLowerCase().contains("/start") || message.toLowerCase().contains("/show")) { //В начале работы высвечиваются все списки (в функции надо сделать проверку)
            showTaskLists(update.getMessage().getFrom().getId(), update.getMessage().getChatId());
        }
        if (message.toLowerCase().contains("/addlist")) {
            //addNewTaskList(update.getMessage().getFrom().getId(), );
        }
        if (message.toLowerCase().contains("/deletelist")) {
            //deleteTaskList();
        }
    }

    private void checkUserStatement(Update update){
        User user = update.getMessage().getFrom();
        UserStatement statement = getUserStatement(user.getId());

        if(statement.getLastCommand().equalsIgnoreCase("/addlist")){
            //ToDo
        }
        if(statement.getLastCommand().equalsIgnoreCase("/deletelist")){
            //ToDo
        }
        if(statement.getLastCommand().equalsIgnoreCase("/renamelist")){
            //ToDo
        }
        if(statement.getLastCommand().equalsIgnoreCase("/addtask")){
            //ToDo
        }
        if(statement.getLastCommand().equalsIgnoreCase("/deletetask")){
            //ToDo
        }
        if(statement.getLastCommand().equalsIgnoreCase("/changetask")){
            //ToDo
        }
        if(statement.getLastCommand().equalsIgnoreCase("/changetaskstatus")){
            //ToDo
        }
    }

    private UserStatement getUserStatement(Integer user_id){
        return userStatementMap.get(user_id);
    }
}