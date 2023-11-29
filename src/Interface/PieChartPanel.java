package Interface;

import Buisness.PieChart;
import DataAccess.CsvDAO;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;

class PieChartPanel extends JPanel {
    public HashMap<String, Integer> pieChartData;
    private ChartPanel pieChartPanelComponent;
    private int pieChartDiameter = 200;
    private int pieChartX = 50;
    private int pieChartY = 50;
    private Point dragStart;
    private boolean resizing = false;
    public PieChartPanel() {
        setSize(400, 400);
        pieChartPanelComponent = new ChartPanel(null);
        pieChartPanelComponent.setLayout(new BorderLayout());
        pieChartPanelComponent.setBackground(Color.WHITE);

        this.add(pieChartPanelComponent);
    }
    void loadPieChart(){
        pieChartData = CsvDAO.loadPieChart();
        drawPieChart();
    }
    void addPieChart(){
        String dataset = JOptionPane.showInputDialog("Enter Pie Chart dataset with the item names (e.g. abc:43,def:56):");
        if (dataset != null && !dataset.isEmpty()) {
            pieChartData = CsvDAO.parseDataset(dataset);
            drawPieChart();
        }
    }
    void savePieChart(Report report){
        CsvDAO.savePieChartData(pieChartData, report);
    }
    void drawPieChart() {
        System.out.println("Drawing Pie Chart");

        JFreeChart pieChart = new PieChart(pieChartData).showPie();
        ChartPanel chartPanel1 = new ChartPanel(pieChart);

        if (pieChartPanelComponent == null) {
            System.out.println("chartPanelComponent is null. Make sure it is properly initialized.");
            return;
        }
        this.pieChartPanelComponent.removeAll();
        pieChartPanelComponent.add(chartPanel1, BorderLayout.CENTER);
        pieChartPanelComponent.revalidate();
        pieChartPanelComponent.repaint();
    }
}