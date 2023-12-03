package DataAccess;

import Buisness.PieChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PieChartDBDAO {

    public static void savePieChart(HashMap<String, Integer> pieChartData, PieChart p) {
        PreparedStatement statement = null;
        try (Connection connection = DatabaseConfig.getConnection()) {
            statement = connection.prepareStatement(
                    "INSERT INTO PieChartTable (component,arc_angle) VALUES (?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, toStringComponent(pieChartData));
            statement.setString(2, toStringAngle(pieChartData));

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating a  pie Chart failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int Id = generatedKeys.getInt(1);
                    p.setId(Id);
                } else {
                    throw new SQLException("Creating Pie Chart failed, no ID obtained.");
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

    static String toStringComponent(HashMap<String,Integer>map){
        StringBuilder string= new StringBuilder();
        for(String s: map.keySet()){
            string.append(s+",");
        }
        if (string.length() > 0) {
            string.deleteCharAt(string.length() - 1);
        }
        return String.valueOf(string);
    }
    static String toStringAngle(HashMap<String,Integer>map){
        StringBuilder string= new StringBuilder();
        for(String s: map.keySet()){
            string.append(String.valueOf(map.get(s))+",");
        }
        if (string.length() > 0) {
            string.deleteCharAt(string.length() - 1);
        }
        return String.valueOf(string);
    }



    public static List<PieChart> loadPieChart() {
        List<PieChart> pieCharts = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM PieChartTable")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String component = resultSet.getString("component");
                String arc_angle = resultSet.getString("arc_angle");
                PieChart pieChart = new PieChart(toHashmap(component,arc_angle));
                pieChart.setId(id);
                pieCharts.add(pieChart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all Pie Charts from the database");
        }
        return pieCharts;
    }

    static HashMap<String,Integer> toHashmap(String c, String a){
        HashMap<String,Integer>map=new HashMap<>();
        List<String> component = Arrays.asList(c.split(","));
        List<String> angle = Arrays.asList(a.split(","));
        List<Integer>angleI=new ArrayList<>();
        for(int i=0;i<angle.size();i++){
            angleI.add(Integer.parseInt(angle.get(i)));
        }
        for(int i=0;i<component.size();i++){
            map.put(component.get(i),angleI.get(i));
        }
        return map;
    }


//    public PieChart getPieChartById(int Id) {
//        try (Connection connection = DatabaseConfig.getConnection();
//             PreparedStatement statement = connection.prepareStatement("SELECT * FROM PieChartTable WHERE id = ?")) {
//            statement.setInt(1, Id);
//
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    String component = resultSet.getString("component");
//                    String arc_angle = resultSet.getString("arc_angle");
//                    PieChart piechart= new PieChart(Id,component,arc_angle);
//                    return piechart;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle the exception in a more appropriate way in a real application
//            throw new RuntimeException("Error retrieving  pie chart  by id  from the database");
//        }
//        return null;
//    }


    public static List<String> StringToList(String string) {
        String[] stringArray = string.split(",");
        return Arrays.asList(stringArray);
    }
}
