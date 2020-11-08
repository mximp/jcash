package org.sct.jcash.domain;

import java.time.LocalDate;

public class OperationData {
    private final LocalDate date;
    private final double amount;
    private final Classifier[] classifiers;
    private final boolean committed;

    public OperationData(LocalDate date, double amount, Classifier[] classifiers, boolean committed) {
        this.date = date;
        this.amount = amount;
        this.classifiers = classifiers;
        this.committed = committed;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public Classifier[] getClassifiers() {
        return classifiers;
    }

    public boolean isCommitted() {
        return committed;
    }

    /**
     * Create operation data with reverted sign of the amount
     *
     * @param data base operation
     * @return OperationData with reverted sign of the amount
     */
    public static OperationData oppositeOf(OperationData data) {
        return of(
                data.date,
                data.amount == 0 ? 0 : -data.amount,
                data.classifiers,
                data.committed);
    }

    public static OperationData of(LocalDate date, double amount, Classifier[] classifiers, boolean committed) {
        return new OperationData(date, amount, classifiers, committed);
    }


}
