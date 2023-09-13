package ru.company.app.accounts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.company.app.accounts.dto.*;
import ru.company.app.accounts.service.AccountService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/accounts")
    public List<AccountDto> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping("/accounts")
    public UUID createAccount(@Valid @RequestBody NewAccountRequest newAccountRequest) {
        return accountService.createAccount(newAccountRequest);
    }

    @PostMapping("/accounts/deposit")
    public void deposit(@Valid @RequestBody DepositRequest depositRequest) {
        accountService.deposit(depositRequest);
    }

    @PostMapping("/accounts/withdraw")
    public void withdraw(@Valid @RequestBody WithdrawRequest withdrawRequest) {
        accountService.withdraw(withdrawRequest);
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@Valid @RequestBody TransferRequest transferRequest) {
        accountService.transfer(transferRequest);
    }

}
