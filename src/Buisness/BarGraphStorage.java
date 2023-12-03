package Buisness;

import DataAccess.BarGraphDBDAO;

import java.util.ArrayList;
import java.util.List;

public class BarGraphStorage {
    List<BarGraph> barGraphList;
    public BarGraphStorage(){
        barGraphList=new ArrayList<>();
    }
    void loadBarGraphs(){
        barGraphList= BarGraphDBDAO.loadFromDB();
    }
    public List<BarGraph>getBarGraphList(){
        loadBarGraphs();
        return barGraphList;
    }
}
