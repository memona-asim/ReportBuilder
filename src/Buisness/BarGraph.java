package Buisness;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;

public class BarGraph {

    private DefaultCategoryDataset dataset;
    int id;
    String[]axis;
    private JFreeChart chart; // Member to store the chart

    public BarGraph() {
        dataset = new DefaultCategoryDataset();
        chart = null; // Initialize the chart as null
    }

    public void addData(String series, String category, double value) {
        dataset.addValue(value, series, category);
    }
    public void setId(int id){
        this.id=id;
    }
    public JFreeChart createBarChart(String chartTitle, String categoryAxisLabel, String valueAxisLabel) {
        chart = ChartFactory.createBarChart(
                chartTitle,
                categoryAxisLabel,
                valueAxisLabel,
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        return chart;
    }

    public void setSeriesColor(String seriesName, Color color) {
        if (chart != null) {
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            int seriesIndex = dataset.getRowIndex(seriesName);
            assert seriesIndex>0;
            System.out.println(seriesName);
            renderer.setSeriesPaint(seriesIndex, color);
        }
    }

    public String[] getSeriesNames() {
        List<Comparable<?>> keys = dataset.getRowKeys();
        String[] seriesNames = new String[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            seriesNames[i] = keys.get(i).toString();
        }
        return seriesNames;
    }

    public DefaultCategoryDataset getDataset() {
        return dataset;
    }
    public void clearData() {
        dataset.clear();
    }
    public void loadData(HashMap<String, HashMap<String, Double>> data) {
        for (String category : data.keySet()) {
            for (String series : data.get(category).keySet()) {
                double value = data.get(category).get(series);
                dataset.addValue(value, series, category);
            }
        }
    }
    public int getId(){
        return id;
    }
    public void setAxis(String[] s){
        axis=s;
    }
    public String[] getAxis(){
        return axis;
    }
}
