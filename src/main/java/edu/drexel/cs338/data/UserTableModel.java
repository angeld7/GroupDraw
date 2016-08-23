package edu.drexel.cs338.data;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Map;

/**
 * Created by Angel on 8/22/2016.
 */
public class UserTableModel implements TableModel {

    JTable table;
    Map<String, String> data;
    final String COLUMN_NAME = "Users";

    public UserTableModel(String whiteboardName, JTable table) {
        this.table = table;
        FirebaseController.get().getUserList(whiteboardName, this);
    }

    @Override
    public int getRowCount() {
        if (data == null) return 0;
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAME;
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
        return data.values().toArray()[rowIndex];
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

    public void setData(Map<String, String> data) {
        this.data = data;
        refreshData();
    }

    public void refreshData() {
        table.revalidate();
        table.repaint();
    }

    public void addUser(String key, String user) {
        data.put(key, user);
        refreshData();
    }

    public void removeUser(String key) {
        data.remove(key);
        refreshData();
    }

    public boolean containsUser(String user) {
        return data.containsKey(user);
    }

}
