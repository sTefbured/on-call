package com.stefbured.oncallserver.utils;

import com.stefbured.oncallserver.model.dto.user.UserRegisterDTO;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class OnCallEntityValidator {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("\\w{5,20}");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
    private static final Pattern EMAIL_LOCAL_PART_PATTERN = Pattern.compile("^.+@.+$");

    private static final long MIN_USER_AGE = 14L;

    private OnCallEntityValidator() {
    }

    public static boolean isValid(UserRegisterDTO userRegisterDTO) {
        return USERNAME_PATTERN.matcher(userRegisterDTO.getUsername()).matches()
                && PASSWORD_PATTERN.matcher(userRegisterDTO.getPassword()).matches()
                && isEmailValid(userRegisterDTO.getEmail())
                && isNameValid(userRegisterDTO.getFirstName())
                && isNameValid(userRegisterDTO.getLastName())
                && isBirthDateValid(userRegisterDTO.getBirthDate());
    }

    private static boolean isBirthDateValid(LocalDateTime birthDate) {
        return birthDate.isBefore(LocalDateTime.now().minusYears(MIN_USER_AGE));
    }

    private static boolean isNameValid(String name) {
        if (name == null) {
            return true;
        }
        for (var character : name.toCharArray()) {
            if (!Character.isAlphabetic(character)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isEmailValid(String email) {
        return EMAIL_LOCAL_PART_PATTERN.matcher(email).matches();
    }
}
