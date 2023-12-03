package Interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportStructure implements Serializable {
    private List<ComponentInfo> componentInfoList;
    String name;
    String filename;

    public ReportStructure(String n, String file) {
        componentInfoList = new ArrayList<>();
        name=n;
        filename=file;
    }

    public String getFilename() {
        return filename;
    }

    public void addComponentInfo(ComponentInfo componentInfo) {
        componentInfoList.add(componentInfo);
    }
    public String getName(){
        return name;
    }
    public List<ComponentInfo> getComponentInfoList() {
        return componentInfoList;
    }
}
