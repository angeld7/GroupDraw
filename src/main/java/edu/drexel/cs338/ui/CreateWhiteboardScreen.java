package edu.drexel.cs338.ui;

import edu.drexel.cs338.constants.UIConstants;
import edu.drexel.cs338.interfaces.PassFailHandler;
import edu.drexel.cs338.data.FirebaseController;
import edu.drexel.cs338.data.Whiteboard;
import edu.drexel.cs338.interfaces.InputValidationPassFailHandler;
import edu.drexel.cs338.utility.FormUtility;
import edu.drexel.cs338.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Angel on 8/15/2016.
 */
public class CreateWhiteboardScreen extends JPanel {

    /**
     * Specifies the fields that ar currently failing validation
     */
    final private Set<JComponent> erroredComponents = new HashSet<>();

    /**
     * All fields on the form
     */
    final private List<JComponent> fields = new LinkedList<>();

    AppController controller;

    JTextField nameField;
    JLabel title;
    JTextField whiteBoardTextField;
    JTextField passwordTextField;
    JLabel errorLabel;
    JButton cancelButton;
    JButton createButton;

    public CreateWhiteboardScreen(AppController controller) {
        super();
        this.controller = controller;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initComponents();
        addComponents();
    }

    private void initComponents() {
        title = new JLabel(UIConstants.CREATE_WHITEBOARD);
        title.setFont(new Font("Sans-Serif", Font.BOLD, 25));
        title.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        title.setAlignmentX(CENTER_ALIGNMENT);
        nameField = new JTextField();
        whiteBoardTextField = new JTextField();
        passwordTextField = new JTextField();
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        createButton = new JButton(UIConstants.CREATE);
        cancelButton = new CancelButton(controller);
        fields.add(nameField);
        fields.add(whiteBoardTextField);

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

        FormUtility.addRequiredValidator(nameField, passFailHandler, UIConstants.YOUR_NAME);
        FormUtility.addRequiredValidator(whiteBoardTextField, passFailHandler, UIConstants.WHITEBOARD_NAME);

        createButton.addActionListener(e -> {
            createButton.setEnabled(false);
            cancelButton.setEnabled(false);
            if (!hasErrors()) {
                Whiteboard whiteboard = new Whiteboard(
                        whiteBoardTextField.getText(),
                        nameField.getText(),
                        passwordTextField.getText());

                WhiteboardScreen screen = new WhiteboardScreen(controller, whiteboard);

                FirebaseController.get().createWhiteboard(whiteboard, screen.getDrawHandler(), new PassFailHandler() {
                    @Override
                    public void pass() {
                        controller.display(screen);
                        screen.createImage();
                    }

                    @Override
                    public void fail() {
                        createButton.setEnabled(true);
                        cancelButton.setEnabled(true);
                        FormUtility.validationFailed(whiteBoardTextField);
                        whiteBoardTextField.requestFocus();
                        passFailHandler.fail(UIConstants.WHITEBOARD_NAME_IN_USE, whiteBoardTextField);
                    }
                });
            }
        });
    }

    private void addComponents() {
        add(title);
        JPanel formPanel = new JPanel(new GridBagLayout());
        FormUtility.addRequiredLabel(UIConstants.YOUR_NAME, formPanel);
        FormUtility.addLastField(nameField, formPanel);

        FormUtility.addRequiredLabel(UIConstants.WHITEBOARD_NAME, formPanel);
        FormUtility.addLastField(whiteBoardTextField, formPanel);

        FormUtility.addLabel(UIConstants.PASSWORD_OPTIONAL, formPanel);
        FormUtility.addLastField(passwordTextField, formPanel);

        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(errorLabel);
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        add(formPanel);
        add(Box.createVerticalGlue());
        add(buttonPanel);
    }

    /**
     * Validates all fields
     *
     * @return
     */
    public boolean hasErrors() {
        boolean error;
        synchronized (erroredComponents) {
            error = erroredComponents.size() > 0;
        }
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
                JComponent component = erroredComponents.iterator().next();
                InputVerifier verifier = component.getInputVerifier();
                verifier.verify(component);
                component.requestFocus();
                error = true;
            }
        }

        return error;
    }

}
