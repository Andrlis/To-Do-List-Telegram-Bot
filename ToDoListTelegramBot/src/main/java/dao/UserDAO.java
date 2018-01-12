package dao;

import bean.User;
import java.util.List;

public interface UserDAO {

    User getUserByID(int id) throws DAOException;
    User getUserByTelegramID(int telegram_id) throws DAOException;
    List<User> getAllUsers() throws DAOException;
    void saveUser(User user) throws DAOException;
    void removeUser(int id) throws DAOException;
}