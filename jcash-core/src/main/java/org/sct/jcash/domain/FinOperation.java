package org.sct.jcash.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FinOperation {
    private final double amount;
    private final LocalDateTime date;
    private final String account;
    private final Set<String> classifiers = new HashSet<>();


    private FinOperation(double amount, LocalDateTime date, String account) {
        this.amount = amount;
        this.date = date;
        this.account = account;
    }

    public static FinOperation of(double amount, LocalDateTime date, String account) {
        if(date == null) {
            throw new IllegalArgumentException("Operation date must be provided");
        }

        if(account == null) {
            throw new IllegalArgumentException("Operation account must be provided");
        }

        return new FinOperation(amount, date, account);
    }

    public static FinOperation of(double amount, String account) {
        return of(amount, LocalDateTime.now(), account);
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

    @Override
    public String toString() {
        return "FinOperation{" +
                "amount=" + amount +
                ", date=" + date +
                ", account='" + account + '\'' +
                ", classifiers=" + classifiers +
                '}';
    }

    public void classify(Collection<String> classifiers) {
        this.classifiers.addAll(classifiers);
    }

    public Set<String> getClassifiers() {
        return Collections.unmodifiableSet(classifiers);
    }

    public void removeClassifier(String classifier) {
        this.classifiers.remove(classifier);
    }
}
