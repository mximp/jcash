package org.sct.jcash.domain;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Classifier {
    private final String classifierString;

    public static Classifier of(String classifierString) {
        return new Classifier(classifierString);
    }

    public Classifier(String classifierString) {

        this.classifierString = normalizeClassifierString(classifierString);
    }

    @Override
    public String toString() {
        return classifierString;
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
