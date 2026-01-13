package Observers;

import Methods.Account;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class BalanceAlertTracker implements BalanceObserver {

    private static final double MIN_BALANCE = 100;
    private static final String LOG_FILE =
            System.getProperty("user.dir") + "/data/transactions.log";

    @Override
    public void update(Account account) {

        if (account.getBalance() < MIN_BALANCE) {

            //  Console alert (already working)
            String message =
                    "ALERT: Account balance is below minimum limit. " +
                            "Current Balance: " + account.getBalance();

            System.out.println(message);


            writeAlertToFile(account, message);
        }
    }

    private void writeAlertToFile(Account account, String message) {
        try {
            File file = new File(LOG_FILE);
            file.getParentFile().mkdirs();

            try (FileWriter fw = new FileWriter(file, true)) {
                fw.write(
                        LocalDateTime.now() + " | ALERT | Account=" +
                                account.getId() + " | " +
                                message + "\n"
                );
            }
        } catch (IOException e) {
            System.out.println("Alert log write failed");
        }
    }
}
