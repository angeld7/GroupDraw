package edu.drexel.cs338.data;

import com.google.firebase.database.DataSnapshot;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.*;

/**
 * Created by Angel on 8/21/2016.
 */
public class WhiteboardTableModel implements TableModel {

    JTable table;
    List<Whiteboard> data = new ArrayList<>();
    List<String> columns;

    public WhiteboardTableModel(JTable table) {
        this.table = table;
        FirebaseController.get().getWhiteboardList(this);
        columns = Arrays.asList("Name", "Creator", "Users", "Password");
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Whiteboard whiteboard = data.get(rowIndex);
        Object value = null;
        switch (columnIndex) {
            case 0:
                value = whiteboard.getName();
                break;
            case 1:
                value = whiteboard.getCreator();
                break;
            case 2:
                value = whiteboard.getUsers().size();
                break;
            case 3:
                value = whiteboard.getPassword().isEmpty() ? "No" : "Yes";
        }
        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }

    public void setData(List<Whiteboard> data) {
        this.data = data;
    }

    public void addRow(Whiteboard whiteboard) {
        if(!data.contains(whiteboard)) {
            data.add(whiteboard);
            refreshData();
        }
    }

    public void removeRow(Whiteboard whiteboard) {
        data.remove(whiteboard);
        refreshData();
    }

    public void refreshData() {
        table.revalidate();
        table.repaint();
    }
}
