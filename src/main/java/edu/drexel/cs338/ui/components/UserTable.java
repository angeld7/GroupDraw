package edu.drexel.cs338.ui.components;

import edu.drexel.cs338.data.UserTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Angel on 8/22/2016.
 */
public class UserTable extends JTable {
    @Override
    public String getToolTipText(MouseEvent e) {
        Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        return getValueAt(rowIndex, colIndex).toString();
    }
}
