package edu.drexel.cs338.ui;

import edu.drexel.cs338.constants.UIConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Angel on 8/15/2016.
 */
public class CreateWhiteboardScreen extends JPanel {
    AppController controller;

    public CreateWhiteboardScreen(AppController controller) {
        super();
        this.controller = controller;
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        initComponents();
    }

    private void initComponents() {
        JLabel title = new JLabel("Create Whiteboard");
        title.setFont(new Font("Sans-Serif", Font.BOLD, 75));
        title.setBorder(BorderFactory.createEmptyBorder(50, 50, 100, 50));

        JPanel middle = new JPanel(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Your Name: ");
        JTextField nameField = new JTextField();

        JLabel whiteboardLabel = new JLabel("Whiteboard Name: ");
        JTextField whiteBoardTextField = new JTextField();

        JLabel passwordLabel = new JLabel("Password (Optional): ");
        JTextField passwordTextField = new JTextField();

        middle.add(nameLabel);
        middle.add(nameField);
        middle.add(whiteboardLabel);
        middle.add(whiteBoardTextField);
        middle.add(passwordLabel);
        middle.add(passwordTextField);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        JButton createButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> controller.goBack());

        bottomPanel.add(createButton);
        bottomPanel.add(cancelButton);

        add(title);
        add(middle);
        add(bottomPanel);
    }
}
