package org.sct.jcash.domain;

import java.time.LocalDateTime;

public class FinOperation {
    private final double amount;
    private final LocalDateTime date;
    private String account;
    private int sign;

    public FinOperation(double amount, LocalDateTime date, String account, int sign) {
        this.amount = amount;
        this.date = date;
        this.account = account;
        this.sign = sign;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getAccount() {
        return account;
    }

    public int getSign() {
        return sign;
    }
}
