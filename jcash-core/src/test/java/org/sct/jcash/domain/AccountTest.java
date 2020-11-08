package org.sct.jcash.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.sct.jcash.domain.Account.RUB_CCY;

public class AccountTest {

    @Test
    public void construction() {
        Account account = Account.of("Account name", RUB_CCY);
        Assertions.assertNotNull(account);

        Assertions.assertFalse(account.getId().isBlank());
        Assertions.assertEquals("Account name", account.getName());
        Assertions.assertEquals(RUB_CCY, account.getCcy());
        Assertions.assertEquals(0.0, account.getBalance());
        Assertions.assertEquals(0, account.getChildren().size());
        Assertions.assertFalse(account.isClosed());

        account = Account.of("a name");
        Assertions.assertEquals("a name", account.getName());
        Assertions.assertEquals(RUB_CCY, account.getCcy());

        account = Account.of("account_with_balance", RUB_CCY, 103.5);
        Assertions.assertEquals(103.5, account.getBalance());

        account = Account.of("id", "name", "USD");
        Assertions.assertEquals("id", account.getId());
        Assertions.assertEquals("name", account.getName());
        Assertions.assertEquals("USD", account.getCcy());
    }

    @Test
    public void parentChildRelation() {
        Account parentAccount = Account.of("");
        Account childAccount = Account.of("");

        boolean added = parentAccount.addChild(childAccount);

        Assertions.assertTrue(added);
        Assertions.assertEquals(1, parentAccount.getChildren().size());
        Assertions.assertEquals(childAccount, parentAccount.getChildren().get(0));
    }

    @Test
    public void equality() {

        // equality by id only

        Assertions.assertEquals(
                Account.of("1", "acc1", "EUR"),
                Account.of("1", "acc2", "USD"));

        Assertions.assertNotEquals(
                Account.of("1", "acc1", "EUR"),
                Account.of("2", "acc1", "EUR"));

        Assertions.assertNotEquals(
                Account.of(""),
                Account.of(""));

        Assertions.assertNotEquals(
                Account.of(""),
                Account.of("1"));

        Assertions.assertNotEquals(
                Account.of(""),
                Account.of("", "USD"));


    }

    @Test
    public void balance() {
        Account acc = Account.of("acc");

        LocalDate date = LocalDate.now();
        double amount = 100.15;
        acc.setBalance(date, amount);

        double balance = acc.getBalance(date);
        Assertions.assertEquals(amount, balance);

        Assertions.assertEquals(0.0, acc.getBalance(date.minus(1, ChronoUnit.DAYS)));
        Assertions.assertEquals(amount, acc.getBalance(date.plus(5, ChronoUnit.DAYS)));
    }

    @Test
    public void addOperation() {

    }

    @Test
    public void rename() {
        Account account = Account.of("");

        account.rename("acc1");

        Assertions.assertEquals("acc1", account.getName());
    }

    @Test
    public void close() {
        Account account = Account.of("");

        account.close();

        Assertions.assertTrue(account.isClosed());
    }
}
