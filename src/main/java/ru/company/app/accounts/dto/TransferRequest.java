package ru.company.app.accounts.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.company.app.common.util.constraints.Pin;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {

    @NotNull
    private UUID sourceAccountId;

    @Pin
    private Integer sourcePin;

    @NotNull
    @Positive
    private Long amount;

    @NotNull
    private UUID destinationAccountId;

}
