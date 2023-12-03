package Buisness;

import DataAccess.PieChartDBDAO;

import java.util.ArrayList;
import java.util.List;

public class PieChartStorage {
    private List<PieChart> pieChartList;
    public PieChartStorage(){
        pieChartList=new ArrayList<>();
    }
    public List<PieChart> getPieChartList() {
        loadList();
        return pieChartList;
    }
    public void loadList(){
        pieChartList= PieChartDBDAO.loadPieChart();
    }
}
