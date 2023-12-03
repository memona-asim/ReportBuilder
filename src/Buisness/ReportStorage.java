package Buisness;

import DataAccess.ReportStructureDBDAO;
import Interface.ReportStructure;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportStorage {
    List<ReportStructure>reports;
    public ReportStorage(){
        reports=new ArrayList<>();
    }
    public List<ReportStructure>getReports(){
        loadReports();
        return reports;
    }
    public void loadReports(){
        reports.clear();
        HashMap<String,String>map= ReportStructureDBDAO.loadStructure();
        for(String str: map.keySet()){
            reports.add(loadReportStructure(map.get(str)));
        }
    }
    public ReportStructure loadReportStructure(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (ReportStructure) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void savetoDB(ReportStructure r){
        ReportStructureDBDAO.saveStructure(r.getName(),r.getFilename());
    }
}
