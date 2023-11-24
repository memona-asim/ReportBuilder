import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Report extends JFrame {
    private PieChartPanel pieChartPanel;
    private BarGraphPanel barGraphPanel;
    private JPanel chartContainer;
    private CardLayout cardLayout;

    public Report() {
        setTitle("Report Builder");

        pieChartPanel = new PieChartPanel();
        barGraphPanel = new BarGraphPanel();

        cardLayout = new CardLayout();
        chartContainer = new JPanel(cardLayout);
        chartContainer.add(pieChartPanel, "PieChart");
        chartContainer.add(barGraphPanel, "BarGraph");

        setLayout(new BorderLayout());
        add(createToolbar(), BorderLayout.NORTH);
        add(chartContainer, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JToolBar createToolbar() {
        JToolBar toolBar = new JToolBar();
        JButton pieChartButton = new JButton("Pie Chart");
        JButton barGraphButton = new JButton("Bar Graph");

        pieChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(chartContainer, "PieChart");
            }
        });

        barGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(chartContainer, "BarGraph");
            }
        });

        toolBar.add(pieChartButton);
        toolBar.add(barGraphButton);

        return toolBar;
    }

    class PieChartPanel extends JPanel {
        private HashMap<String, Integer> pieChartData;
        private ChartPanel pieChartPanelComponent;

        public PieChartPanel() {
            setTitle("Pie Chart");
            setSize(400, 400);
            pieChartPanelComponent = new ChartPanel(null);
            pieChartPanelComponent.setLayout(new BorderLayout());
            pieChartPanelComponent.setBackground(Color.WHITE);

            JButton pieChartButton = new JButton("Add Pie Chart");
            pieChartButton.addActionListener(e -> {
                String dataset = JOptionPane.showInputDialog("Enter Pie Chart dataset with the item names (e.g. abc:43,def:56):");
                if (dataset != null && !dataset.isEmpty()) {
                    pieChartData = CsvDAO.parseDataset(dataset);
                    drawPieChart();
                }
            });

            JButton loadPieChartButton = new JButton("Load Pie Chart");
            loadPieChartButton.addActionListener(e -> {
                pieChartData = CsvDAO.loadPieChart();
                drawPieChart();
            });

            JButton saveButton = new JButton("Save Pie Chart Data");
            saveButton.addActionListener(e -> CsvDAO.savePieChartData(pieChartData, Report.this));

            this.add(pieChartButton);
            this.add(saveButton);
            this.add(loadPieChartButton);
            this.add(pieChartPanelComponent);
        }

        private void drawPieChart() {
            System.out.println("Drawing Pie Chart");

            JFreeChart pieChart = new PieChart(pieChartData).showPie();
            ChartPanel chartPanel1 = new ChartPanel(pieChart);

            if (pieChartPanelComponent == null) {
                System.out.println("chartPanelComponent is null. Make sure it is properly initialized.");
                return;
            }

            pieChartPanelComponent.removeAll();
            pieChartPanelComponent.add(chartPanel1, BorderLayout.CENTER);
            pieChartPanelComponent.revalidate();
            pieChartPanelComponent.repaint();
        }
    }

    public class BarGraphPanel extends JPanel {
        private BarGraph barGraph;
        private JFreeChart barChart;
        private String xAxisLabel = " ";
        String category = "";
        int numberOfBars = -1;
        String[] series;
        private String yAxisLabel = " ";
        private String[] axisNames;
        private HashMap<String, HashMap<String, Double>> barGraphData;
        private ChartPanel chartPanel;

        public BarGraphPanel() {
            barGraph = new BarGraph();
            barGraphData = new HashMap<>();

            JButton loadFileButton =new JButton("Load From File");

            loadFileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    axisNames=new String[3];
                    barGraphData= CsvDAO.loadBarChartData(axisNames);
                    assert axisNames[2]!=null;
                    numberOfBars=Integer.parseInt(axisNames[2]);
                    CategoryPlot plot = (CategoryPlot) barChart.getPlot();
                    plot.getDomainAxis().setLabel(axisNames[0]);
                    plot.getRangeAxis().setLabel(axisNames[1]);
                    barGraph.loadData(barGraphData);
                    xAxisLabel=axisNames[0];
                    yAxisLabel=axisNames[1];
                    series= barGraph.getSeriesNames();
                    barChart = barGraph.createBarChart("Bar Chart", axisNames[0], axisNames[1]);
                    CategoryPlot plot1 = (CategoryPlot) barChart.getPlot();
                    plot.getDomainAxis().setCategoryMargin(0.2); // Adjust the margin as needed
                }
            });

            JButton addDataButton = new JButton("Add Data");
            addDataButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    collectUserData();
                    updateChart();
                }
            });

            JButton saveButton = new JButton("Save Bar Graph Data");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveBarGraph();
                }
            });

            barChart = barGraph.createBarChart("Bar Chart", xAxisLabel, yAxisLabel);

            CategoryPlot plot = (CategoryPlot) barChart.getPlot();
            plot.getDomainAxis().setCategoryMargin(0);

            //this.setLayout(new BorderLayout());

            chartPanel = new ChartPanel(barChart);
            this.add(loadFileButton);
            this.add(addDataButton);
            this.add(saveButton);
            this.add(chartPanel, BorderLayout.CENTER);

        }

        private void setNumberOfBars() {
            String input = JOptionPane.showInputDialog("Enter the number of bars:");
            try {
                numberOfBars = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
            }
        }

        private void collectUserData() {
            if (axisNames == null) {
                axisNames = new String[2];
            }
            if (numberOfBars == -1) {
                setNumberOfBars();
                series = new String[numberOfBars];
                Arrays.fill(series, "");
            }

            if ((numberOfBars) <= 0) {
                JOptionPane.showMessageDialog(this, "Number of bars should be greater than 0.");
                return;
            }

            if (Objects.equals(xAxisLabel, " ") && Objects.equals(yAxisLabel, " ")) {
                xAxisLabel = JOptionPane.showInputDialog("Enter X-Axis Label:");
                if (xAxisLabel == null || xAxisLabel.isEmpty()) {
                    return;
                }
                axisNames[0] = xAxisLabel;

                yAxisLabel = JOptionPane.showInputDialog("Enter Y-Axis Label:");
                if (yAxisLabel == null || yAxisLabel.isEmpty()) {
                    return;
                }
                axisNames[1] = yAxisLabel;

                CategoryPlot plot = (CategoryPlot) barChart.getPlot();
                plot.getDomainAxis().setLabel(axisNames[0]);
                plot.getRangeAxis().setLabel(axisNames[1]);
            }

            HashMap<String, Double> innerMap = new HashMap<>();

                category = JOptionPane.showInputDialog("Enter Category for Bar ");

            if (category != null && !category.isEmpty()) {

                for (int i = 0; i < numberOfBars; i++) {

                    if (series[i].isEmpty()) {
                        series[i] = JOptionPane.showInputDialog("Enter Series for Bar " + (i + 1) + ":");
                    }
                    if (series[i] != null && !series[i].isEmpty()) {
                        String value = JOptionPane.showInputDialog("Enter Value for Bar " + (i + 1) + ":");
                        if (value != null && !value.isEmpty()) {
                            try {
                                double valueNum = Double.parseDouble(value);
                                barGraph.addData(series[i], category, valueNum);
                                innerMap.put(series[i], valueNum);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Invalid Value. Please enter a valid number.");
                            }
                        }
                    }
                }
                barGraphData.put(category,innerMap);
            }
        }
        private void updateChart() {
            barChart = barGraph.createBarChart("Bar Chart", axisNames[0], axisNames[1]);
            chartPanel.setChart(barChart);  // Removed casting issue
        }
        public void saveBarGraph(){
            CsvDAO.saveBarChartData(xAxisLabel,yAxisLabel,numberOfBars,barGraphData,Report.this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Report().setVisible(true);
        });
    }
}
