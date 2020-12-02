package org.sct.jcash.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class OperationTest {

    @Test
    public void createExpenseOperation() {

        var date = LocalDateTime.now();
        var amount = 100.0;
        var account = "AC#1";
        var sign = -1;

        FinOperation expense = new FinOperation(amount, date, account, sign);

        Assertions.assertEquals(amount, expense.getAmount());
        Assertions.assertEquals(date, expense.getDate());
        Assertions.assertEquals(account, expense.getAccount());
        Assertions.assertEquals(sign, expense.getSign());
    }

}
