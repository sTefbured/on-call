package com.stefbured.oncallserver.config;

import com.stefbured.oncallserver.utils.SqlScriptSplitter;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.Scanner;

@Component
public class ApplicationStartupListener implements ApplicationRunner {
    private static final String GET_ADMIN_USER = "select * from users where username = 'sTefbured-admin'";
    private static final String DEFAULTS_QUERY_PATH = "db/defaults.sql";
    private static final String TEST_GRANTS_QUERY_PATH = "db/test/test_grants.sql";
    private static final String TEST_GROUPS_QUERY_PATH = "db/test/test_groups.sql";
    private static final String TEST_USERS_QUERY_PATH = "db/test/test_users.sql";
    private static final String PRODUCTION_INIT_QUERY_PATH = "db/production_init.sql";

    private final SessionFactory sessionFactory;

    @Autowired
    public ApplicationStartupListener(EntityManagerFactory entityManagerFactory) {
        sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Override
    public void run(ApplicationArguments args) {
        initializeDatabase(DEFAULTS_QUERY_PATH);
        if (args.containsOption("test")) {
            initializeDatabase(TEST_USERS_QUERY_PATH);
            initializeDatabase(TEST_GROUPS_QUERY_PATH);
            initializeDatabase(TEST_GRANTS_QUERY_PATH);
        } else {
            initializeDatabase(PRODUCTION_INIT_QUERY_PATH);
        }
    }

    private void initializeDatabase(String initQueryPath) {
        try (var session = sessionFactory.openSession()) {
            session.doWork(connection -> {
                var initQueryText = getInitQuery(initQueryPath);
                var initQueries = new SqlScriptSplitter().split(initQueryText);
                for (var query : initQueries) {
                    var statement = connection.prepareStatement(query);
                    statement.executeUpdate();
                }
            });
        }
    }

    private String getInitQuery(String initQueryPath) {
        var queryStream = getClass().getClassLoader().getResourceAsStream(initQueryPath);
        if (queryStream == null) {
            throw new NullPointerException("Init script file name is null");
        }
        String query;
        try (var scanner = new Scanner(queryStream)) {
            query = scanner.useDelimiter("\\A").next();
        }
        return query;
    }
}
