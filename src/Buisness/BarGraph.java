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
    private JFreeChart chart; // Member to store the chart

    public BarGraph() {
        dataset = new DefaultCategoryDataset();
        chart = null; // Initialize the chart as null
    }

    public void addData(String series, String category, double value) {
        dataset.addValue(value, series, category);
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
            renderer.setSeriesPaint(seriesIndex, color);
        }
    }


    public  String[] getSeriesNames() {
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
        for (String series : data.keySet()) {
            for (String category : data.get(series).keySet()) {
                double value = data.get(series).get(category);
                dataset.addValue(value, series, category);
            }
        }
    }
}
