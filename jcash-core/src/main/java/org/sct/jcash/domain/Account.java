package org.sct.jcash.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Account {
    public static final String RUB_CCY = "RUB";

//  todo: make a separate class/enum for ccy
    private final String ccy;
    private final String id;

    private String name;

    private final List<Account> children;

    private boolean closed;
    private final Set<AccountOperation> operations = new TreeSet<>(Comparator.comparing(AccountOperation::getOperationDate));

    private Account(String id, String name, String ccy) {
        this.name = name;
        this.ccy = ccy;
        this.closed = false;
        this.children = new ArrayList<>();
        this.id = id;
    }

    /**
     * UUID string is used as account id by default
     */
    public static Account of(String name, String ccy) {
        return of(UUID.randomUUID().toString(), name, ccy);
    }

    public static Account of(String id, String name, String ccy) {
        return new Account(id, name, ccy);
    }

    /**
     * RUB is used as default ccy
     */
    public static Account of(String name) {
        return of(name, RUB_CCY);
    }

    public static Account of(String name, String ccy, double initAmount) {
        Account account = of(name, ccy);

        LocalDateTime timestamp = LocalDateTime.now();
        account.addOperation(new AccountOperation() {
            @Override
            public LocalDateTime getOperationDate() {
                return timestamp;
            }

            @Override
            public double getAmount() {
                return initAmount;
            }
        });
        return account;
    }

    public String getCcy() {
        return ccy;
    }

    /**
     * Balance which reflect all account operations up until specified timestamp inclusive
     */
    public double getBalance(LocalDateTime timestamp) {
        return operations.stream()
                .filter(o -> o.getOperationDate().isBefore(timestamp) || o.getOperationDate().isEqual(timestamp))
                .mapToDouble(AccountOperation::getAmount)
                .sum();
    }

    public double getBalance() {
        return getBalance(LocalDateTime.now());
    }

    public List<Account> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean addChild(Account childAccount) {
        if(childAccount == null) {
            throw new IllegalArgumentException("Trying to add null child account");
        }

        if(!children.isEmpty() && children.contains(childAccount)) {
            return false;
        }

        children.add(childAccount);
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getName() {
        return name;
    }

    public void close() {
        this.closed = true;
    }


    public String getId() {
        return id;
    }

    public void rename(String newName) {
        this.name = newName;
    }

    public Collection<AccountOperation> getOperations() {
        return Collections.unmodifiableCollection(operations);
    }

    public void addOperation(AccountOperation accountOperation) {
        operations.add(accountOperation);
    }
}
