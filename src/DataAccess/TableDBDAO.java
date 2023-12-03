package DataAccess;

import Buisness.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableDBDAO {
    private Table table;

    public TableDBDAO(Table table){this.table=table;}

    public static void saveTable(Table table) {
        String[] columnNames = table.getColNames();
        String[][] tableData = table.getTableData();

        // Check if the table exists, if not, create it
        try {
            if (!tableExists(table.getName())) {
                createTable(table.getName(), columnNames);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String valuePlaceholders = String.join(", ", Collections.nCopies(columnNames.length, "?"));

        String insertRowQuery = "INSERT INTO " + table.getName() + " (" + String.join(", ", columnNames) + ") VALUES (" + valuePlaceholders + ")";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement insertRowStatement = connection.prepareStatement(insertRowQuery)) {

            for (int row = 0; row < tableData.length; row++) {
                for (int col = 0; col < columnNames.length; col++) {
                    String value = tableData[row][col];
                    System.out.println("Setting value for column " + (col + 1) + ": " + value);

                    // Check if the value is a string; if not, convert it to a string
                    if (value != null && !value.isEmpty()) {
                        insertRowStatement.setString(col + 1, value);
                    } else {
                        insertRowStatement.setNull(col + 1, Types.VARCHAR);
                    }
                }

                // Execute the statement for each row
                insertRowStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Print the exception stack trace for debugging
            throw new RuntimeException(e);
        }
    }
    private static boolean tableExists(String tableName) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseConfig.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, tableName, null);
            return resultSet.next();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
    private static void createTable(String tableName, String[] columnNames) throws SQLException {
        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE " + tableName + " (");
        createTableQuery.append("ID INT AUTO_INCREMENT PRIMARY KEY");

        // Check if 'ID' is already present in user-specified column names
        boolean hasID = false;
        for (String columnName : columnNames) {
            if ("ID".equalsIgnoreCase(columnName)) {
                hasID = true;
                break;
            }
        }
        // Add user-specified column names to the query, skipping 'ID' if already present
        for (String columnName : columnNames) {
            if (!hasID || !"ID".equalsIgnoreCase(columnName)) {
                createTableQuery.append(", ").append(columnName).append(" VARCHAR(255)");
            }
        }

        createTableQuery.append(")");

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(createTableQuery.toString());

            System.out.println("Table created successfully!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Table loadTable(String name) {
        List<String[]> loadedData = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + name;
        Table table1=null;

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            int rowCount=0;
            while (resultSet.next()) {
                String[] rowData = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getString(i);
                }
                rowCount++;
                loadedData.add(rowData);
            }
            table1=new Table(rowCount,columnCount);
            table1.setDataSet(loadedData.toArray(new String[0][0]));

        } catch (SQLException e) {
            System.err.println("Error executing query: " + selectQuery);
            e.printStackTrace();
            throw new RuntimeException("Error loading table from database: " + e.getMessage(), e);
        }
        return table1;
    }





}

