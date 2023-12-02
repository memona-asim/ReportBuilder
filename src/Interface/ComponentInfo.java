package Interface;

import java.io.Serializable;

public class ComponentInfo implements Serializable {
    private String componentType;
    private String componentName;
    private int gridRow;
    private int gridColumn;

    public ComponentInfo(String componentType, String componentName, int gridRow, int gridColumn) {
        this.componentType = componentType;
        this.componentName = componentName;
        this.gridRow = gridRow;
        this.gridColumn = gridColumn;
    }

    public String getComponentType() {
        return componentType;
    }

    public String getComponentName() {
        return componentName;
    }

    public int getGridRow() {
        return gridRow;
    }

    public int getGridColumn() {
        return gridColumn;
    }
}
