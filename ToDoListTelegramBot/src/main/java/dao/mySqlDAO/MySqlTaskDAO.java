package dao.mySqlDAO;

import bean.Task;
import dao.DAOException;
import dao.TaskDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlTaskDAO extends AbstractMySqlDAO implements TaskDAO  {

    private final static String GET_ALL_QUERY =
            "SELECT * FROM to_do_bot.lits_items WHERE list_id = ?";
    private final static String GET_BY_ID_QUERY =
            "SELECT * FROM to_do_bot.lits_items WHERE item_id = ?";
    private final static String SAVE_QUERY =
            "INSERT INTO to_do_bot.list_items (list_id, item_description, item_status)"
                    + " VALUES (?, ?, ?)";
    private final static String UPDATE_QUERY =
            "UPDATE to_do_bot.lits_items SET list_id = ?, item_description = ?, item_status = ?"
                    + " WHERE item_id = ?";
    private final static String REMOVE_QUERY =
            "DELETE FROM to_do_bot.lits_items WHERE item_id = ?";

    @Override
    public Task getTaskByID(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        Task task = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_BY_ID_QUERY);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                task = new Task();
                task.setId(resultSet.getInt("item_id"));
                task.setListId(Integer.parseInt(resultSet.getString("list_id")));
                task.setTaskDescription(resultSet.getString("item_description"));
                task.setTaskStatus(resultSet.getBoolean("item_status"));
            }
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            closeDB(connection, statement, resultSet);
        }

        return task;
    }

    @Override
    public List<Task> getTasksByListID(int list_id) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<Task> tasks = new ArrayList<Task>();

        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_ALL_QUERY);
            statement.setInt(1, list_id);
            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Task task = new Task();
                task.setId(resultSet.getInt("item_id"));
                task.setListId(Integer.parseInt(resultSet.getString("list_id")));
                task.setTaskDescription(resultSet.getString("item_description"));
                task.setTaskStatus(resultSet.getBoolean("item_status"));

                tasks.add(task);
            }
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            closeDB(connection, statement, resultSet);
        }

        return tasks;
    }

    @Override
    public void saveTask(Task task) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(SAVE_QUERY);
            statement.setInt(1, task.getListId());
            statement.setString(2, task.getTaskDescription());
            statement.setBoolean(3, task.getTaskStatus());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            closeDB(connection, statement);
        }
    }

    @Override
    public void updateTask(int id, Task task) throws DAOException{
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setInt(1, task.getListId());
            statement.setString(2, task.getTaskDescription());
            statement.setBoolean(3, task.getTaskStatus());
            statement.setInt(4, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            closeDB(connection, statement);
        }
    }

    @Override
    public void removeTask(int id) throws DAOException {
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