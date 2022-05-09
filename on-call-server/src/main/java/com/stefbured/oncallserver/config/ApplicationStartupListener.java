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
    private static final String DROP_TABLES_QUERY_PATH = "db/clear_tables.sql";
    private static final String TEST_GRANTS_QUERY_PATH = "db/test/test_grants.sql";
    private static final String TEST_GROUPS_QUERY_PATH = "db/test/test_groups.sql";
    private static final String TEST_USERS_QUERY_PATH = "db/test/test_users.sql";
    private static final String TEST_SCHEDULE_RECORDS_QUERY_PATH = "db/test/test_schedule_records.sql";
    private static final String PRODUCTION_INIT_QUERY_PATH = "db/production_init.sql";

    // Defaults
    private static final String NOTIFICATION_TYPES_DEFAULTS = "db/defaults/notification_types.sql";
    private static final String ROLE_TYPES_DEFAULTS = "db/defaults/role_types.sql";
    private static final String PERMISSIONS_DEFAULTS = "db/defaults/permissions.sql";
    private static final String ROLES_DEFAULTS = "db/defaults/roles.sql";

    private final SessionFactory sessionFactory;

    @Autowired
    public ApplicationStartupListener(EntityManagerFactory entityManagerFactory) {
        sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (args.containsOption("dbreload")) {
            if (args.containsOption("test")) {
                runQueries(DROP_TABLES_QUERY_PATH);
                initializeDefaults();
                runQueries(TEST_USERS_QUERY_PATH);
                runQueries(TEST_GROUPS_QUERY_PATH);
                runQueries(TEST_GRANTS_QUERY_PATH);
                runQueries(TEST_SCHEDULE_RECORDS_QUERY_PATH);
            } else {
                runQueries(PRODUCTION_INIT_QUERY_PATH);
            }
        }
    }

    // The order is important! Be careful.
    private void initializeDefaults() {
        runQueries(NOTIFICATION_TYPES_DEFAULTS);
        runQueries(ROLE_TYPES_DEFAULTS);
        runQueries(PERMISSIONS_DEFAULTS);
        runQueries(ROLES_DEFAULTS);
    }

    private void runQueries(String queryPath) {
        try (var session = sessionFactory.openSession()) {
            session.doWork(connection -> {
                var initQueryText = getInitQuery(queryPath);
                var queries = new SqlScriptSplitter().split(initQueryText);
                for (var query : queries) {
                    var statement = connection.prepareStatement(query);
                    statement.executeUpdate();
                }
            });
        }
    }

    private String getInitQuery(String queryPath) {
        var queryStream = getClass().getClassLoader().getResourceAsStream(queryPath);
        if (queryStream == null) {
            throw new NullPointerException("Query file name is null");
        }
        String query;
        try (var scanner = new Scanner(queryStream)) {
            query = scanner.useDelimiter("\\A").next();
        }
        return query;
    }
}
