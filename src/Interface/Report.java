package Interface;

import Buisness.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Report extends JFrame {
    protected PieChartPanel pieChartPanel;
    ReportStructure reportStructure;
    protected BarGraphPanel barGraphPanel;
    protected JPanel sidebarPanel;
    protected JPanel contentPanel;
    protected TablePanel tablePanel;
    protected TextPanel textPanel;
    protected ImagePanel imagePanel;
    protected LineGraphPanel lineGraphPanel;
    protected JMenu mainMenu;
    private List<ReportStructure> reportStructureList;
    DefaultListModel<String> listModel;
    DefaultListModel<String> barGraphListModel;
    DefaultListModel<String> textListModel;
    DefaultListModel<String>tableListModel;
    DefaultListModel<String>pieChartListModel;
    BarGraphStorage barGraphStorage;
    PieChartStorage pieChartStorage;
    TextStorage textStorage;
    TableStorage tableStorage;


    public Report() {
        reportStructureList=new ArrayList<>();
        listModel = new DefaultListModel<>();
        barGraphListModel=new DefaultListModel<>();
        pieChartListModel=new DefaultListModel<>();
        tableListModel=new DefaultListModel<>();
        textListModel=new DefaultListModel<>();
        initializeComponents();
        setupUI();
    }

    private void initializeComponents() {
        pieChartPanel = new PieChartPanel();
        barGraphPanel = new BarGraphPanel();
        tablePanel = new TablePanel();
        textPanel = new TextPanel();
        imagePanel = new ImagePanel();
        lineGraphPanel = new LineGraphPanel();
        //loadPanel = new LoadPanel(Report.this);

        barGraphStorage=new BarGraphStorage();
        pieChartStorage=new PieChartStorage();
        textStorage=new TextStorage();
        tableStorage=new TableStorage();
    }

    private void setupUI() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel = new JPanel(new GridLayout(2, 2)); // Use GridLayout for contentPanel

        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(Color.DARK_GRAY);
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        JButton pieChartButton = createPieChartButton("Pie Chart");
        JButton barGraphButton = createBarGraphButton("Bar Graph");
        JButton imageButton = createImageButton("Image");  // Move this line before lineGraphButton
        JButton lineGraphButton = createLineGraphButton("Line Graph");
        JButton textButton = createtextButton("Text");
        JButton tableButton = createTableButton("Table");

        sidebarPanel.add(pieChartButton);
        sidebarPanel.add(barGraphButton);
        sidebarPanel.add(imageButton);
        sidebarPanel.add(lineGraphButton);
        sidebarPanel.add(textButton);
        sidebarPanel.add(tableButton);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mainMenu = new JMenu("File"); // You might want to give your menu a name
        menuBar.add(mainMenu);

        JMenuItem save = new JMenuItem("Save Structure");
        JMenuItem load = new JMenuItem("Load");
        JMenuItem saveAsPNG = new JMenuItem("Save as PNG"); // New menu item
        JMenuItem saveAsPDF = new JMenuItem("Save as PDF"); // New menu item


        mainMenu.add(load);
        mainMenu.add(save);
        mainMenu.add(saveAsPNG); // Add "Save as PNG" to the menu
        mainMenu.add(saveAsPDF); // Add "Save as PDF" to the menu


        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveReportStructure();
            }
        });
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialog();
            }
        });
        saveAsPNG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAsPNGAction();
            }
        });

        saveAsPDF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //saveAsPDFAction();            //add pdf
            }
         });

        //contentPanel.setSize((int) Math.round(8.27 * 72), (int) Math.round(11.69 * 72));

        //getContentPane().add(BorderLayout.NORTH,mainMenu);
        getContentPane().add(BorderLayout.WEST, sidebarPanel);
        getContentPane().add(BorderLayout.CENTER, contentPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(BorderLayout.CENTER, scrollPane);

        setVisible(true);
    }
    //save as pdf implementation here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void saveAsPNGAction() {
        try {
            // Create a robot for taking screenshots
            Robot robot = new Robot();

            // Set the coordinates and dimensions of the region to capture (adjust these values)
            int x = 95;
            int y = 50;
            int width = 1500;
            int height = 760;

            // Capture a screenshot of the specified region
            BufferedImage screenshot = robot.createScreenCapture(new Rectangle(x, y, width, height));

            // Save the screenshot as a PNG file (adjust the file path)
            File outputfile = new File("output.png");
            ImageIO.write(screenshot, "png", outputfile);

            System.out.println("Screenshot saved successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        JList<String> jList = new JList<>(listModel);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = jList.getSelectedValue();
                if (selectedValue != null) {
                    performOperations(selectedValue);
                    ((JDialog) SwingUtilities.getWindowAncestor(jList)).dispose();
                } else {
                    JOptionPane.showMessageDialog(Report.this, "Please select an option");
                }
            }
        });

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.add(new JScrollPane(jList), BorderLayout.CENTER);
        dialogPanel.add(okButton, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(Report.this, "Select an Option", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(dialogPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private void performOperations( String selectedReport) {
        ReportStructure reportStructure = null;
        for (ReportStructure r : reportStructureList) {
            if (selectedReport.equals(r.name)) {
                reportStructure = loadReportStructure(r.filename);
                break;
            }
        }
        loadReportStructureUtility(reportStructure);
    }
    private JButton createPieChartButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.add(pieChartPanel);
            }
        });

        // Add right-click context menu
        JPopupMenu contextMenu = createPieChartContextMenu();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    contextMenu.show(button, e.getX(), e.getY());
                }
            }
        });

        return button;
    }

    private JButton createImageButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.add(imagePanel);
            }
        });

        // Add right-click context menu
        JPopupMenu contextMenu = createImageContextMenu();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    contextMenu.show(button, e.getX(), e.getY());
                }
            }
        });

        return button;
    }

    private JButton createBarGraphButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add right-click context menu
        JPopupMenu contextMenu = createBarGraphContextMenu();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.add(barGraphPanel);
            }
        });
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    contextMenu.show(button, e.getX(), e.getY());
                }
            }
        });
        return button;
    }

    private JButton createTableButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add right-click context menu
        JPopupMenu contextMenu = createTableContextMenu();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.add(tablePanel);
            }
        });
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    contextMenu.show(button, e.getX(), e.getY());
                }
            }
        });

        return button;
    }

    private JButton createLineGraphButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.removeAll();
                contentPanel.add(lineGraphPanel); // Add lineGraphPanel to contentPanel
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });

        // Add right-click context menu
        JPopupMenu contextMenu = createLineGraphContextMenu();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    contextMenu.show(button, e.getX(), e.getY());
                }
            }
        });

        return button;
    }

    private JButton createtextButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPopupMenu contextMenu = createtextContextMenu();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    contextMenu.show(button, e.getX(), e.getY());
                }
            }
        });
        button.addActionListener(e -> {
            contentPanel.add(textPanel);
        });
        return button;
    }

    private JPopupMenu createBarGraphContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Add new");
        JMenuItem menuItem1 = new JMenuItem("Load from file");
        JMenuItem menuItem2 = new JMenuItem("Load from saved");
        //menuItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "Context menu item clicked"));
        menu.add(menuItem);
        menu.add(menuItem1);
        menu.add(menuItem2);
        menuItem.addActionListener(e -> {
            //String dataset = JOptionPane.showInputDialog("Enter Pie Chart dataset with the item names (e.g. abc:43,def:56):");

            barGraphPanel.addBarChart();

        });
        menuItem1.addActionListener(e -> {
            barGraphPanel.loadFromFile();
        });
        menuItem2.addActionListener(e -> {
            showBarDialog();
        });

        return menu;
    }

    private JPopupMenu createTableContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Add new");
        JMenuItem menuItem1 = new JMenuItem("Load from file");
        JMenuItem menuItem2 = new JMenuItem("Load from saved");
        //menuItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "Context menu item clicked"));
        menu.add(menuItem);
        menu.add(menuItem1);
        menu.add(menuItem2);
        menuItem.addActionListener(e -> {
            tablePanel.collectData();
        });
        menuItem1.addActionListener(e -> {
            tablePanel.loadFromFile();
        });
        menuItem2.addActionListener(e->{
            showTableDialog();
        });
        return menu;
    }

    private JPopupMenu createLineGraphContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Add new");
        JMenuItem menuItem1 = new JMenuItem("Load from file");
        JMenuItem menuItem2 = new JMenuItem("Load from saved");
        //menuItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "Context menu item clicked"));
        menu.add(menuItem);
        menu.add(menuItem1);
        menu.add(menuItem2);
        menuItem.addActionListener(e -> {
            String dataset = JOptionPane.showInputDialog("Enter Pline Chart dataset with the item names (e.g. abc:43,def:56):");
            if (dataset != null && !dataset.isEmpty()) {
                lineGraphPanel.addLineChart();
            }
        });
        menuItem1.addActionListener(e -> {
            lineGraphPanel.loadFromFile();
        });
        menuItem2.addActionListener(e -> {
            lineGraphPanel.loadFromDB();
        });
        return menu;
    }

    private JPopupMenu createPieChartContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Add new");
        JMenuItem menuItem1 = new JMenuItem("Load from file");
        JMenuItem menuItem2 = new JMenuItem("Load from saved");
        //menuItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "Context menu item clicked"));
        menu.add(menuItem);
        menu.add(menuItem1);
        menu.add(menuItem2);

        menuItem.addActionListener(e -> {
            String dataset = JOptionPane.showInputDialog("Enter Pie Chart dataset with the item names (e.g. abc:43,def:56):");
            if (dataset != null && !dataset.isEmpty()) {
                pieChartPanel.addPieChart();
            }
        });
        menuItem1.addActionListener(e -> {
            pieChartPanel.loadPieChart();
        });
        menuItem2.addActionListener(e -> {
            showPieDialog();
        });
        return menu;
    }
    private JPopupMenu createtextContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Add new");
        JMenuItem menuItem1 = new JMenuItem("Load from file");
        JMenuItem menuItem2 = new JMenuItem("Load from saved");
        //menuItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "Context menu item clicked"));
        menu.add(menuItem);
        menu.add(menuItem1);
        menu.add(menuItem2);
        menuItem.addActionListener(e -> {
            textPanel.addText();
        });
        menuItem1.addActionListener(e -> {
            textPanel.loadText();
        });
        menuItem2.addActionListener(e->{
            showTextDialog();
        });
        return menu;
    }

    private JPopupMenu createImageContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Add new");
        menu.add(menuItem);
        menuItem.addActionListener(e -> {
            imagePanel.addImage();
        });

        return menu;
    }

    public void saveReportStructure() {
        String input = JOptionPane.showInputDialog("Enter the name for your report");

        String filePath=input + ".txt";
        reportStructure = new ReportStructure(input,filePath );
        reportStructureList.add(reportStructure);
        listModel.addElement(reportStructure.name);
        //loadPanel.addReport(reportStructure);

        Component[] components = contentPanel.getComponents();

        for (Component component : components) {
            int row = contentPanel.getComponentZOrder(component) / 2;
            int column = contentPanel.getComponentZOrder(component) % 2;

            if (component instanceof PieChartPanel) {
                reportStructure.addComponentInfo(new ComponentInfo("Pie Chart", "PieChart1", row, column));
            } else if (component instanceof BarGraphPanel) {
                reportStructure.addComponentInfo(new ComponentInfo("Bar Graph", "BarGraph1", row, column));
            } else if (component instanceof ImagePanel) {
                reportStructure.addComponentInfo(new ComponentInfo("Image", "Image1", row, column));
            } else if (component instanceof LineGraphPanel) {
                reportStructure.addComponentInfo(new ComponentInfo("Line Graph", "LineGraph1", row, column));
            } else if (component instanceof TextPanel) {
                reportStructure.addComponentInfo(new ComponentInfo("Text", "Text1", row, column));
            } else if (component instanceof TablePanel) {
                reportStructure.addComponentInfo(new ComponentInfo("Table", "Table1", row, column));
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(reportStructure);
            System.out.println("Report structure saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
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

    public void loadReportStructureUtility(ReportStructure loadedStructure) {
        contentPanel.removeAll();
        if (loadedStructure != null) {
           // contentPanel.removeAll();  // Clear existing components

            for (ComponentInfo componentInfo : loadedStructure.getComponentInfoList()) {
                int row = componentInfo.getGridRow();
                int column = componentInfo.getGridColumn();

                if (componentInfo.getComponentType().equals("Bar Graph")) {
                    System.out.println("bar");
                    barGraphPanel=new BarGraphPanel();
                    contentPanel.add(barGraphPanel);
                   // contentPanel.setComponentZOrder(barGraphPanel, row * 2 + column);
                } else if (componentInfo.getComponentType().equals("Line Graph")) {
                    lineGraphPanel=new LineGraphPanel();
                    contentPanel.add(lineGraphPanel);
                   // contentPanel.setComponentZOrder(lineGraphPanel, row * 2 + column);
                } else if (componentInfo.getComponentType().equals("Pie Chart")) {
                    pieChartPanel=new PieChartPanel();
                    contentPanel.add(pieChartPanel);
                    //contentPanel.setComponentZOrder(pieChartPanel, row * 2 + column);
                } else if (componentInfo.getComponentType().equals("Image")) {
                    imagePanel=new ImagePanel();
                    contentPanel.add(imagePanel);
                   // contentPanel.setComponentZOrder(imagePanel, row * 2 + column);
                } else if (componentInfo.getComponentType().equals("Text")) {
                    textPanel=new TextPanel();
                    contentPanel.add(textPanel);
                    //contentPanel.setComponentZOrder(textPanel, row * 2 + column);
                } else if (componentInfo.getComponentType().equals("Table")) {
                    tablePanel=new TablePanel();
                    contentPanel.add(tablePanel);
                    //contentPanel.setComponentZOrder(tablePanel, row * 2 + column);
                }
            }
        }
    }
    void populateBarListModel() {
        List<BarGraph> list = barGraphStorage.getBarGraphList();

        for (BarGraph b : list) {
            String idString = String.valueOf(b.getId());

            if (!barGraphListModel.contains(idString)) {
                barGraphListModel.addElement(idString);
                //System.out.println(b.getId());
            }
        }
    }
    private void showBarDialog () {
        populateBarListModel();
        JList<String> jList = new JList<>(barGraphListModel);
        System.out.println(barGraphListModel.get(0));
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = jList.getSelectedValue();
                if (selectedValue != null) {
                    BarGraph b=findBarGraph(selectedValue);
                    assert b != null;
                    barGraphPanel.loadFromSaved(b);
                    SwingUtilities.getWindowAncestor(jList).dispose();
                } else {
                    JOptionPane.showMessageDialog(Report.this, "Please select an option");
                }
            }
        });
        JPanel dialogPanel = new JPanel(new BorderLayout());
        //dialogPanel.add(jList);
        dialogPanel.add(new JScrollPane(jList), BorderLayout.CENTER);
        dialogPanel.add(okButton, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(Report.this, "Select an Option", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(dialogPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private BarGraph findBarGraph(String selected){
        List<BarGraph>list=barGraphStorage.getBarGraphList();
        for(int i=0;i<list.size();i++){
            if(Integer.parseInt(selected)==list.get(i).getId()){
                return list.get(i);
            }
        }
        return null;
    }
    void populatePieListModel() {
        List<PieChart> list = pieChartStorage.getPieChartList() ;

        for (PieChart b : list) {
            String idString = String.valueOf(b.getId());

            if (!pieChartListModel.contains(idString)) {
                pieChartListModel.addElement(idString);
                //System.out.println(b.getId());
            }
        }
    }
    private void showPieDialog () {
        populatePieListModel();
        JList<String> jList = new JList<>(pieChartListModel);
        //System.out.println(barGraphListModel.get(0));
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = jList.getSelectedValue();
                if (selectedValue != null) {
                    PieChart b=findPieChart(selectedValue);
                    assert b != null;
                    pieChartPanel.loadFromSaved(b);
                    SwingUtilities.getWindowAncestor(jList).dispose();
                } else {
                    JOptionPane.showMessageDialog(Report.this, "Please select an option");
                }
            }
        });
        JPanel dialogPanel = new JPanel(new BorderLayout());
        //dialogPanel.add(jList);
        dialogPanel.add(new JScrollPane(jList), BorderLayout.CENTER);
        dialogPanel.add(okButton, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(Report.this, "Select an Option", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(dialogPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private PieChart findPieChart(String selected){
        List<PieChart>list=pieChartStorage.getPieChartList();
        for(int i=0;i<list.size();i++){
            if(Integer.parseInt(selected)==list.get(i).getId()){
                return list.get(i);
            }
        }
        return null;
    }
    void populateTextListModel() {
        List<Text> list = textStorage.getTextList() ;

        for (Text b : list) {
            String idString = String.valueOf(b.getId());

            if (!textListModel.contains(idString)) {
                textListModel.addElement(idString);
            }
        }
    }
    private void showTextDialog () {
        populateTextListModel();
        JList<String> jList = new JList<>(textListModel);
        //System.out.println(barGraphListModel.get(0));
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = jList.getSelectedValue();
                if (selectedValue != null) {
                    Text b=findText(selectedValue);
                    assert b != null;
                    textPanel.loadFromSaved(b);
                    SwingUtilities.getWindowAncestor(jList).dispose();
                } else {
                    JOptionPane.showMessageDialog(Report.this, "Please select an option");
                }
            }
        });
        JPanel dialogPanel = new JPanel(new BorderLayout());
        //dialogPanel.add(jList);
        dialogPanel.add(new JScrollPane(jList), BorderLayout.CENTER);
        dialogPanel.add(okButton, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(Report.this, "Select an Option", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(dialogPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private Text findText(String selected){
        List<Text>list=textStorage.getTextList();
        for(int i=0;i<list.size();i++){
            if(Integer.parseInt(selected)==list.get(i).getId()){
                return list.get(i);
            }
        }
        return null;
    }
    void populateTableListModel() {
        List<Table> list = tablePanel.getTableList() ;

        for (Table b : list) {
            String idString = b.getName();

            if (!tableListModel.contains(idString)) {
                tableListModel.addElement(idString);
            }
        }
    }
    void setTables(){
        tableStorage.loadList(tablePanel.getTableList());
    }
    private void showTableDialog () {
        setTables();
        populateTableListModel();
        JList<String> jList = new JList<>(tableListModel);
        //System.out.println(barGraphListModel.get(0));
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = jList.getSelectedValue();
                if (selectedValue != null) {
                    Table b=findTable(selectedValue);
                    assert b != null;
                    tablePanel.loadFromSaved(b);
                    SwingUtilities.getWindowAncestor(jList).dispose();
                } else {
                    JOptionPane.showMessageDialog(Report.this, "Please select an option");
                }
            }
        });
        JPanel dialogPanel = new JPanel(new BorderLayout());
        //dialogPanel.add(jList);
        dialogPanel.add(new JScrollPane(jList), BorderLayout.CENTER);
        dialogPanel.add(okButton, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(Report.this, "Select an Option", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(dialogPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private Table findTable(String selected){
        List<Table>list=tableStorage.getTableList();
        for(int i=0;i<list.size();i++){
            if(Objects.equals(selected, list.get(i).getName())){
                return list.get(i);
            }
        }
        return null;
    }
}