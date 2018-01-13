package bean;

public class Task {

    private int id;
    private int listId;
    private String taskDescription;
    private boolean taskStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public boolean getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return taskDescription +
                " - " +
                (taskStatus?"\t\\xE2\\x9C\\x94":"\\xE2\\x9C\\x96") +
                '\n';
    }
}