package dbService;

import dbService.dao.UsersDAO;
import dbService.models.User;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService {
    private static DBService dbService = new DBService();
    private final Connection connection;

    private DBService() {
        this.connection = getH2Connection();
    }

    public User getUser(int id) throws SQLException {
        try {
            return (new UsersDAO(connection).get(id));
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public static DBService getDbService() {
        return dbService;
    }

    public void saveCmd(Cmds cmd, int sCmd) throws SQLException {
        connection.setAutoCommit(true);
        switch (cmd) {
            case PING:
                UsersDAO dao = new UsersDAO(connection);
                dao.saveCmd(sCmd);
                dao.getAll();
                break;
            default:
                break;
        }
        connection.commit();
    }


    private static Connection getH2Connection() {
        try {
            String url = "jdbc:h2:~/h2db";

            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(url);

            Connection connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
