package org.sct.jcash.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class ClassifierTest {

    @Test
    public void create() {
        Classifier classifier = Classifier.of("A.B.C");
        Assertions.assertEquals("A.B.C", classifier.toString());
    }

    @Test
    public void parentChild() {
        assertParentChild("A.B", "A.B.C");
        assertParentChild("A", "A.B");
        assertParentChild("", "A");
        assertParentChild("", "A.");
        assertParentChild("", "A.....");
        assertParentChild("", ".A");
        assertParentChild("", "....A");
        assertParentChild("", ".....");
        assertParentChild("", "");
        assertParentChild("A.B", "...A...B...C....");

        assertNotParentChild("", "A.B");
        assertNotParentChild("A", "A.B.C");
        assertNotParentChild("A.B.C", "A.B.C");
    }

    @Test
    public void emptyClassifier() {
        Assertions.assertEquals("", Classifier.EMPTY_CLASSIFIER.toString());

        Stream.of("", ".", "....")
                .forEach(ClassifierTest::assertEmptyClassifier);
    }

    private static void assertEmptyClassifier(String s) {
        Assertions.assertSame(Classifier.EMPTY_CLASSIFIER, Classifier.of(s));
    }

    private static void assertParentChild(String parent, String child) {
        Assertions.assertEquals(Classifier.of(parent), Classifier.of(child).getParent());
    }

    private static void assertNotParentChild(String parent, String child) {
        Assertions.assertNotEquals(Classifier.of(parent), Classifier.of(child).getParent());
    }


}
