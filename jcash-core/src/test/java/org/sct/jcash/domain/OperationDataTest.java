package org.sct.jcash.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class OperationDataTest {

    @Test
    public void creation() {
        LocalDate now = LocalDate.now();

        OperationData data = OperationData.of(
                now,
                -109.50,
                new Classifier[] {Classifier.EMPTY_CLASSIFIER},
                true
        );

        Assertions.assertEquals(now, data.getDate());
        Assertions.assertEquals(-109.50, data.getAmount());
        Assertions.assertEquals(1, data.getClassifiers().length);
        Assertions.assertTrue(data.isCommitted());
    }

    @Test
    public void createOpposite() {
        LocalDate now = LocalDate.now();

        OperationData data = OperationData.of(
                now,
                -109.50,
                new Classifier[] {Classifier.EMPTY_CLASSIFIER},
                true
        );

        OperationData oppositeData = OperationData.oppositeOf(data);

        Assertions.assertEquals(now, oppositeData.getDate());
        Assertions.assertEquals(109.50, oppositeData.getAmount());
        Assertions.assertEquals(1, oppositeData.getClassifiers().length);
        Assertions.assertTrue(oppositeData.isCommitted());
    }

    @Test
    public void oppositeToZero() {
        OperationData oppositeData = OperationData.oppositeOf(
                OperationData.of(
                        LocalDate.now(),
                        0,
                        new Classifier[] {Classifier.EMPTY_CLASSIFIER},
                        true
        ));

        Assertions.assertEquals(0, oppositeData.getAmount());
    }



}
