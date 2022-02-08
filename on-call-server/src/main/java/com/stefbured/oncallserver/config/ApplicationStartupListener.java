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
    private static final String GET_ADMIN_USER = "select * from users where username = 'admin'";
    private static final String TEST_INIT_QUERY_PATH = "db/test_init.sql";
    private static final String PRODUCTION_INIT_QUERY_PATH = "db/production_init.sql";

    private final SessionFactory sessionFactory;

    @Autowired
    public ApplicationStartupListener(EntityManagerFactory entityManagerFactory) {
        sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (args.containsOption("test")) {
            initializeDatabase(TEST_INIT_QUERY_PATH);
        } else {
            initializeDatabase(PRODUCTION_INIT_QUERY_PATH);
        }
    }

    private void initializeDatabase(String initQueryPath) {
        try (var session = sessionFactory.openSession()) {
            session.doWork(connection -> {
                var statement = connection.prepareStatement(GET_ADMIN_USER);
                var result = statement.executeQuery();
                if (!result.next()) {
                    var initQueryText = getInitQuery(initQueryPath);
                    var initQueries = new SqlScriptSplitter().split(initQueryText);
                    for (var query : initQueries) {
                        statement = connection.prepareStatement(query);
                        statement.executeUpdate();
                    }
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
