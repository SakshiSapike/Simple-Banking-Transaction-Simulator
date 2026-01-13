package Services;
import java.time.*;
import java.util.*;

enum TransactionType {  TRANSFER_IN, TRANSFER_OUT }
enum TransactionStatus { SUCCESS, FAILED }

class Transaction {
    String transactionId = UUID.randomUUID().toString();
    String accountId;
    TransactionType type;
    double amount;
    LocalDateTime time = LocalDateTime.now();
    TransactionStatus status;
    String reason;

    public Transaction(String accountId, TransactionType type,
                       double amount, TransactionStatus status, String reason) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return time + " | " + type + " | " +
                amount + " | " + status +
                (reason != null ? " | " + reason : "");
    }
}

class DailyStats {
    int count = 0;
    double totalAmount = 0;
}

public class BankSystem {

    public static final int MAX_TXN = 5;
    public static final double MAX_AMOUNT = 100000;

    public static boolean checkLimit(
            Map<String, Map<LocalDate, DailyStats>> dailyMap,
            String id, double amount) {

        LocalDate today = LocalDate.now();
        dailyMap.putIfAbsent(id, new HashMap<>());
        dailyMap.get(id).putIfAbsent(today, new DailyStats());

        DailyStats ds = dailyMap.get(id).get(today);
        return ds.count < MAX_TXN && ds.totalAmount + amount <= MAX_AMOUNT;
    }

    public static void updateLimit(
            Map<String, Map<LocalDate, DailyStats>> dailyMap,
            String id, double amount) {

        DailyStats ds = dailyMap.get(id).get(LocalDate.now());
        ds.count++;
        ds.totalAmount += amount;
    }
}
