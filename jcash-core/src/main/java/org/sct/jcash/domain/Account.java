package org.sct.jcash.domain;

import java.time.LocalDate;
import java.util.*;

public class Account {
    public static final String RUB_CCY = "RUB";

//  todo: make a separate class/enum for ccy
    private final String ccy;
    private final String name;

    private final List<Account> children;
    private final NavigableMap<LocalDate, Double> balance;
    private boolean closed;

    public Account(String name, String ccy) {
        this.name = name;
        this.ccy = ccy;
        this.closed = false;
        this.children = new ArrayList<>();
        this.balance = new TreeMap<>();
    }

    public static Account of(String name, String ccy) {
        return new Account(name, ccy);
    }

    public static Account of(String name) {
        return of(name, RUB_CCY);
    }

    public static Account of(String name, String ccy, double initAmount) {
        Account account = of(name, ccy);
        account.setBalance(LocalDate.now(), initAmount);
        return account;
    }

    public String getCcy() {
        return ccy;
    }

    public double getBalance(LocalDate date) {
        if (balance.containsKey(date)) {
            return balance.get(date);
        }

        NavigableSet<LocalDate> dates = (NavigableSet<LocalDate>) balance.keySet();

        LocalDate closestDate = dates.stream().filter(d -> d.isBefore(date)).findFirst().orElse(null);

        if(closestDate != null) {
            return balance.get(closestDate);
        }

        return 0;
    }

    public double getBalance() {
        return getBalance(LocalDate.now());
    }

    public List<Account> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean addChild(Account childAccount) {
        children.add(childAccount);
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return closed == account.closed &&
                Objects.equals(ccy, account.ccy) &&
                Objects.equals(name, account.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ccy, name);
    }

    public String getName() {
        return name;
    }

    public void setBalance(LocalDate date, double amount) {
        this.balance.put(date, amount);
    }

    public void close() {
        this.closed = true;
    }


}
