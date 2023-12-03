package Buisness;

import java.util.ArrayList;
import java.util.List;

public class TableStorage {
    private List<Table> tableList;
    public TableStorage(){
        tableList=new ArrayList<>();
    }
    public void loadList(List<Table>tables){
        tableList=tables;
    }
    public List<Table> getTableList() {
        return tableList;
    }
}
