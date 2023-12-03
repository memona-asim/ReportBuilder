package Interface;

import Buisness.PieChart;
import DataAccess.CsvDAO;
import DataAccess.PieChartDBDAO;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class PieChartPanel extends JPanel {
    public HashMap<String, Integer> pieChartData;
    private PieChart pieChart;
    private ChartPanel pieChartPanelComponent;
    private double chartScaleFactor = 0.8;
    private HashMap<String, Color> colorMap; // Store colors for each key
    private JComboBox<String> keyComboBox;
    public PieChartPanel() {
        setSize(400, 400);
        colorMap = new HashMap<>();
        keyComboBox=new JComboBox<>();
        pieChartPanelComponent = new ChartPanel(null);
        pieChartPanelComponent.setLayout(new BorderLayout());
        pieChartPanelComponent.setBackground(Color.WHITE);
        pieChart=new PieChart();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    createContextMenu().show(PieChartPanel.this, e.getX(), e.getY());
                }
            }
        });

        pieChartPanelComponent.setPreferredSize(new Dimension(
                (int) (pieChartPanelComponent.getPreferredSize().width * chartScaleFactor),
                (int) (pieChartPanelComponent.getPreferredSize().height * chartScaleFactor)
        ));

        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        this.add(pieChartPanelComponent);
    }
    private void openOptionsPanel() {
        // Create a separate panel with a combo box and a button
        JPanel optionsPanel = new JPanel();

        JButton actionButton = new JButton("Choose Colour");
        actionButton.addActionListener(e -> chooseKeyColor());

        optionsPanel.add(keyComboBox);
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
    }
    private void chooseKeyColor() {
        String selectedKey = (String) keyComboBox.getSelectedItem();
        if (selectedKey != null) {
            Color color = ColorChooser.chooseColor(this);
            if (color != null) {
                colorMap.put(selectedKey, color); // Store the color
                drawPieChart();
            }
        }
    }
    private void updateKeyComboBox() {
        if(!(keyComboBox==null))
        keyComboBox.removeAllItems();
        for (String key : pieChartData.keySet()) {
            keyComboBox.addItem(key);
        }
    }
    void loadPieChart(){
        pieChartData = CsvDAO.loadPieChart();
        drawPieChart();
    }
    public void loadFromSaved(PieChart p){
        pieChartData=p.getDataSet();
        drawPieChart();
    }
    void addPieChart(){
        String dataset = JOptionPane.showInputDialog("Enter Pie Chart dataset with the item names (e.g. abc:43,def:56):");
        if (dataset != null && !dataset.isEmpty()) {
            pieChartData = CsvDAO.parseDataset(dataset);
            drawPieChart();
        }
    }
    private JPopupMenu createContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Save");
        JMenuItem menuItem1 =new JMenuItem("Customize Colour");
        menu.add(menuItem);
        menu.add(menuItem1);

        menuItem.addActionListener(e -> {
            CsvDAO.savePieChartData(pieChartData,this);
            PieChartDBDAO.savePieChart(pieChartData,pieChart);
        });
        menuItem1.addActionListener(e-> openOptionsPanel());
        return menu;
    }
    void drawPieChart() {
        System.out.println("Drawing Pie Chart");
        pieChart.addData(pieChartData);

        JFreeChart pieChartJ = pieChart.showPie();
        ChartPanel chartPanel1 = new ChartPanel(pieChartJ);

        if (pieChartPanelComponent == null) {
            System.out.println("chartPanelComponent is null. Make sure it is properly initialized.");
            return;
        }
        this.pieChartPanelComponent.removeAll();
        pieChartPanelComponent.add(chartPanel1, BorderLayout.CENTER);
        pieChartPanelComponent.revalidate();
        pieChartPanelComponent.repaint();

        PiePlot plot = (PiePlot) pieChartJ.getPlot();
        colorMap.forEach(plot::setSectionPaint);

        pieChartPanelComponent.setChart(pieChartJ);
        updateKeyComboBox();
    }
}