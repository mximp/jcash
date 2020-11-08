package org.sct.jcash.domain;

import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classifier domain object
 */
public class Classifier {
    public static final Classifier EMPTY_CLASSIFIER = new Classifier("");
    private final String classifierString;

    public static Classifier of(String classifierString) {
        String normalizedString = normalizeClassifierString(classifierString);

        if(normalizedString.equals("")) {
            return EMPTY_CLASSIFIER;
        }

        return new Classifier(normalizedString);
    }

    private Classifier(String classifierString) {
        this.classifierString = classifierString;
    }

    @Override
    public String toString() {
        return classifierString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classifier that = (Classifier) o;
        return Objects.equals(classifierString, that.classifierString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classifierString);
    }

    public Classifier getParent() {
        long count = new Scanner(classifierString).findAll("\\.").count();
        String subClassifierString = Stream.of(classifierString
                .split("\\."))
                .limit(count)
                .collect(Collectors.joining("."));

        return new Classifier(subClassifierString);
    }

    private static String normalizeClassifierString(String classifierString) {
        return classifierString
                .replaceAll("^\\.+", "")
                .replaceAll("\\.+$", "")
                .replaceAll("\\.+", ".")
                .trim();
    }
}
