package ru.company.app.common.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.company.app.accounts.domain.Account;
import ru.company.app.accounts.dto.AccountDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MapperFacadeTest {

    @Autowired
    Mapper mapper;

    @Test
    void When_Map_Expect_CorrectMapping() {
        Account account = new Account();
        account.setName("Andrew");
        account.setMoney(999L);

        AccountDto expectedDto = AccountDto.builder()
                .name("Andrew")
                .money(999L)
                .build();
        AccountDto actualDto = mapper.map(account, AccountDto.class);

        assertThat(actualDto)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedDto);
    }

    @Test
    void When_MapAsList_Expect_CorrectMapping() {
        Account account = new Account();
        account.setName("Andrew");
        account.setMoney(999L);
        List<Account> accounts = List.of(account);
        List<AccountDto> accountDtoList = mapper.mapAsList(accounts, AccountDto.class);

        AccountDto expectedDto = AccountDto.builder()
                .name("Andrew")
                .money(999L)
                .build();

        assertThat(accountDtoList)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedDto);
    }

}
