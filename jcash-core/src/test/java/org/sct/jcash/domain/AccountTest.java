package org.sct.jcash.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
        // todo do we need child hierarchy here? maybe create separate structure
        Account parentAccount = sampleRURAccount();
        Account childAccount = sampleRURAccount();

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

        // Accounts are equal by ID

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
        Account account = sampleRURAccount();

        account.addOperation(operation("2020-01-20T10:15:30", 101.2432));
        account.addOperation(operation("2020-01-20T10:15:31", 201.2342));

        Assertions.assertEquals(302.4774, account.getBalance());
        Assertions.assertEquals(302.4774, account.getBalance(timestamp("2021-01-20T10:15:31")));
        Assertions.assertEquals(302.4774, account.getBalance(timestamp("2020-01-20T10:15:31")));
        Assertions.assertEquals(101.2432, account.getBalance(timestamp("2020-01-20T10:15:30")));
        Assertions.assertEquals(0, account.getBalance(timestamp("2020-01-20T10:15:29")));

    }

    @Test
    public void operations() {
        Account acc = sampleRURAccount();

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
    public void operationCcy() {
        Account acc = sampleRURAccount();

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> acc.addOperation(operation("2020-07-01T10:15:30", "USD")));
    }

    @Test
    public void rename() {
        Account account = sampleRURAccount();

        account.rename("acc1");

        Assertions.assertEquals("acc1", account.getName());
    }

    @Test
    public void close() {
        Account account = sampleRURAccount();

        account.close();

        Assertions.assertTrue(account.isClosed());
    }

    public static AccountOperation operation(String timestamp) {
        return operation(timestamp, 1.0);
    }

    public static AccountOperation operation(String timestamp, double amount) {
        return operation(timestamp, RUB_CCY, amount);
    }

    public static AccountOperation operation(String timestamp, String ccy) {
        return operation(timestamp, ccy, 1.0);
    }

    public static AccountOperation operation(String timestamp, String ccy, double amount) {
        return new AccountOperation() {
            @Override
            public LocalDateTime getOperationDate() {
                return timestamp(timestamp);
            }

            @Override
            public double getAmount() {
                return amount;
            }

            @Override
            public String getCcy() {return ccy; }
        };
    }

    public static LocalDateTime timestamp(String timestamp) {
        return LocalDateTime.parse(timestamp);
    }

    public static Account sampleRURAccount() {
        return Account.of("");
    }

}
