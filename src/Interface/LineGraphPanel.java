package Interface;

import Buisness.LineGraph;
import DataAccess.CsvDAO;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class LineGraphPanel extends JPanel {
    private LineGraph lineGraph;
    private double chartScaleFactor=0.8;
    private JFreeChart lineChart;
    private ChartPanel chartPanel;
    private String xAxisLabel;
    private String yAxisLabel;
    private JComboBox<String> seriesComboBox; // For selecting series

    public LineGraphPanel() {
        lineGraph = new LineGraph();
        xAxisLabel = "X Axis";
        yAxisLabel = "Y Axis";
        lineChart = lineGraph.createLineChart("Line Chart", xAxisLabel, yAxisLabel);
        chartPanel = new ChartPanel(lineChart);

        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        chartPanel.setPreferredSize(new Dimension(
                (int) (chartPanel.getPreferredSize().width * chartScaleFactor),
                (int) (chartPanel.getPreferredSize().height * chartScaleFactor)
        ));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    createContextMenu().show(LineGraphPanel.this, e.getX(), e.getY());
                }
            }
        });

        seriesComboBox = new JComboBox<>(); // Initialize the combo box for series selection

        add(chartPanel, BorderLayout.CENTER);

    }
    private void openOptionsPanel() {
        // Create a separate panel with a combo box and a button
        JPanel optionsPanel = new JPanel();

        JButton actionButton = new JButton("Choose Colour");
        actionButton.addActionListener(e->chooseSeriesColor());

        optionsPanel.add(seriesComboBox);
        optionsPanel.add(actionButton);

        // Show the panel in a dialog
        int result = JOptionPane.showOptionDialog(
                this,
                optionsPanel,
                "Options Panel",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );

        if (result == JOptionPane.OK_OPTION) {
            // Take action based on the combo box selection
            String selectedOption = (String) seriesComboBox.getSelectedItem();
            // Implement your logic here
        }

    }
    private JPopupMenu createContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Save");
        JMenuItem menuItem1 =new JMenuItem("Customize Colour");
        menu.add(menuItem);
        menu.add(menuItem1);

        menuItem.addActionListener(e -> {
            saveLineGraph(LineGraphPanel.this);
            lineGraph.saveToDB();
        });
        menuItem1.addActionListener(e-> openOptionsPanel());

        return menu;
    }
    public void loadFromDB() {
        // You can modify this to take an ID or other parameters as needed
        int graphId = askForGraphId(); // Implement this method to get the ID from the user

        // Using the static method from LineGraph class to load data from DB
        LineGraph loadedGraph = LineGraph.loadFromDB(graphId);

        if (loadedGraph != null) {
            this.lineGraph = loadedGraph; // Replace the current lineGraph with the loaded one
            updateChart();
        } else {
            JOptionPane.showMessageDialog(this, "No data found for the given ID.", "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int askForGraphId() {
        // Dummy implementation, replace with actual user input logic
        String idString = JOptionPane.showInputDialog(this, "Enter the ID of the graph to load:");
        try {
            return Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return -1; // Indicating invalid ID
        }
    }
    public void addLineChart() {
        String seriesName = JOptionPane.showInputDialog(this, "Enter series name:");
        String xValuesStr = JOptionPane.showInputDialog(this, "Enter X values (comma-separated):");
        String yValuesStr = JOptionPane.showInputDialog(this, "Enter Y values (comma-separated):");

        if (seriesName != null && xValuesStr != null && yValuesStr != null) {
            double[] xValues = Arrays.stream(xValuesStr.split(","))
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            double[] yValues = Arrays.stream(yValuesStr.split(","))
                    .mapToDouble(Double::parseDouble)
                    .toArray();

            lineGraph.addSeries(seriesName);
            for (int i = 0; i < xValues.length; i++) {
                lineGraph.addDataPoint(seriesName, xValues[i], yValues[i]);
            }
            updateChart();
        }
    }

    public void loadFromFile() {
        // Assuming CsvDAO.loadLineGraphData returns a Map<String, List<double[]>>
        Map<String, List<double[]>> lineGraphData = CsvDAO.loadLineGraphData();
        lineGraph.loadData(lineGraphData);
        updateChart();
        repaint();
    }
    private void chooseSeriesColor() {
        String seriesName = (String) seriesComboBox.getSelectedItem();
        if (seriesName != null) {
            Color color = ColorChooser.chooseColor(this);
            if (color != null) {
                lineGraph.setSeriesColor(seriesName, color);
                refreshChart();
            }
        }
    }

    private void refreshChart() {
        chartPanel.repaint(); // Repaint the chart panel to reflect the color change
    }
    private void updateChart() {
        lineChart = lineGraph.createLineChart("Line Chart", xAxisLabel, yAxisLabel);
        chartPanel.setChart(lineChart);
        updateSeriesComboBox(); // Update the series combo box whenever the chart is updated
    }

    private void updateSeriesComboBox() {
        seriesComboBox.removeAllItems();
        for (String seriesName : lineGraph.getSeriesNames()) { // Assuming getSeriesNames() method exists
            seriesComboBox.addItem(seriesName);
        }
    }
    public void saveLineGraph(LineGraphPanel report) {
        // Assuming CsvDAO has a method to save line graph data
        CsvDAO.saveLineGraphData(lineGraph, report);
    }



    // Add other necessary methods and functionalities as per your application's requirements
}
