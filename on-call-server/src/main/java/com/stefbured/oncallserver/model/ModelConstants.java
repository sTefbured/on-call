package com.stefbured.oncallserver.model;

public class ModelConstants {
    public static class User {
        public static final int MAX_USERNAME_LENGTH = 30;
        public static final int MIN_USERNAME_LENGTH = 4;
        public static final int MAX_PASSWORD_LENGTH = 100;
        public static final int MIN_PASSWORD_LENGTH = 8;
        public static final int MAX_EMAIL_LENGTH = 320;
        public static final int MAX_NAME_LENGTH = 50;
        public static final String USERNAME_VALIDATION_REGEX = "\\w{5,20}";
        public static final String PASSWORD_VALIDATION_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]$";
        public static final String EMAIL_VALIDATION_REGEX = "^[^@]+@[^@]+$";

        public static final String USERNAME_VALIDATION_ERROR_MESSAGE = "Username must only consist of latin letters and numbers and its length must be in range from 5 to 20";
        public static final String USERNAME_LENGTH_ERROR_MESSAGE = "Username length must be between " + MIN_USERNAME_LENGTH + " and " + MAX_USERNAME_LENGTH;
        public static final String PASSWORD_LENGTH_ERROR_MESSAGE = "Password length must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH;
        public static final String PASSWORD_VALIDATION_ERROR_MESSAGE = "Password must only consist of latin characters and at least one number";
        public static final String EMAIL_LENGTH_ERROR_MESSAGE = "Max email length is " + MAX_EMAIL_LENGTH;
        public static final String EMAIL_VALIDATION_ERROR_MESSAGE = "Email must consist of local and domain parts separated by a single '@' character";
        public static final String FIRST_NAME_LENGTH_ERROR_MESSAGE = "Max first name length is " + MAX_NAME_LENGTH;
        public static final String LAST_NAME_LENGTH_ERROR_MESSAGE = "Max last name length is " + MAX_NAME_LENGTH;
        public static final String REGISTRATION_DATE_VALIDATION_ERROR_MESSAGE = "Registration date must be equal to current or past date";
        public static final String BIRTH_DATE_IN_FUTURE_ERROR_MESSAGE = "Birth date must be a past date";
        public static final String LAST_VISIT_DATE_VALIDATION_ERROR_MESSAGE = "Last visit date must be equal to current or past date";

        private User() {
        }
    }

    public static class DatabaseQuery {
        public static final int MAX_SQL_QUERY_LENGTH = 1000;

        public static final String SQL_QUERY_LENGTH_ERROR_MESSAGE = "Query is too long. Max length: " + MAX_SQL_QUERY_LENGTH;

        private DatabaseQuery() {
        }
    }

    public static class Role {
        public static final int MAX_ROLE_NAME_LENGTH = 30;
        public static final int MAX_ROLE_DESCRIPTION_LENGTH = 120;

        public static final String ROLE_NAME_LENGTH_ERROR_MESSAGE = "Max role name length is " + MAX_ROLE_NAME_LENGTH;
        public static final String ROLE_DESCRIPTION_LENGTH_ERROR_MESSAGE = "Max role description length is " + MAX_ROLE_DESCRIPTION_LENGTH;

        private Role() {
        }
    }

    public static class UserGroup {
        public static final int MAX_USER_GROUP_ID_TAG_LENGTH = 32;
        public static final int MIN_USER_GROUP_NAME_LENGTH = 5;
        public static final int MAX_USER_GROUP_NAME_LENGTH = 32;
        public static final int MAX_USER_GROUP_DESCRIPTION_LENGTH = 120;

        public static final String USER_GROUP_ID_TAG_LENGTH_ERROR_MESSAGE = "Max id tag length is " + MAX_USER_GROUP_ID_TAG_LENGTH;
        public static final String USER_GROUP_NAME_LENGTH_ERROR_MESSAGE = "Length of user group name must be in range from " + MIN_USER_GROUP_NAME_LENGTH + " to " + MAX_USER_GROUP_NAME_LENGTH;
        public static final String USER_GROUP_DESCRIPTION_LENGTH_ERROR_MESSAGE = "Max length of user group description is" + MAX_USER_GROUP_DESCRIPTION_LENGTH;
        public static final String CREATION_DATE_VALIDATION_ERROR_MESSAGE = "Group creation date must be equal to current or past date";

        private UserGroup() {
        }
    }

    public static class Permission {
        public static final int MAX_PERMISSION_NAME_LENGTH = 10;
        public static final int MAX_PERMISSION_DESCRIPTION_LENGTH = 10;

        private Permission() {
        }
    }

    private ModelConstants() {
    }
}
