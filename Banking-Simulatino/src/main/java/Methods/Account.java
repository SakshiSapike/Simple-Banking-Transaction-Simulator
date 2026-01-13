package Methods;
import java.util.*;
import Observers.BalanceObserver;
import java.util.ArrayList;
import java.util.List;
import Exception.InsufficientFundsException;

public class Account {

    private String accountId;
    private String name;
    private double balance;
    private String email;
    private String mobile;
    private AccountType type;
    private boolean isActive = true;
    private boolean isFrozen = false;
    private int failedCount = 0;
    private List<BalanceObserver> observers = new ArrayList<>();
    public void addObserver(BalanceObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (BalanceObserver o : observers) {
            o.update(this);
        }
    }

    public Account(String name, AccountType type, double balance,
                   String email, String mobile) {

        if (balance < 0)
            throw new ArithmeticException("Negative balance");

        if (!email.contains("@"))
            throw new IllegalArgumentException("Invalid email");

        if (!mobile.matches("\\d{10}"))
            throw new IllegalArgumentException("Invalid mobile number");

        this.accountId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.email = email;
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    // deposit
    public void credit(double amount) {
        if (!isActive)
            throw new IllegalStateException("Account inactive");

        balance += amount;
        notifyObservers();
    }

    // withdraw
    public void debit(double amount) {
        if (!isActive || isFrozen)
            throw new IllegalStateException("Account inactive or frozen");
        if (amount <= 0 || balance < amount)
            throw new ArithmeticException("Invalid or insufficient balance");
        balance -= amount;
        notifyObservers();
    }

    // transfer
    public void transfer(Account to, double amount)
            throws InsufficientFundsException {

        if (amount > balance)
            throw new InsufficientFundsException("Insufficient funds");

        this.balance -= amount;
        to.balance += amount;

        notifyObservers();      // sender
        to.notifyObservers();   // receiver
    }


    public void markFailure() {
        failedCount++;
        if (failedCount >= 3) isFrozen = true;
    }

    public void markSuccess() {
        failedCount = 0;
    }

    public void activate() { isActive = true; }
    public void deactivate() { isActive = false; }

    public String getId() { return accountId; }
    public double getBalance() { return balance; }
    public String getName() { return name; }


    @Override
    public String toString() {
        return "ID=" + accountId +
                ", Name=" + name +
                ", Type=" + type +
                ", Balance=" + balance +
                ", Email=" + email +
                ", Mobile=" + mobile +
                ", Active=" + isActive +
                ", Frozen=" + isFrozen;
    }

}
