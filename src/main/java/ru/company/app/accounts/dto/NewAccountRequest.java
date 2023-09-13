package ru.company.app.accounts.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.company.app.common.util.constraints.Pin;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewAccountRequest {

    @NotBlank
    private String name;

    @Pin
    private Integer pin;

}
