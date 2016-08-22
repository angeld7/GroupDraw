package edu.drexel.cs338.ui;

import com.google.common.collect.Lists;
import edu.drexel.cs338.constants.UIConstants;
import edu.drexel.cs338.data.Whiteboard;
import edu.drexel.cs338.ui.components.*;
import edu.drexel.cs338.utility.FormUtility;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Angel on 8/21/2016.
 */
public class WhiteboardPreviewScreen extends JPanel{
    private AppController controller;
    private Whiteboard whiteboard;
    private JLabel title;
    private JButton joinButton;
    private JTextField nameField;
    private JTextField passwordField;
    private JTable userTable;

    public WhiteboardPreviewScreen(AppController controller, Whiteboard whiteboard) {
        super(new BorderLayout());
        this.controller = controller;
        this.whiteboard = whiteboard;
        initComponents();
        addComponents();
    }

    private void initComponents() {
        title = new JLabel(whiteboard.getName());
        title.setFont(new Font("Sans-Serif", Font.BOLD, 25));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setHorizontalAlignment(JLabel.CENTER);
        setAlignmentX(CENTER_ALIGNMENT);

        nameField = new JTextField();
        if(!whiteboard.getPassword().isEmpty()) {
            passwordField = new JPasswordField();
        }

        //userTable = new JTable(new Vector(whiteboard.getUsers()), Lists.asList("String", null));

        joinButton = new JButton(UIConstants.JOIN);
    }

    private void addComponents() {

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));

        JPanel form = new JPanel(new GridBagLayout());
        FormUtility.addLabel(UIConstants.YOUR_NAME, form);
        FormUtility.addLastField(nameField, form);
        if(passwordField != null) {
            FormUtility.addLabel(UIConstants.WHITEBOARD_PASSWORD, form);
            FormUtility.addLastField(passwordField, form);
        }

        middlePanel.add(form);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(joinButton);
        buttonPanel.add(new CancelButton(controller));

        add(title, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }


}
