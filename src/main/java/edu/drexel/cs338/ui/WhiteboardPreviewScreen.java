package edu.drexel.cs338.ui;

import com.google.common.collect.Lists;
import edu.drexel.cs338.constants.UIConstants;
import edu.drexel.cs338.data.FirebaseController;
import edu.drexel.cs338.data.UserTableModel;
import edu.drexel.cs338.data.Whiteboard;
import edu.drexel.cs338.interfaces.InputValidationPassFailHandler;
import edu.drexel.cs338.interfaces.WhiteboardDeleteListener;
import edu.drexel.cs338.ui.components.*;
import edu.drexel.cs338.utility.FormUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by Angel on 8/21/2016.
 */
public class WhiteboardPreviewScreen extends JPanel {

    /**
     * Specifies the fields that ar currently failing validation
     */
    final private Set<JComponent> erroredComponents = new HashSet<>();

    /**
     * All fields on the form
     */

    final private java.util.List<JComponent> fields = new LinkedList<>();
    private AppController controller;
    private Whiteboard whiteboard;
    private JLabel title;
    private JButton joinButton;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JTable userTable;
    private JLabel errorLabel;
    private JButton cancelButton;

    WhiteboardDeleteListener deleteListener = new WhiteboardDeleteListener() {
        @Override
        public void onDelete(Whiteboard whiteboard) {
            if (whiteboard.equals(whiteboard)) {
                controller.goBack();
            }
            FirebaseController.get().removeDeleteListener(this);
        }
    };

    InputValidationPassFailHandler passFailHandler = new InputValidationPassFailHandler() {
        @Override
        public void fail(String message, JComponent component) {
            synchronized (erroredComponents) {
                erroredComponents.add(component);
            }
            errorLabel.setText(message);
        }

        @Override
        public void pass(JComponent component) {
            synchronized (erroredComponents) {
                erroredComponents.remove(component);
            }
            errorLabel.setText("");
        }
    };

    public WhiteboardPreviewScreen(AppController controller, Whiteboard whiteboard) {
        super(new BorderLayout());
        this.controller = controller;
        this.whiteboard = whiteboard;
        initComponents();
        addComponents();
        FirebaseController.get().addDeleteListener(deleteListener);
    }

    private void initComponents() {
        title = new JLabel(whiteboard.getName());
        title.setFont(new Font("Sans-Serif", Font.BOLD, 25));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setHorizontalAlignment(JLabel.CENTER);
        setAlignmentX(CENTER_ALIGNMENT);

        userTable = new UserTable();
        UserTableModel model = new UserTableModel(whiteboard.getName(), userTable);
        userTable.setModel(model);

        ActionListener actionListener = e -> {
            if (!joinButton.isEnabled()) return;
            if (!hasErrors()) {
                if (model.containsUser(nameField.getText())) {
                    FormUtility.validationFailed(nameField);
                    nameField.requestFocus();
                    passFailHandler.fail(UIConstants.NAME_IN_USE, nameField);
                } else if (!whiteboard.getPassword().isEmpty() && !whiteboard.getPassword().equals(String.valueOf(passwordField.getPassword()))) {
                    FormUtility.validationFailed(passwordField);
                    passwordField.requestFocus();
                    passFailHandler.fail(UIConstants.INCORRECT_PASSWORD, passwordField);
                } else {
                    FirebaseController.get().removeDeleteListener(deleteListener);
                    WhiteboardScreen screen = new WhiteboardScreen(controller, whiteboard, nameField.getText());
                    controller.display(screen);
                    screen.createImage();
                }
            }
        };

        nameField = new JTextField();
        nameField.addActionListener(actionListener);
        if (!whiteboard.getPassword().isEmpty()) {
            passwordField = new JPasswordField();
            passwordField.addActionListener(actionListener);
        }

        cancelButton = new CancelButton(controller);
        cancelButton.addActionListener(e -> FirebaseController.get().removeDeleteListener(deleteListener));

        joinButton = new JButton(UIConstants.JOIN);
        joinButton.addActionListener(actionListener);

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        FormUtility.addRequiredValidator(nameField, passFailHandler, UIConstants.YOUR_NAME);
        fields.add(nameField);
        if (!whiteboard.getPassword().isEmpty()) {
            fields.add(passwordField);
            FormUtility.addRequiredValidator(passwordField, passFailHandler, UIConstants.WHITEBOARD_PASSWORD);
        }

        SwingUtilities.invokeLater(() -> nameField.requestFocus());
    }

    private void addComponents() {

        JPanel middlePanel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        FormUtility.addRequiredLabel(UIConstants.YOUR_NAME, form);
        FormUtility.addLastField(nameField, form);
        if (passwordField != null) {
            FormUtility.addRequiredLabel(UIConstants.WHITEBOARD_PASSWORD, form);
            FormUtility.addLastField(passwordField, form);
        }

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(400, 800));

        middlePanel.add(form, BorderLayout.CENTER);
        middlePanel.add(scrollPane, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(errorLabel);
        buttonPanel.add(joinButton);
        buttonPanel.add(cancelButton);

        add(title, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }


    /**
     * Validates all fields
     *
     * @return
     */
    public boolean hasErrors() {
        boolean error = false;
        Iterator<JComponent> iterator = fields.iterator();
        while (!error && iterator.hasNext()) {
            JComponent component = iterator.next();
            InputVerifier verifier = component.getInputVerifier();
            if (verifier != null) {
                component.getInputVerifier().verify(component);
            }
        }
        synchronized (erroredComponents) {
            if (erroredComponents.size() > 0) {
                while(iterator.hasNext()) {
                    JComponent component = erroredComponents.iterator().next();
                    InputVerifier verifier = component.getInputVerifier();
                    verifier.verify(component);
                    component.requestFocus();
                    error = true;
                }
            }
        }

        return error;
    }
}
