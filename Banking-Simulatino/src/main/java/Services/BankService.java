package Services;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import Observers.BalanceAlertTracker;

import Methods.Account;
import Methods.AccountType;

import java.util.*;

public class BankService {

    private Map<String, Account> accounts = new HashMap<>();
    private List<Transaction> transactions = new ArrayList<>();
    private Map<String, Map<java.time.LocalDate, DailyStats>> dailyMap = new HashMap<>();
    private static final String LOG_FILE =
            System.getProperty("user.dir") + "/data/transactions.log";


    private void writeTransactionToFile(Transaction t) {
        try {
            File file = new File(LOG_FILE);
            file.getParentFile().mkdirs(); // create data folder if not exists

            try (FileWriter fw = new FileWriter(file, true)) {
                fw.write(
                        LocalDateTime.now() + " | " +
                                t.accountId + " | " +
                                t.type + " | " +
                                t.amount + " | " +
                                t.status +
                                (t.reason != null ? " | " + t.reason : "") +
                                "\n"
                );
            }
            //  DEBUG: shows exact file location
            System.out.println("Log written to: " + file.getAbsolutePath());

        } catch (IOException e) {
            System.out.println(" Transaction log write failed");
            e.printStackTrace(); // optional but helpful
        }
    }


    public Account createAccount(String name, AccountType type, double bal,
                                 String email, String mobile) {

        Account acc = new Account(name, type, bal, email, mobile);
        acc.addObserver(new BalanceAlertTracker());
        accounts.put(acc.getId(), acc);
        return acc;
    }



    public Account getAccount(String id) {
        Account acc = accounts.get(id);
        if (acc == null)
            throw new NoSuchElementException("Account not found");
        return acc;
    }

    public void activateAccount(String id) { getAccount(id).activate(); }
    public void deactivateAccount(String id) { getAccount(id).deactivate(); }

    public void transfer(String fromId, String toId, double amount) {

        Account from = accounts.get(fromId);
        Account to = accounts.get(toId);


        if (from == null || to == null) {
            Transaction tx = new Transaction(
                    fromId,
                    TransactionType.TRANSFER_OUT,
                    amount,
                    TransactionStatus.FAILED,
                    "Account missing"
            );

            transactions.add(tx);
            writeTransactionToFile(tx);   //  FILE LOG

            System.out.println("Transfer Failed: Account not found");
            return;
        }

        try {
            if (fromId.equals(toId))
                throw new IllegalArgumentException("Self transfer not allowed");

            if (!BankSystem.checkLimit(dailyMap, fromId, amount))
                throw new IllegalStateException("Daily limit exceeded");

            //  BUSINESS LOGIC
            from.transfer(to, amount);
            from.markSuccess();
            BankSystem.updateLimit(dailyMap, fromId, amount);

            // SUCCESS TRANSACTIONS
            Transaction txOut = new Transaction(
                    fromId,
                    TransactionType.TRANSFER_OUT,
                    amount,
                    TransactionStatus.SUCCESS,
                    null
            );

            Transaction txIn = new Transaction(
                    toId,
                    TransactionType.TRANSFER_IN,
                    amount,
                    TransactionStatus.SUCCESS,
                    null
            );

            transactions.add(txOut);
            transactions.add(txIn);

            writeTransactionToFile(txOut);   //  REQUIRED
            writeTransactionToFile(txIn);    //  REQUIRED

            System.out.println("\nTransfer Successful!");
            System.out.println("Remaining Balance: " + from.getBalance());

        } catch (Exception e) {

            from.markFailure();


            Transaction txFail = new Transaction(
                    fromId,
                    TransactionType.TRANSFER_OUT,
                    amount,
                    TransactionStatus.FAILED,
                    e.getMessage()
            );

            transactions.add(txFail);
            writeTransactionToFile(txFail);   //  FILE LOG

            System.out.println("Transfer Failed: " + e.getMessage());
        }
    }


    public void showAccountTransactions(String id) {
        transactions.stream()
                .filter(t -> t.accountId.equals(id))
                .forEach(System.out::println);
    }

    public void listAccounts() {
        accounts.values().forEach(System.out::println);
    }

    // MAIN (UI)
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BankService bank = new BankService();

        while (true) {
            System.out.println("\n1.Create 2.Transfer 3.Profile 4.List 5.Exit");
            int c = Integer.parseInt(sc.nextLine());

            switch (c) {
                case 1:
                    System.out.print("Name: ");
                    String name = sc.nextLine();

                    System.out.print("Account Type (SAVINGS or CURRENT): ");
                    AccountType type = AccountType.valueOf(
                            sc.nextLine().trim().toUpperCase()
                    );

                    System.out.print("Initial Balance: ");
                    double bal = Double.parseDouble(sc.nextLine());

                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    System.out.print("Mobile (10 digits): ");
                    String mobile = sc.nextLine();

                    Account acc = bank.createAccount(name, type, bal, email, mobile);
                    System.out.println(" Account Created Successfully!");
                    System.out.println(acc);
                    break;


                case 2:
                    System.out.print("From (Account ID): ");
                    String f = sc.nextLine();
                    System.out.print("To (Account ID): ");
                    String t = sc.nextLine();
                    System.out.print("Amount: ");
                    double amt = Double.parseDouble(sc.nextLine());
                    bank.transfer(f, t, amt);
                    break;

                case 3:
                    System.out.print("Account ID: ");
                    String id = sc.nextLine();
                    System.out.println("1.Activate 2.Deactivate 3.History 4.Balance");
                    int p = Integer.parseInt(sc.nextLine());
                    if (p == 1) bank.activateAccount(id);
                    else if (p == 2) bank.deactivateAccount(id);
                    else if (p == 3) bank.showAccountTransactions(id);
                    else if (p == 4)
                        System.out.println(bank.getAccount(id).getBalance());
                    break;

                case 4:
                    bank.listAccounts();
                    break;

                case 5:
                    return;
            }
        }
    }
}
