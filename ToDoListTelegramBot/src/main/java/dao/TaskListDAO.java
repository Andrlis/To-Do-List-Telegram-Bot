package dao;

import bean.TaskList;
import java.util.List;

public interface TaskListDAO {
    TaskList getTaskListByID(int id) throws DAOException;
    List<TaskList> getAllTaskLists() throws DAOException;
    void saveTaskList(TaskList user) throws DAOException;
    void updateTaskList(int id, TaskList taskList) throws DAOException;
    void removeTaskList(int id) throws DAOException;
}