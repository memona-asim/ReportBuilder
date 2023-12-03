package DataAccess;

import Buisness.LineGraph;
import java.sql.*;
import java.util.*;

public class LineGraphDBDAO {

    public static void saveToDB(LineGraph lineGraph) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfig.getConnection();
            // Assuming a table structure with columns: id (auto-increment), series_name, x_value, y_value
            String sql = "INSERT INTO LineGraphTable (series_name, x_value, y_value) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            Map<String, List<double[]>> dataMap = lineGraph.getData();
            for (Map.Entry<String, List<double[]>> entry : dataMap.entrySet()) {
                String seriesName = entry.getKey();
                for (double[] point : entry.getValue()) {
                    statement.setString(1, seriesName);
                    statement.setDouble(2, point[0]);
                    statement.setDouble(3, point[1]);
                    statement.addBatch();
                }
            }

            statement.executeBatch();

            // Retrieve generated keys (IDs) if needed
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConfig.closeConnection(connection);
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static LineGraph loadFromDB(int graphId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        LineGraph lineGraph = new LineGraph();

        try {
            connection = DatabaseConfig.getConnection();
            String sql = "SELECT * FROM LineGraphTable WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, graphId);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String seriesName = resultSet.getString("series_name");
                double xValue = resultSet.getDouble("x_value");
                double yValue = resultSet.getDouble("y_value");
                lineGraph.addDataPoint(seriesName, xValue, yValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConfig.closeConnection(connection);
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return lineGraph;
    }
}
