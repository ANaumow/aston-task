package ru.company.app.accounts.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.company.app.accounts.domain.Account;
import ru.company.app.accounts.dto.*;
import ru.company.app.accounts.repository.AccountRepository;
import ru.company.app.common.service.Mapper;
import ru.company.app.common.util.exception.BusinessLogicException;

import java.util.List;
import java.util.UUID;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.longThat;

@SpringBootTest
class AccountServiceImplTest {

    @Autowired
    AccountService accountService;

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    Mapper mapper;

    @Mock
    Account mockedAccount;

    UUID accountId = UUID.randomUUID();

    Integer accountPin = 1234;

    Long accountMoney = 300L;

    @BeforeEach
    void beforeEach() {
        when(mockedAccount.getId()).thenReturn(accountId);
        when(mockedAccount.getMoney()).thenReturn(accountMoney);
        when(mockedAccount.getPin()).thenReturn(accountPin);
        when(accountRepository.findById(accountId)).thenReturn(of(mockedAccount));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void When_CreateAccount_Expect_RepositoryWasInvoked() {
        String expectedName = "Andrew";
        int expectedPin = 1234;

        NewAccountRequest newAccountRequest = new NewAccountRequest(expectedName, expectedPin);

        accountService.createAccount(newAccountRequest);

        verify(accountRepository).save(assertArg(account -> {
            assertThat(account.getName()).isEqualTo(expectedName);
            assertThat(account.getPin()).isEqualTo(expectedPin);
        }));
    }

    @Test
    void When_GetAllAccounts_Expect_RepositoryAndMapperWasInvoked() {
        List<Account> mockedAccounts = mock(List.class);
        when(accountRepository.findAll()).thenReturn(mockedAccounts);

        List<AccountDto> mockedAccountDtoList = mock(List.class);
        when(mapper.mapAsList(mockedAccounts, AccountDto.class)).thenReturn(mockedAccountDtoList);

        assertThat(accountService.getAllAccounts()).isEqualTo(mockedAccountDtoList);
    }

    @Test
    void When_Deposit_Expect_MoneyToBeIncreased() {
        DepositRequest depositRequest = new DepositRequest(accountId, 100L);

        accountService.deposit(depositRequest);

        verify(mockedAccount).setMoney(longThat(comparesEqualTo(accountMoney + 100L)));
    }

    @Test
    void When_WithdrawWithCorrectPin_Expect_MoneyToBeDecreased() {
        WithdrawRequest withdrawRequest = new WithdrawRequest(accountId, 100L, accountPin);

        accountService.withdraw(withdrawRequest);

        verify(mockedAccount).setMoney(longThat(comparesEqualTo(accountMoney - 100L)));
    }

    @Test
    void When_WithdrawWithIncorrectPin_Expect_MoneyNotToBeChangedAndExceptionToBeThrown() {
        int accountIncorrectPin = accountPin + 1;
        assertThatThrownBy(() -> {
            WithdrawRequest withdrawRequest = new WithdrawRequest(accountId, 100L, accountIncorrectPin);
            accountService.withdraw(withdrawRequest);
        }).isInstanceOf(BusinessLogicException.class);

        verify(mockedAccount, never()).setMoney(anyLong());
    }

    @Test
    void When_TransferWithCorrectPin_Expect_MoneyTransfer() {
        UUID destinationId = UUID.randomUUID();
        long destinationMoney = 600L;
        Account destinationAccount = mock(Account.class);
        when(destinationAccount.getId()).thenReturn(destinationId);
        when(destinationAccount.getMoney()).thenReturn(destinationMoney);
        when(accountRepository.findById(destinationId)).thenReturn(of(destinationAccount));

        long transferAmount = 100L;
        accountService.transfer(new TransferRequest(accountId, accountPin, transferAmount, destinationId));

        verify(mockedAccount).setMoney(longThat(comparesEqualTo(accountMoney - transferAmount)));
        verify(destinationAccount).setMoney(longThat(comparesEqualTo(destinationMoney + transferAmount)));
    }

    @Test
    void When_TransferWithIncorrectPin_Expect_MoneyNotToBeChangedAndExceptionToBeThrown() {
        UUID destinationId = UUID.randomUUID();
        Long destinationMoney = 600L;
        Account destinationAccount = mock(Account.class);
        when(destinationAccount.getId()).thenReturn(destinationId);
        when(destinationAccount.getMoney()).thenReturn(destinationMoney);
        when(accountRepository.findById(destinationId)).thenReturn(of(destinationAccount));

        long transferAmount = 100L;
        int accountIncorrectPin = accountPin + 1;
        assertThatThrownBy(() -> {
            TransferRequest transferRequest = new TransferRequest(accountId, accountIncorrectPin, transferAmount, destinationId);
            accountService.transfer(transferRequest);
        }).isInstanceOf(BusinessLogicException.class);

        verify(mockedAccount, never()).setMoney(anyLong());
        verify(destinationAccount, never()).setMoney(anyLong());
    }
}
