package ru.company.app.accounts.service;

import ru.company.app.accounts.dto.*;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountDto> getAllAccounts();

    UUID createAccount(NewAccountRequest newAccountRequest);

    void deposit(DepositRequest depositRequest);

    void withdraw(WithdrawRequest withdrawRequest);

    void transfer(TransferRequest transferRequest);
}
