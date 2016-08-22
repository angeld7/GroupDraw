package edu.drexel.cs338.ui;

import edu.drexel.cs338.constants.UIConstants;
import edu.drexel.cs338.data.WhiteboardTableModel;
import edu.drexel.cs338.ui.components.*;

import javax.swing.*;
import java.awt.*;


/**
 * Created by Angel on 8/21/2016.
 */
public class JoinWhiteboardScreen extends JPanel {
    private AppController controller;
    private JLabel title;
    private JButton joinButton;
    private JTable whiteboardList;

    public JoinWhiteboardScreen(AppController controller) {
        this.controller = controller;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        intComponents();
        addComponents();
    }

    private void intComponents() {
        title = new JLabel(UIConstants.JOIN_WHITEBOARD);
        title.setFont(new Font("Sans-Serif", Font.BOLD, 25));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        title.setAlignmentX(CENTER_ALIGNMENT);

        String[][] data = new String[4][4];
        for (String[] thing : data) {
            for (int x = 0; x < thing.length; x++) {
                thing[x] = "data!";
            }
        }
        whiteboardList = new JTable();
        WhiteboardTableModel model = new WhiteboardTableModel(whiteboardList);
        whiteboardList.setModel(model);
        whiteboardList.setFillsViewportHeight(true);

        whiteboardList.setRowSelectionAllowed(true);
        whiteboardList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        whiteboardList.getSelectionModel().addListSelectionListener(e -> {
            if (whiteboardList.getSelectedRow() >= 0) {
                joinButton.setEnabled(true);
            }
        });

        joinButton = new JButton(UIConstants.JOIN);
        joinButton.addActionListener(e ->
                controller.display(new WhiteboardPreviewScreen(controller, model.getRow(whiteboardList.getSelectedRow())))
        );
        joinButton.setEnabled(false);
    }

    private void addComponents() {

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(joinButton);
        buttonPanel.add(new CancelButton(controller));

        JScrollPane scrollPane = new JScrollPane(whiteboardList);

        add(title);
        add(scrollPane);
        add(Box.createVerticalGlue());
        add(buttonPanel);
    }


}
