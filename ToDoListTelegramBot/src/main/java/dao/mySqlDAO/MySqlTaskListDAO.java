package dao.mySqlDAO;

import bean.TaskList;
import dao.DAOException;
import dao.TaskListDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlTaskListDAO extends AbstractMySqlDAO implements TaskListDAO {

    private final static String GET_ALL_QUERY =
            "SELECT * FROM to_do_bot.to_do_lists WHERE user_id = ?";
    private final static String GET_BY_ID_QUERY =
            "SELECT * FROM to_do_bot.to_do_lists WHERE list_id = ?";
    private final static String SAVE_QUERY =
            "INSERT INTO to_do_bot.to_do_lists (user_id, list_name)"
                    + " VALUES (?, ?)";
    private final static String UPDATE_QUERY =
            "UPDATE to_do_bot.to_do_list SET user_id = ?, list_name = ?"
                    + " WHERE list_id = ?";
    private final static String REMOVE_QUERY =
            "DELETE FROM to_do_bot.users WHERE list_id = ?";

    @Override
    public TaskList getTaskListByID(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        TaskList taskList = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_BY_ID_QUERY);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                taskList = new TaskList();
                taskList.setId(resultSet.getInt("list_id"));
                taskList.setUserId(Integer.parseInt(resultSet.getString("user_id")));
                taskList.setListName(resultSet.getString("list_name"));
            }
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            closeDB(connection, statement, resultSet);
        }

        return taskList;
    }

    @Override
    public List<TaskList> getTaskListsByUserID(int user_id) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<TaskList> taskLists = new ArrayList<TaskList>();

        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_ALL_QUERY);
            statement.setInt(1, user_id);
            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                TaskList taskList = new TaskList();
                taskList.setId(resultSet.getInt("list_id"));
                taskList.setUserId(Integer.parseInt(resultSet.getString("user_id")));
                taskList.setListName(resultSet.getString("list_name"));

                taskLists.add(taskList);
            }
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            closeDB(connection, statement, resultSet);
        }

        return taskLists;
    }

    @Override
    public void saveTaskList(TaskList taskList) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(SAVE_QUERY);
            statement.setInt(1, taskList.getUserId());
            statement.setString(2, taskList.getListName());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            closeDB(connection, statement);
        }
    }

    @Override
    public void updateTaskList(int id, TaskList taskList) throws DAOException{
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setInt(1, taskList.getUserId());
            statement.setString(2, taskList.getListName());
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            closeDB(connection, statement);
        }
    }

    @Override
    public void removeTaskList(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(REMOVE_QUERY);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            closeDB(connection, statement);
        }
    }
}