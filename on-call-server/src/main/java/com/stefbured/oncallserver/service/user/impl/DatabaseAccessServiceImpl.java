package com.stefbured.oncallserver.service.user.impl;

import com.stefbured.oncallserver.model.dto.db.DatabaseQueryResultDTO;
import com.stefbured.oncallserver.service.user.DatabaseAccessService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class DatabaseAccessServiceImpl implements DatabaseAccessService {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseAccessServiceImpl.class);
    private static final int MAX_ROWS_COUNT = 500;

    private final SessionFactory sessionFactory;

    @Autowired
    public DatabaseAccessServiceImpl(EntityManagerFactory entityManagerFactory) {
        sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Override
    public List<DatabaseQueryResultDTO> runQueries(Collection<String> queries) {
        var queryResults = new ArrayList<DatabaseQueryResultDTO>();
        try (var session = sessionFactory.openSession()) {
            session.doWork(connection -> {
                for (var query : queries) {
                    queryResults.add(runSingleQuery(connection, query));
                }
            });
            return queryResults;
        } catch (HibernateException exception) {
            LOGGER.error("Hibernate exception occurred", exception);
            throw new HibernateException(exception);
        }
    }

    private DatabaseQueryResultDTO runSingleQuery(Connection connection, String query) {
        var queryResult = new DatabaseQueryResultDTO();
        try (var statement = connection.prepareStatement(query)) {
            queryResult.setQuery(query);
            if (!statement.execute()) {
                return queryResult;
            }
            var resultSet = statement.getResultSet();
            var metaData = resultSet.getMetaData();
            var columnsCount = metaData.getColumnCount();
            var columnNames = new ArrayList<String>(columnsCount);
            for (int i = 1; i <= columnsCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            queryResult.setColumnNames(columnNames);
            var dataRows = new ArrayList<List<String>>();
            int rowsCount = 0;
            for (; rowsCount < MAX_ROWS_COUNT && resultSet.next(); rowsCount++) {
                var row = new ArrayList<String>(columnsCount);
                for (int j = 1; j <= columnsCount; j++) {
                    row.add(resultSet.getString(j));
                }
                dataRows.add(row);
            }
            queryResult.setDataRows(dataRows);
        } catch (SQLException exception) {
            LOGGER.info("Database error occurred: message={}", exception.getMessage());
            queryResult.setExceptionMessage(exception.getMessage());
        }
        return queryResult;
    }
}
