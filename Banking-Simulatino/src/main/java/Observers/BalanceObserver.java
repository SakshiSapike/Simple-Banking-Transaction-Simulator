package Observers;

import Methods.Account;

public interface BalanceObserver {

    void update(Account account);

}