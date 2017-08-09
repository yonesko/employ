package main.dao;

import main.model.domain.Customer;

import java.util.List;

public interface CustomerDAO {
    void insertCustomer(Customer customer);
    List<Customer> getAll();
    Customer getCustomer(String login);
    void reCreateTable();
    void dropTale();
    void truncateTable();
}
