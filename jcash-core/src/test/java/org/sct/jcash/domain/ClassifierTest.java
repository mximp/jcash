package org.sct.jcash.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClassifierTest {

    @Test
    public void create() {
        Classifier classifier = Classifier.of("A.B.C");

        Assertions.assertEquals("A.B.C", classifier.toString());
    }

    @Test
    public void getParent() {
        Assertions.assertEquals("A.B", Classifier.of("A.B.C").getParent().toString());
        Assertions.assertEquals("A", Classifier.of("A.B").getParent().toString());
        Assertions.assertEquals("", Classifier.of("A").getParent().toString());
        Assertions.assertEquals("", Classifier.of("A.").getParent().toString());
        Assertions.assertEquals("", Classifier.of("A.....").getParent().toString());
        Assertions.assertEquals("", Classifier.of(".A").getParent().toString());
        Assertions.assertEquals("", Classifier.of("....A").getParent().toString());
        Assertions.assertEquals("", Classifier.of(".....").getParent().toString());
        Assertions.assertEquals("", Classifier.of("").getParent().toString());
        Assertions.assertEquals("A.B", Classifier.of("...A...B...C....").getParent().toString());
    }

}
