package com.kakaopay.assignment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Getter
@Entity
@NoArgsConstructor
@Table(name = "USER")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Version
    private long version;

    private long balance;

    public void deductAmount(long amount) {
        balance -= amount;
    }

    public void addAmount(long amount) {
        balance += amount;
    }
}
