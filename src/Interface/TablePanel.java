package Interface;

import Buisness.Table;

import javax.swing.*;
import java.awt.*;

public class TablePanel extends JPanel {
    Table table;
    JScrollPane scrollPane;
    JTextArea textArea;

    public TablePanel() {
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textArea);
    }

    void setRows(int r, int c) {
        if (table == null) {
            table = new Table(r, c);
        } else {
            // If the table already exists, you might want to handle this case accordingly
            // For now, I'm assuming you want to create a new table
            table = new Table(r, c);
        }
    }
    public void collectData() {
        int rows = 0, columns = 0;

        String rowsInput = JOptionPane.showInputDialog("Enter number of rows:");
        String columnsInput = JOptionPane.showInputDialog("Enter number of columns:");

        if (rowsInput != null && columnsInput != null) {
            try {
                rows = Integer.parseInt(rowsInput);
                columns = Integer.parseInt(columnsInput);

                setRows(rows, columns);

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        textArea.setText("");
                        int result = JOptionPane.showOptionDialog(
                                this,
                                scrollPane,
                                "Enter Text for cell (" + i + ", " + j + "):",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                null,
                                null
                        );

                        if (result == JOptionPane.OK_OPTION) {
                            table.setTableData(i, j, textArea.getText());
                        }
                        else {
                        }
                    }
                }
                repaint();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for rows and columns.");
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Check if table is null
        if (table != null) {
            int cellWidth = getWidth() / table.getTableData()[0].length;
            int cellHeight = getHeight() / table.getTableData().length;

            for (int i = 0; i < table.getTableData().length; i++) {
                for (int j = 0; j < table.getTableData()[0].length; j++) {
                    if (table.getTableData()[i][j] != null) {
                        g.drawString(table.getTableData()[i][j], j * cellWidth, (i + 1) * cellHeight);
                    }
                }
            }

            for (int i = 0; i <= table.getTableData().length; i++) {
                g.drawLine(0, i * cellHeight, getWidth(), i * cellHeight);
            }

            for (int j = 0; j <= table.getTableData()[0].length; j++) {
                g.drawLine(j * cellWidth, 0, j * cellWidth, getHeight());
            }
        }
    }
}
