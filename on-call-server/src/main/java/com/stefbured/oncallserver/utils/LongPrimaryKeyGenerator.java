package com.stefbured.oncallserver.utils;

import com.stefbured.oncallserver.config.SpringContextHolder;
import com.stefbured.oncallserver.exception.primarykey.PrimaryKeyGenerationException;
import com.stefbured.oncallserver.exception.primarykey.PrimaryKeyGenerationTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
public class LongPrimaryKeyGenerator {
    private static final String REPOSITORY_PACKAGE = "com.stefbured.oncallserver.repository.";
    private static final String REPOSITORY_STRING = "Repository";
    private static final long MAX_ID_VALUE = 9007199254740992L; // added due to JS limit of 2^53
    private static final long GENERATION_TIMEOUT = 5000L;

    private final Random random;

    @Autowired
    public LongPrimaryKeyGenerator() {
        random = new Random(System.currentTimeMillis());
    }

    public <T> long generatePk(Class<T> clazz) {
        long primaryKey;
        long start = System.currentTimeMillis();
        do {
            primaryKey = random.nextLong(0, MAX_ID_VALUE) + 1;
            if (System.currentTimeMillis() - start > GENERATION_TIMEOUT) {
                throw new PrimaryKeyGenerationTimeoutException();
            }
        } while (primaryKeyExists(primaryKey, clazz));
        return primaryKey;
    }

    private <T> boolean primaryKeyExists(Long primaryKey, Class<T> clazz) {
        try {
            var repositoryClass = Class.forName(REPOSITORY_PACKAGE + clazz.getSimpleName() + REPOSITORY_STRING);
            var findByIdMethod = repositoryClass.getMethod("findById", Object.class);
            var repository = SpringContextHolder.getContext().getBean(repositoryClass);
            return ((Optional<?>)findByIdMethod.invoke(repository, primaryKey)).isPresent();
        } catch (ReflectiveOperationException e) {
            throw new PrimaryKeyGenerationException("Reflective operation error during primary key generation", e);
        }
    }
}
