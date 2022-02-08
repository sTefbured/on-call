package com.stefbured.oncallserver.utils;

import java.util.ArrayList;
import java.util.List;

public class SqlScriptSplitter {
    private boolean isString;
    private boolean isMultiLineComment;
    private boolean isSingleLineComment;
    private boolean isMultiLineCommentStart;

    public List<String> split(String sqlScript) {
        var outputQueries = new ArrayList<String>();
        int currentIndex = 0;
        while (currentIndex < sqlScript.length()) {
            var stringBuilder = new StringBuilder();
            sqlScript = sqlScript.substring(currentIndex);
            for (currentIndex = 0; currentIndex < sqlScript.length(); currentIndex++) {
                checkStringCase(sqlScript, currentIndex);
                checkMultiLineCommentCase(sqlScript, currentIndex);
                checkSingleLineCommentCase(sqlScript, currentIndex);
                if (sqlScript.charAt(currentIndex) == ';' && !(isString || isMultiLineComment || isSingleLineComment)) {
                    break;
                }
                stringBuilder.append(sqlScript.charAt(currentIndex));
            }
            outputQueries.add(stringBuilder.toString());
            currentIndex++;
        }
        return outputQueries;
    }

    private void checkStringCase(String sqlScript, int index) {
        if (isMultiLineComment || isSingleLineComment) {
            return;
        }
        if (sqlScript.charAt(index) == '\'') {
            isString = !isString;
        }
    }

    private void checkMultiLineCommentCase(String sqlScript, int index) {
        if (isSingleLineComment || isString) {
            return;
        }
        var currentChar = sqlScript.charAt(index);
        if (isMultiLineCommentStart && currentChar == '*') {
            isMultiLineCommentStart = false;
            return;
        }
        if (isMultiLineComment && !isMultiLineCommentStart
                && currentChar == '/' && sqlScript.charAt(index - 1) == '*') {
            isMultiLineComment = false;
            return;
        }
        if (currentChar == '/' && index + 1 < sqlScript.length() && sqlScript.charAt(index + 1) == '*') {
            isMultiLineComment = true;
            isMultiLineCommentStart = true;
        }
    }

    private void checkSingleLineCommentCase(String sqlScript, int index) {
        if (isString || isMultiLineComment) {
            return;
        }
        var currentChar = sqlScript.charAt(index);
        if (isSingleLineComment && currentChar == '\n') {
            isSingleLineComment = false;
            return;
        }
        if (!isSingleLineComment && index + 1 < sqlScript.length()
                && ((currentChar == '/' && sqlScript.charAt(index + 1) == '/')
                || (currentChar == '-' && sqlScript.charAt(index + 1) == '-'))) {
            isSingleLineComment = true;
        }
    }
}
