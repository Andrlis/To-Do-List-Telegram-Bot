package dao;

import bean.Task;
import java.util.List;

public interface TaskDAO {
    Task getTaskByID(int id) throws DAOException;
    List<Task> getTasksByListID(int list_id) throws DAOException;
    void saveTask(Task user) throws DAOException;
    void updateTask(int id, Task task) throws DAOException;
    void removeTask(int id) throws DAOException;
}