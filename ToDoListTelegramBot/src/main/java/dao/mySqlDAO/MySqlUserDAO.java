package dao.mySqlDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.User;
import dao.DAOException;
import dao.UserDAO;

public class MySqlUserDAO extends AbstractMySqlDAO implements UserDAO {

    private final static String GET_ALL_QUERY =
            "SELECT * FROM to_do_bot.users";
    private final static String GET_BY_ID_QUERY =
            "SELECT * FROM to_do_bot.users WHERE user_id = ?";
    private final static String SAVE_QUERY =
            "INSERT INTO to_do_bot.users (user_id, telegram_id)"
                    + " VALUES (?, ?)";
    private final static String REMOVE_QUERY =
            "DELETE FROM to_do_bot.users WHERE user_id = ?";

    @Override
    public User getUserByID(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        User user = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_BY_ID_QUERY);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("user_id"));
                user.setTelegramId(resultSet.getInt("telegram_id"));
            }
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            closeDB(connection, statement, resultSet);
        }

        return user;
    }

    @Override
    public List<User> getAllUsers() throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<User> users = new ArrayList<User>();

        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_ALL_QUERY);
            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("user_id"));
                user.setTelegramId(resultSet.getInt("telegram_id"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            closeDB(connection, statement, resultSet);
        }

        return users;
    }

    @Override
    public void saveUser(User user) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(SAVE_QUERY);
            statement.setInt(1, user.getId());
            statement.setInt(2, user.getTelegramId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            closeDB(connection, statement);
        }
    }

    @Override
    public void removeUser(int id) throws DAOException {
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