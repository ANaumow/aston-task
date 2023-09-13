package ru.company.app.accounts.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.company.app.accounts.dto.AccountDto;
import ru.company.app.accounts.dto.NewAccountRequest;
import ru.company.app.accounts.dto.WithdrawRequest;
import ru.company.app.accounts.service.AccountService;
import ru.company.app.common.util.exception.BusinessLogicException;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.company.app.test.Utils.jsonFrom;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    void When_CreateAccount_Expect_UuidToBeReturned() throws Exception {
        UUID expectedId = UUID.randomUUID();
        AccountDto mockedAccountDto = mock(AccountDto.class);
        when(mockedAccountDto.getId()).thenReturn(expectedId);
        when(accountService.createAccount(any())).thenReturn(expectedId);

        this.mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFrom(new NewAccountRequest("Andrew", 1234))))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonFrom(expectedId)));
    }

    @Test
    void When_DepositWithBadPin_Expect_BadRequest() throws Exception {
        this.mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFrom(new NewAccountRequest("Andrew", 12345))))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void When_Exception_Expect_ResponseWithErrorCode() throws Exception {
        doThrow(new BusinessLogicException("error.account.withdraw.insufficientFunds"))
                .when(accountService).withdraw(any());

        this.mockMvc.perform(post("/accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFrom(WithdrawRequest.builder()
                                .accountId(UUID.randomUUID())
                                .amount(100L)
                                .pin(1234)
                                .build())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("cause")));
    }



}
