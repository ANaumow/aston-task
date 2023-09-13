package ru.company.app.accounts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.company.app.common.domain.JpaEntity;

import java.util.UUID;

@Entity(name = "account")
@Setter
@Getter
public class Account extends JpaEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "money", nullable = false)
    private Long money;

    @Column(name = "pin", nullable = false)
    private Integer pin;

}
