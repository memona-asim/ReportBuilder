package DataAccess;

import Buisness.Text;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TextDBDAO {

    public static void saveText(Text Text) {
        PreparedStatement statement = null;
        try (Connection connection = DatabaseConfig.getConnection()) {
            statement = connection.prepareStatement(
                    "INSERT INTO TextTable (text) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, Text.getText());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding text failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    // You can handle the generated ID as needed
                } else {
                    throw new SQLException("Adding text failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // Handle or log the exception
                }
            }
        }
    }

    public static List<Text> loadText() {
        List<Text> textList = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM TextTable")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String text = resultSet.getString("text");

                Text textObject = new Text(id, text);
                textList.add(textObject);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error loading text from database: " + ex.getMessage(), ex);
        }

        return textList;
    }

    public Text getTextById(int id) {
        Text text = null;
        String selectQuery = "SELECT * FROM TextTable WHERE ID = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Assuming you have a constructor in your Text class that takes ID and text parameters
                    text = new Text(resultSet.getInt("ID"), resultSet.getString("text"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving Text by ID: " + e.getMessage(), e);
        }

        return text;
    }


}
