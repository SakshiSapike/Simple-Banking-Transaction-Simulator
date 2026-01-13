package RestService;

import Methods.Account;
import Methods.AccountType;
import Services.BankService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BankController {

    private final BankService bankService = new BankService();

    // 1️⃣ CREATE ACCOUNT

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

    // 2️⃣ TRANSFER MONEY
    @PostMapping("/transfer")
    public String transfer(
            @RequestParam String fromId,
            @RequestParam String toId,
            @RequestParam double amount
    ) {
        bankService.transfer(fromId, toId, amount);
        return "Transfer request processed";
    }

    // 3️⃣ GET BALANCE
    @GetMapping("/accounts/{id}/balance")
    public double getBalance(@PathVariable String id) {
        return bankService.getAccount(id).getBalance();
    }
}
