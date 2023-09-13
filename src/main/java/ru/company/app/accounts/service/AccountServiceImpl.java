package ru.company.app.accounts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.company.app.accounts.domain.Account;
import ru.company.app.accounts.dto.*;
import ru.company.app.accounts.repository.AccountRepository;
import ru.company.app.common.util.exception.BusinessLogicException;
import ru.company.app.common.service.Mapper;

import java.util.List;
import java.util.UUID;

import static ru.company.app.common.util.BusinessAssert.check;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final Mapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return mapper.mapAsList(accounts, AccountDto.class);
    }

    @Override
    @Transactional
    public UUID createAccount(NewAccountRequest newAccountRequest) {
        Account account = new Account();
        account.setName(newAccountRequest.getName());
        account.setPin(newAccountRequest.getPin());
        account.setMoney(0L);
        accountRepository.save(account);
        return account.getId();
    }

    private Account findAccountById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessLogicException("account not found"));
    }

    @Override
    @Transactional
    public void deposit(DepositRequest depositRequest) {
        Account account = findAccountById(depositRequest.getAccountId());
        doDeposit(account, depositRequest.getAmount());
    }

    private void doDeposit(Account account, Long amount) {
        Long prev = account.getMoney();
        Long post = prev + amount;
        account.setMoney(post);
    }

    @Override
    @Transactional
    public void withdraw(WithdrawRequest withdrawRequest) {
        Account account = findAccountById(withdrawRequest.getAccountId());
        doWithdraw(account, withdrawRequest.getAmount(), withdrawRequest.getPin());
    }

    private void doWithdraw(Account account, Long amount, Integer pin) {
        check(account.getPin().equals(pin), "error.account.incorrectPin");
        Long prev = account.getMoney();
        long post = prev - amount;
        check(post > 0, "error.account.withdraw.insufficientFunds");
        account.setMoney(post);
    }

    @Override
    @Transactional
    public void transfer(TransferRequest transferRequest) {
        Account sourceAccount = findAccountById(transferRequest.getSourceAccountId());
        Account destinationAccount = findAccountById(transferRequest.getDestinationAccountId());
        Long amount = transferRequest.getAmount();

        doWithdraw(sourceAccount, amount, transferRequest.getSourcePin());
        doDeposit(destinationAccount, amount);
    }

}
