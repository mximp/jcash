package org.sct.jcash.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class OperationTest {

    @Test
    public void createOperation() {
        // given
        var date = LocalDateTime.now();
        var amount = -100.0;
        var account = "AC#1";

        // when
        FinOperation expense = FinOperation.of(amount, date, account);

        // then
        System.out.println(expense);
        Assertions.assertEquals(amount, expense.getAmount());
        Assertions.assertEquals(date, expense.getDate());
        Assertions.assertEquals(account, expense.getAccount());
    }

    @Test
    public void assignClassifiers() {
        var expense = FinOperation.of(-100.0, "AC#1");
        var classifiers = Set.of("A", "B");

        expense.classify(classifiers);

        Assertions.assertTrue(Set.of("B", "A").containsAll(expense.getClassifiers()));
    }

    @Test
    public void assignAdditionalClassifiers() {
        var expense = FinOperation.of(-100.0, "AC#1");
        expense.classify(Set.of("A")); // already has classifiers

        expense.classify(Set.of("A", "C", "B"));

        Assertions.assertTrue(expense.getClassifiers().containsAll(Set.of("A", "B", "C")));
        Assertions.assertEquals(3, expense.getClassifiers().size());
    }

    @Test
    public void removeClassifier() {
        var expense = FinOperation.of(-100.0, "AC#1");
        expense.classify(List.of("A", "B", "C"));

        expense.removeClassifier("B");

        Assertions.assertTrue(expense.getClassifiers().containsAll(Set.of("A", "C")));
        Assertions.assertFalse(expense.getClassifiers().contains("B"));
    }

    @Test
    public void removeNonExistingClassifier() {
        var expense = FinOperation.of(-100.0, "AC#1");
        expense.classify(List.of("A", "B", "C"));

        expense.removeClassifier("D");

        Assertions.assertTrue(expense.getClassifiers().containsAll(Set.of("A", "B", "C")));
    }

    @Test
    public void missingArguments() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> FinOperation.of(100, null, "AC#1")
        );

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> FinOperation.of(100, LocalDateTime.now(), null)
        );
    }

}
