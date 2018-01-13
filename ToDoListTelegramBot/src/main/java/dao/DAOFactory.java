package dao;

import dao.mySqlDAO.MySqlTaskDAO;
import dao.mySqlDAO.MySqlTaskListDAO;
import dao.mySqlDAO.MySqlUserDAO;

public class DAOFactory {
    private enum DbType {MySQL}

    private static DbType type = DbType.MySQL;

    public static TaskDAO getTaskDao() {
        switch (type) {
            case MySQL:
                return new MySqlTaskDAO();
            default:
                return null;
        }
    }

    public static TaskListDAO getTaskListDao() {
        switch (type) {
            case MySQL:
                return new MySqlTaskListDAO();
            default:
                return null;
        }
    }

    public static UserDAO getUserDao() {
        switch (type){
            case MySQL:
                return new MySqlUserDAO();
            default:
                return null;
        }
    }
}