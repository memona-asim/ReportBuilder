package Buisness;

public class Table {
    private String[][] tableData;
    int rows;
    int col;
    String name;
    String[]colNames;


    public Table(int row, int c){
        rows=row;
        col=c;
        tableData=new String[rows][col];
    }
    public void setColNames(String[]c){
        colNames=c;
    }
    public String[] getColNames() {
        return colNames;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public int getRows(){
        return rows;
    }
    public int getCols(){
        return col;
    }
    public void setRows(int r){
        rows=r;
    }
    public void setCols(int c){
        col=c;
    }
    public void setDataSet(String[][]d){
        tableData=d;
    }
    public String[][] getTableData(){
        return tableData;
    }
    public void setTableData(int r, int c, String d){
        tableData[r][c]=d;
    }
}
