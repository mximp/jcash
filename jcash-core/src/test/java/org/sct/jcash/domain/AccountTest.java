package org.sct.jcash.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    public void addChild() {
        Account parentAccount = Account.of("");
        Account childAccount = Account.of("");

        boolean added = parentAccount.addChild(childAccount);

        Assertions.assertTrue(added);
        Assertions.assertEquals(1, parentAccount.getChildren().size());
        Assertions.assertEquals(childAccount, parentAccount.getChildren().get(0));

        // cannot add the same account twice
        Assertions.assertFalse(parentAccount.addChild(childAccount));
        Assertions.assertEquals(1, parentAccount.getChildren().size());

        // cannot add null child
        Assertions.assertThrows(IllegalArgumentException.class, () -> parentAccount.addChild(null));
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
        // todo move balance api into a separate class BalanceCalculator (maybe related to an account)
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
    public void operationsTest() {
        Account acc = Account.of("");

        Collection<AccountOperation> operations = acc.getOperations();

        // returned collection is unmodifiable
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> operations.add(operation("2020-08-01T10:15:30")));

        // adding operations
        acc.addOperation(operation("2020-08-01T10:15:30"));
        acc.addOperation(operation("2020-09-01T10:15:30"));
        acc.addOperation(operation("2020-07-01T10:15:30"));
        acc.addOperation(operation("2020-07-01T10:15:31"));

        Assertions.assertEquals(4, operations.size());

        // operations are stored ordered by date/time
        Assertions.assertIterableEquals(
                List.of(timestamp("2020-07-01T10:15:30"),
                        timestamp("2020-07-01T10:15:31"),
                        timestamp("2020-08-01T10:15:30"),
                        timestamp("2020-09-01T10:15:30")),
                operations.stream().map(AccountOperation::getOperationDate).collect(Collectors.toList()));

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

    private static AccountOperation operation(String timestamp) {
        return new AccountOperation() {
            @Override
            public LocalDateTime getOperationDate() {
                return timestamp(timestamp);
            }
        };
    }

    private static LocalDateTime timestamp(String timestamp) {
        return LocalDateTime.parse(timestamp);
    }

}
