package DataAccess;

import java.sql.*;
import java.util.HashMap;

public class ReportStructureDBDAO {
    public static void saveStructure(String name,String filename){
        String query="Insert into report_structure (name,filename) values (?,?)";
        PreparedStatement stmt=null;
        Connection connection = DatabaseConfig.getConnection();
        try {
            stmt=connection.prepareStatement(query);
            stmt.setString(1,name);
            stmt.setString(2,filename);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to save structure.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static HashMap<String,String> loadStructure(){
        HashMap<String,String> fileNames = new HashMap<>();
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM report_structure")) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String file = resultSet.getString("filename");

                fileNames.put(name,file);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error loading text from database: " + ex.getMessage(), ex);
        }
        return fileNames;
    }
}
