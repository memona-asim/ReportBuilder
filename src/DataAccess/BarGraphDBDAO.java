package DataAccess;

import Buisness.BarGraph;

import java.sql.*;
import java.util.*;

public class BarGraphDBDAO {

    public static void saveToDB(String xAxis, String yAxis, HashMap<String, HashMap<String,Double>>map,BarGraph graph) {
        PreparedStatement statement = null;
        try (Connection connection = DatabaseConfig.getConnection()) {
            statement = connection.prepareStatement(
                    "INSERT INTO BarGraphTable (x_axis_label,y_axis_label,categories,series,value) VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, xAxis);
            statement.setString(2, yAxis);
            statement.setString(3, toStringCategories(map));
            statement.setString(4, toStringSeries(map) );
            statement.setString(5, toStringValues(map));

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating a Graph failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int Id = generatedKeys.getInt(1);
                    graph.setId(Id);
                } else {
                    throw new SQLException("Creating Graph failed, no ID obtained.");
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

    static String toStringCategories(HashMap<String,HashMap<String,Double>>map){
        StringBuilder string= new StringBuilder();
        for(String s: map.keySet()){
            string.append(s+",");
        }
        if (string.length() > 0) {
            string.deleteCharAt(string.length() - 1);
        }
        return String.valueOf(string);
    }
    public static String toStringSeries(HashMap<String, HashMap<String, Double>> map) {
        StringBuilder string = new StringBuilder();
        Set<String> uniqueStrings = new HashSet<>();

        for (String s : map.keySet()) {
            HashMap<String, Double> inner = map.get(s);
            for (String str : inner.keySet()) {
                // Only append if the string is not already in the set
                if (uniqueStrings.add(str)) {
                    string.append(str).append(",");
                }
            }
        }

        // Remove the trailing comma if there are appended strings
        if (string.length() > 0) {
            string.deleteCharAt(string.length() - 1);
        }

        return string.toString();
    }

    static String toStringValues(HashMap<String,HashMap<String,Double>>map){
        StringBuilder string= new StringBuilder();
        HashMap<String,Double>inner;
        for(String s:map.keySet()){
            inner=map.get(s);
            for(String str:inner.keySet()){
                string.append(String.valueOf(inner.get(str))+",");
            }
        }
        if (string.length() > 0) {
            string.deleteCharAt(string.length() - 1);
        }
        return String.valueOf(string);
    }
    public static List<BarGraph> loadFromDB() {
        List<BarGraph> graphs = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM BarGraphTable")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String x_axis_label = resultSet.getString("x_axis_label");
                String y_axis_label = resultSet.getString("y_axis_label");
                String categories = resultSet.getString("categories");
                String series = resultSet.getString("series");
                String value = resultSet.getString("value");

                String[]axis=new String[2];
                axis[0]=x_axis_label;
                axis[1]=y_axis_label;

                System.out.println(id);

                HashMap<String,HashMap<String,Double>>map=toHashmap(categories,series,value);
                BarGraph graph = new BarGraph();
                graph.setId(id);
                graph.loadData(map);
                graph.setAxis(axis);
                graphs.add(graph);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception in a more appropriate way in a real application
            throw new RuntimeException("Error retrieving all Bar Graphs from the database");
        }
        return graphs;
    }
    public static HashMap<String, HashMap<String, Double>> toHashmap(String c, String s, String v) {
        int count = 0;
        List<String> categories = Arrays.asList(c.split(","));
        List<String> seriesList = Arrays.asList(s.split(","));
        List<Double> values = new ArrayList<>();

        List<String> array = Arrays.asList(v.split(","));
        for (int i = 0; i < array.size(); i++) {
            values.add(Double.parseDouble(array.get(i)));
            System.out.println(values.get(i));
        }

        HashMap<String, HashMap<String, Double>> map = new HashMap<>();

        for (int i = 0; i < categories.size(); i++) {
            map.put(categories.get(i), new HashMap<>());
        }

        for (int i = 0; i < categories.size(); i++) {
            HashMap<String, Double> inner = map.get(categories.get(i));
            for (int j = 0; j < seriesList.size(); j++) {
                inner.put(seriesList.get(j), values.get(count));
                count++;
            }
        }

        for (Map.Entry<String, HashMap<String, Double>> outerEntry : map.entrySet()) {
            String outerKey = outerEntry.getKey();
            HashMap<String, Double> innerMap = outerEntry.getValue();

            System.out.println("Category: " + outerKey);
            for (Map.Entry<String, Double> innerEntry : innerMap.entrySet()) {
                String innerKey = innerEntry.getKey();
                Double value = innerEntry.getValue();

                System.out.println("  Inner Key: " + innerKey + ", Value: " + value);
            }
        }
        return map;
    }

//    public Graph getGraphById(int Id) {
//        try (Connection connection = DatabaseConfig.getConnection();
//             PreparedStatement statement = connection.prepareStatement("SELECT * FROM BarGraphTable WHERE id = ?")) {
//            statement.setInt(1, Id);
//
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    String x_axis_label = resultSet.getString("x_axis_label");
//                    String y_axis_label = resultSet.getString("y_axis_label");
//                    String categories  = resultSet.getString("categories");
//                    String series = resultSet.getString("series");
//                    String value = resultSet.getString("value");
//
//                    Graph bargraph = new Graph(Id,x_axis_label,y_axis_label,categories,series,value);
//                    return bargraph;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle the exception in a more appropriate way in a real application
//            throw new RuntimeException("Error retrieving  graph  by id  from the database");
//        }
//        return null;
//    }


    public static List<String> StringToList(String string) {
        String[] stringArray = string.split(",");
        return Arrays.asList(stringArray);
    }

}

