package dbService.dao;

import dbService.Cmds;
import dbService.models.User;
import dbService.executor.Executor;
import dbService.executor.ResultHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {

    private Executor executor;

    public UsersDAO(Connection connection) {
        this.executor = new Executor(connection);
        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User get(int id) throws SQLException {
        return executor.execQuery("select * from users where id=" + id, new ResultHandler<User>() {
            public User handle(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("cmd"),
                        resultSet.getInt("cmd_received"));
            }
        });
    }

    public void getAll() throws SQLException {
        executor.execQuery("select * from users", new ResultHandler<User>() {
            public User handle(ResultSet resultSet) throws SQLException {
                System.out.println("id, cmd, cmd_received");
                while (resultSet.next())
                    System.out.println(String.format("%d %s %d",
                            resultSet.getInt("id"),
                            resultSet.getString("cmd"),
                            resultSet.getInt("cmd_received")));
                return null;
            }
        });
    }

    public void createTable() throws SQLException {
        executor.execUpdate("create table if not exists users (id bigint, cmd varchar(256), cmd_received int, primary key (id))");
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("drop table users");
    }

    public void saveCmd(int id) throws SQLException {
        int upded = executor.execUpdate(String.format("UPDATE users SET\n" +
                "cmd_received = cmd_received + 1\n" +
                "WHERE\n" +
                "id = %d\n" +
                "AND cmd = '%s'", id, Cmds.PING));
        if (upded == 0)
            executor.execUpdate(String.format("insert into users (id, cmd, cmd_received) values (%d, '%s', 1)", id, Cmds.PING));
    }
}
