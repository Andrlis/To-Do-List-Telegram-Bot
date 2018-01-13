package bot;

import bean.User;

public class UserStatement {

    private User user;
    private String lastCommand;
    private int selectedList;

    public UserStatement(){
        user = new User();
        lastCommand = null;
        selectedList = -1;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLastCommand() {
        return lastCommand;
    }

    public void setLastCommand(String lastCommand) {
        this.lastCommand = lastCommand;
    }

    public int getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(int selectedList) {
        this.selectedList = selectedList;
    }
}