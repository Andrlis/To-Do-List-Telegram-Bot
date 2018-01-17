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

        sendMessage(responseMsg, chat_id);
    }

    private void deleteTaskList(int list_id) {  //если я правильно понимаю то Index в листе и id в таблице одни и те же
        try {
            DAOFactory.getTaskListDao().removeTaskList(list_id);
        }catch (DAOException exc){
            exc.printStackTrace();
        }
    }

    private void changeTaskListName(int list_id, String newName, Integer user_id/*Long chat_id*/) {
        try {
            TaskList newList = DAOFactory.getTaskListDao().getTaskListByID(list_id);

            newList.setListName(newName);
            /*newList.setUserId(user_id);*/             //Зачем ставить user_id, если он уже есть

            DAOFactory.getTaskListDao().updateTaskList(list_id, newList);
        }catch (DAOException exc){
            exc.printStackTrace();
        }
    }

    private void addNewTaskList(Integer user_id, String listName) {
        TaskList newList = new TaskList();

        newList.setUserId(user_id);
        newList.setListName(listName);

        try {
            DAOFactory.getTaskListDao().saveTaskList(newList);
        }catch (DAOException exc){
            exc.printStackTrace();
        }
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

        sendMessage(responseMsg, chat_id);
    }

    private void changeTaskStatus(int task_id, boolean status) {
        try {
            Task changedTask = DAOFactory.getTaskDao().getTaskByID(task_id);
            changedTask.setTaskStatus(status);

            DAOFactory.getTaskDao().updateTask(task_id, changedTask);
        }catch (DAOException exc){
            exc.printStackTrace();
        }
    }

    private void deleteTask(int task_id) {
        try{
            DAOFactory.getTaskDao().removeTask(task_id);
        }catch (DAOException exc){
            exc.printStackTrace();
        }
    }

    private void changeTaskText(int task_id, String newTaskText){
        try{
            Task changedTask = DAOFactory.getTaskDao().getTaskByID(task_id);
            changedTask.setTaskDescription(newTaskText);

            DAOFactory.getTaskDao().updateTask(task_id, changedTask);
        }catch (DAOException exc){
            exc.printStackTrace();
        }
    }

    private void addNewTask(int list_id, String taskDescription) {
        Task newTask = new Task();

        newTask.setTaskDescription(taskDescription);
        newTask.setTaskStatus(false);
        newTask.setListId(list_id);

        try {
            DAOFactory.getTaskDao().saveTask(newTask);
        }catch (DAOException exc){
            exc.printStackTrace();
        }
    }

    private void analizeMessage(Update update){
        String message = update.getMessage().getText();

        checkUserStatement(update);

        if (message.toLowerCase().contains("/start") || message.toLowerCase().contains("/show")) { //В начале работы высвечиваются все списки (в функции надо сделать проверку)
            showTaskLists(update.getMessage().getFrom().getId(), update.getMessage().getChatId());
        }
        if (message.toLowerCase().contains("/addlist")) {
            UserStatement statement = getUserStatement(update.getMessage().getFrom().getId());
            statement.setLastCommand("/addlist");
            sendMessage("Enter new list`s title", update.getMessage().getChatId());
        }
        if (message.toLowerCase().contains("/deletelist")) {
            UserStatement statement = getUserStatement(update.getMessage().getFrom().getId());
            statement.setLastCommand("/deletelist");
            sendMessage("Select list which you want to delete.", update.getMessage().getChatId());
        }
        if (message.toLowerCase().contains("/renamelist")){
            UserStatement statement = getUserStatement(update.getMessage().getFrom().getId());
            statement.setLastCommand("/renamelist");
            sendMessage("Select list which you want to rename.", update.getMessage().getChatId());
            //sendMessage("Enter new title.", update.getMessage().getChatId());
        }
        if (message.toLowerCase().contains("/showtasks")){
            UserStatement statement = getUserStatement(update.getMessage().getFrom().getId());
            sendMessage("Select list which you want to see.", update.getMessage().getChatId());
            //showTasks(statement.getSelectedList(), update.getMessage().getChatId());
        }
        if (message.toLowerCase().contains("/addtask")){
            UserStatement statement = getUserStatement(update.getMessage().getFrom().getId());
            statement.setLastCommand("/addtask");
            sendMessage("Enter new task.", update.getMessage().getChatId());
        }
        if (message.toLowerCase().contains("/deletetask")){
            UserStatement statement = getUserStatement(update.getMessage().getFrom().getId());
            statement.setLastCommand("/deletetask");
            sendMessage("Select task which you want to delete.", update.getMessage().getChatId());
        }
        if (message.toLowerCase().contains("/changetask")){
            UserStatement statement = getUserStatement(update.getMessage().getFrom().getId());
            statement.setLastCommand("/changetask");
            sendMessage("Select task which you want to change.", update.getMessage().getChatId());
        }
        if (message.toLowerCase().contains("/changetaskstatus")){
            UserStatement statement = getUserStatement(update.getMessage().getFrom().getId());
            statement.setLastCommand("/changetaskstatus");
            sendMessage("Enter new task`s status.", update.getMessage().getChatId());
        }
    }

    private void checkUserStatement(Update update){
        User user = update.getMessage().getFrom();
        UserStatement statement = getUserStatement(user.getId());

        if(statement.getLastCommand().equalsIgnoreCase("/addlist")){
            addNewTaskList(user.getId(), update.getMessage().getText());
        }
        if(statement.getLastCommand().equalsIgnoreCase("/deletelist")){
            deleteTaskList(Integer.parseInt(update.getMessage().getText()));
        }
        if(statement.getLastCommand().equalsIgnoreCase("/renamelist")){
            if(statement.getSelectedList() != -1){
                changeTaskListName(statement.getSelectedList(), update.getMessage().getText(), user.getId());
                statement.setSelectedList(-1);
                statement.setLastCommand("");
            }
            statement.setSelectedList(Integer.parseInt(update.getMessage().getText()));
            sendMessage("Enter new title.", update.getMessage().getChatId());
        }

        if(statement.getLastCommand().equalsIgnoreCase("/addtask")){
            addNewTask(statement.getSelectedList(), update.getMessage().getText());
        }
        if(statement.getLastCommand().equalsIgnoreCase("/deletetask")){
            deleteTask(Integer.parseInt(update.getMessage().getText()));
        }
        if(statement.getLastCommand().equalsIgnoreCase("/changetask")){
            if(statement.getSelectedTask() != -1){
                changeTaskText(statement.getSelectedTask(), update.getMessage().getText());
                statement.setSelectedTask(-1);
                statement.setLastCommand("");
            }
            statement.setSelectedTask(Integer.parseInt(update.getMessage().getText()));
            sendMessage("Enter new title.", update.getMessage().getChatId());
        }
        if(statement.getLastCommand().equalsIgnoreCase("/changetaskstatus")){
            if(statement.getSelectedTask() != -1){
                boolean status;
                if (update.getMessage().getText() == "+") {
                    status = true;
                }
                else status = false;
                changeTaskStatus(statement.getSelectedTask(), status);
                statement.setSelectedTask(-1);
                statement.setLastCommand("");
            }
            statement.setSelectedTask(Integer.parseInt(update.getMessage().getText()));
            sendMessage("Enter new status.", update.getMessage().getChatId());
        }
        if(statement.getLastCommand().equalsIgnoreCase("/showtasks")){
            statement.setSelectedList(Integer.parseInt(update.getMessage().getText()));
            showTasks(statement.getSelectedList(), update.getMessage().getChatId());
        }
    }

    private UserStatement getUserStatement(Integer user_id){
        return userStatementMap.get(user_id);
    }

    private void sendMessage(String responseMsg, Long chat_id){
        SendMessage response = new SendMessage()
                .setChatId(chat_id)
                .setText(responseMsg);
        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}