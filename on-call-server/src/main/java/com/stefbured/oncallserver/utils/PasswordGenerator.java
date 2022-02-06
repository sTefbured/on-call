package com.stefbured.oncallserver.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class PasswordGenerator {
    private static final int DEFAULT_MIN_LOWER_LETTERS_COUNT = 2;
    private static final int DEFAULT_MIN_UPPER_LETTERS_COUNT = 2;
    private static final int DEFAULT_MIN_NUMBERS_COUNT = 1;

    private final Random random;

    public PasswordGenerator() {
        random = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    public String generate(int length, int minUpperLettersCount, int minLowerLettersCount, int minNumbersCount) {
        checkBoundsValidity(length, minUpperLettersCount, minLowerLettersCount, minNumbersCount);
        int bound = length - minLowerLettersCount - minNumbersCount + 1;
        int upperLettersCount = random.nextInt(minUpperLettersCount, bound);
        bound = length - upperLettersCount - minNumbersCount + 1;
        int lowerLettersCount = random.nextInt(minLowerLettersCount, bound);
        int numbersCount = length - upperLettersCount - lowerLettersCount;

        ArrayList<Character> passwordParts = new ArrayList<>(length);
        for (int i = 0; i < upperLettersCount; i++) {
            var codePoint = random.nextInt('A', 'z');
            passwordParts.add((char) codePoint);
        }
        for (int i = 0; i < numbersCount; i++) {
            var codePoint = random.nextInt('0', '9');
            passwordParts.add((char) codePoint);
        }
        Collections.shuffle(passwordParts);
        return passwordParts.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    public String generate(int length) {
        return generate(length, DEFAULT_MIN_UPPER_LETTERS_COUNT, DEFAULT_MIN_LOWER_LETTERS_COUNT, DEFAULT_MIN_NUMBERS_COUNT);
    }

    private void checkBoundsValidity(int length, int minUpperLettersCount, int minLowerLettersCount, int minNumbersCount) {
        int boundsSum = minUpperLettersCount + minLowerLettersCount + minNumbersCount;
        if (length < boundsSum) {
            throw new IllegalArgumentException("Provided length is too small");
        }
        if (minUpperLettersCount < 0 || minLowerLettersCount < 0 || minNumbersCount < 0) {
            throw new IllegalArgumentException("Count must be non-negative value only");
        }
    }
}