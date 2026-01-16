package RestService;

import Methods.Account;
import Methods.AccountType;
import Services.BankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BankController {

    private final BankService bankService = new BankService();

    // 1 CREATE ACCOUNT
    @PostMapping("/accounts")
    public Account createAccount(
            @RequestParam String name,
            @RequestParam AccountType type,
            @RequestParam double balance,
            @RequestParam String email,
            @RequestParam String mobile
    ) {
        return bankService.createAccount(name, type, balance, email, mobile);
    }

    // 2 GET ACCOUNT BY ID
    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable String id) {
        return bankService.getAccount(id);
    }

    // 3 GET BALANCE
    @GetMapping("/accounts/{id}/balance")
    public double getBalance(@PathVariable String id) {
        return bankService.getAccount(id).getBalance();
    }

    // 4ACTIVATE ACCOUNT
    @PostMapping("/accounts/{id}/activate")
    public String activate(@PathVariable String id) {
        bankService.activateAccount(id);
        return " Account activated successfully";
    }

    // 5 DEACTIVATE ACCOUNT
    @PostMapping("/accounts/{id}/deactivate")
    public String deactivate(@PathVariable String id) {
        bankService.deactivateAccount(id);
        return " Account deactivated successfully";
    }

    // 6 TRANSFER MONEY
    @PostMapping("/transfer")
    public String transfer(
            @RequestParam String fromId,
            @RequestParam String toId,
            @RequestParam double amount
    ) {
        bankService.transfer(fromId, toId, amount);
        return " Transfer request processed";
    }

    // 7 GET TRANSACTION HISTORY (console-based but exposed)
    @GetMapping("/accounts/{id}/transactions")
    public String showTransactions(@PathVariable String id) {
        bankService.showAccountTransactions(id);
        return " Transactions printed in server console";
    }

    // 8 LIST ALL ACCOUNTS
    @GetMapping("/accounts")
    public String listAccounts() {
        bankService.listAccounts();
        return " Accounts printed in server console";
    }
}
