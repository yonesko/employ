package main.dao;

import main.model.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcCustomerDAO implements CustomerDAO {
    private static final Logger log = LoggerFactory.getLogger(JdbcCustomerDAO.class);

    public static final String INSERT_CUSTOMER = "INSERT INTO customers(login, password, reg_ts, balance) VALUES (?,?,?,?)";
    public static final String SELECT_ALL = "SELECT login, password, reg_ts, balance FROM customers";
    public static final String SELECT_BY_LOGIN = "SELECT login, password, reg_ts, balance FROM customers WHERE login = ?";

    public static CustomerRowMapper rowMapper = new CustomerRowMapper();


    JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Customer> getAll() {
        return jdbcTemplate.query(
                SELECT_ALL,
                new CustomerRowMapper());
    }

    public void insertCustomer(Customer customer) {
        jdbcTemplate.update(INSERT_CUSTOMER,
                customer.getLogin(),
                customer.getPassword(),
                customer.getRegTs(),
                customer.getBalance());
    }


    public Customer getCustomer(String login) {
        List<Customer> l = jdbcTemplate.query(SELECT_BY_LOGIN, rowMapper, login);

        if (l.size() > 0)
            return l.get(0);
        return null;
    }

    @Override
    public void reCreateTable() {
        dropTale();

        log.info("Creating tables");

        jdbcTemplate.execute("CREATE TABLE customers(" +
                "login VARCHAR2(50) PRIMARY KEY, " +
                "password VARCHAR2(50), " +
                "reg_ts TIMESTAMP, " +
                "balance DECIMAL(20, 2))");
    }

    @Override
    public void dropTale() {
        log.info("Dropping tables");

        jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
    }

    @Override
    public void truncateTable() {
        log.info("Truncating tables");

        jdbcTemplate.execute("TRUNCATE TABLE customers");
    }

    private static class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Customer(
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getTimestamp("reg_ts"),
                    rs.getBigDecimal("balance"));

        }
    }
}
