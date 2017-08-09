package main.model.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Customer {
    private String login;
    private String password;
    private Timestamp regTs;
    private BigDecimal balance;


    public Customer(String login, String password, Timestamp regTs, BigDecimal balance) {
        this.login = login;
        this.password = password;
        this.regTs = regTs;
        this.balance = balance;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getRegTs() {
        return regTs;
    }

    public void setRegTs(Timestamp regTs) {
        this.regTs = regTs;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Customer{");
        sb.append("login='").append(login).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", regTs=").append(regTs);
        sb.append(", balance=").append(balance);
        sb.append('}');
        return sb.toString();
    }
}