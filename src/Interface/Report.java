package Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Report extends JFrame {
    protected PieChartPanel pieChartPanel;
    protected BarGraphPanel barGraphPanel;
    protected JPanel sidebarPanel;
    protected JPanel contentPanel;
    protected TablePanel tablePanel;

    public Report() {
        initializeComponents();
        setupUI();
    }

    private void initializeComponents() {
        pieChartPanel = new PieChartPanel();
        barGraphPanel = new BarGraphPanel();
        tablePanel = new TablePanel();
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
        JButton lineGraphButton = createLineGraphButton("Line Graph");
        JButton textButton = createtextButton("Text");
        JButton tableButton = createTableButton("Table");

        sidebarPanel.add(pieChartButton);
        sidebarPanel.add(barGraphButton);
        sidebarPanel.add(lineGraphButton);
        sidebarPanel.add(textButton);
        sidebarPanel.add(tableButton);

        getContentPane().add(BorderLayout.WEST, sidebarPanel);
        getContentPane().add(BorderLayout.CENTER, contentPanel);

        setVisible(true);
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

        // Add right-click context menu
        JPopupMenu contextMenu = createtextContextMenu();
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
            String dataset = JOptionPane.showInputDialog("Enter Pie Chart dataset with the item names (e.g. abc:43,def:56):");
            if (dataset != null && !dataset.isEmpty()) {
                barGraphPanel.addBarChart();
            }
        });
        menuItem1.addActionListener(e -> {
            barGraphPanel.loadFromFile();
        });
        menuItem2.addActionListener(e -> {
            barGraphPanel.saveBarGraph(this);
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
            pieChartPanel.savePieChart(this);
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

        return menu;
    }

//    private void saveAsPNG() {
//        try {
//            // Get the current chart panel
//            JPanel currentChartPanel = (JPanel) chartContainer.getComponent(0);
//            Rectangle bounds = currentChartPanel.getBounds();
//
//            // Create an image of the chart panel
//            BufferedImage screenshot = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
//            Graphics g = screenshot.getGraphics();
//            currentChartPanel.paint(g);
//            g.dispose();
//
//            // Save the image to a file
//            File file = new File("chart_image.png");
//            ImageIO.write(screenshot, "png", file);
//
//            JOptionPane.showMessageDialog(this, "Chart saved as chart_image.png");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error saving the chart image");
//        }
//    }
//    private void saveAsPDF() {
//        try {
//            // Get the current chart panel
//            JPanel currentChartPanel = (JPanel) chartContainer.getComponent(0);
//            Rectangle bounds = currentChartPanel.getBounds();
//
//            // Create an image of the chart panel
//            BufferedImage screenshot = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
//            Graphics g = screenshot.getGraphics();
//            currentChartPanel.paint(g);
//            g.dispose();
//
//            // Create a PDF document and add the chart image
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream("chart_output.pdf"));
//            document.open();
//            com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(toByteArray(screenshot));
//            document.add(pdfImage);
//            document.close();
//
//            JOptionPane.showMessageDialog(this, "Chart saved as chart_output.pdf");
//        } catch (IOException | DocumentException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error saving the chart as PDF");
//        }
//    }
//    private byte[] toByteArray(BufferedImage image) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(image, "png", baos);
//        return baos.toByteArray();
//    }
//

    //    private static JPanel createCard(String text) {
//        JPanel card = new JPanel();
//        card.add(new JLabel(text, SwingConstants.CENTER));
//        return card;
//    }

}