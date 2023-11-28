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
        //this.setTitle("Pie Chart");
        setSize(400, 400);
        pieChartPanelComponent = new ChartPanel(null);
        pieChartPanelComponent.setLayout(new BorderLayout());
        pieChartPanelComponent.setBackground(Color.WHITE);

//        JButton pieChartButton = new JButton("Add Pie Chart");
//        pieChartButton.addActionListener(e -> {
//            String dataset = JOptionPane.showInputDialog("Enter Pie Chart dataset with the item names (e.g. abc:43,def:56):");
//            if (dataset != null && !dataset.isEmpty()) {
//                pieChartData = DataAccess.CsvDAO.parseDataset(dataset);
//                drawPieChart();
//            }
//        });

//        JButton loadPieChartButton = new JButton("Load Pie Chart");
//        loadPieChartButton.addActionListener(e -> {
//            pieChartData = DataAccess.CsvDAO.loadPieChart();
//            drawPieChart();
//        });

//        JButton saveButton = new JButton("Save Pie Chart Data");
//        saveButton.addActionListener(e -> DataAccess.CsvDAO.savePieChartData(pieChartData, new Interface.Report()));

//        this.add(pieChartButton);
//        this.add(saveButton);
//        this.add(loadPieChartButton);
        this.add(pieChartPanelComponent);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isInsidePieChart(e.getPoint())) {
                    dragStart = e.getPoint();
                    resizing = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null;
                resizing = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null && resizing) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    pieChartX += dx;
                    pieChartY += dy;
                    dragStart = e.getPoint();
                    repaint();
                }
            }
        });
    }
    private boolean isInsidePieChart(Point point) {
        int centerX = pieChartX + pieChartDiameter / 2;
        int centerY = pieChartY + pieChartDiameter / 2;
        int radius = pieChartDiameter / 2;

        double distance = Math.sqrt(Math.pow(point.x - centerX, 2) + Math.pow(point.y - centerY, 2));
        return distance <= radius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillArc(pieChartX, pieChartY, pieChartDiameter, pieChartDiameter, 0, 360);
        g.setColor(Color.BLACK);
        g.drawArc(pieChartX, pieChartY, pieChartDiameter, pieChartDiameter, 0, 360);
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