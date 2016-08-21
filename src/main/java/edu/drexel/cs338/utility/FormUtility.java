package edu.drexel.cs338.utility;

import edu.drexel.cs338.interfaces.InputValidationPassFailHandler;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.*;
import java.util.Date;

/*
 * Angel Delgado
 * ald363@drexel.edu
 * CS338:GUI, Assignment 2, Trip Tracker
 */

/**
 * This utility class is used to streamline building forms and to keep them consistent.
 */
public class FormUtility {

    /**
     * A last field will take up the rest of the columns on the row
     */
    private static final GridBagConstraints LAST_CONSTRAINTS;
    /**
     * Labels just take up 1 column
     */
    private static final GridBagConstraints LABEL_CONSTRAINTS;

    static {
        LAST_CONSTRAINTS = new GridBagConstraints();
        LAST_CONSTRAINTS.fill = GridBagConstraints.HORIZONTAL;
        LAST_CONSTRAINTS.anchor = GridBagConstraints.NORTHWEST;
        LAST_CONSTRAINTS.weightx = 1.0;
        LAST_CONSTRAINTS.gridwidth = GridBagConstraints.REMAINDER;
        LAST_CONSTRAINTS.insets = new Insets(3, 3, 3, 100);

        LABEL_CONSTRAINTS = (GridBagConstraints) LAST_CONSTRAINTS.clone();
        LABEL_CONSTRAINTS.insets = new Insets(0,50,0,50);
        LABEL_CONSTRAINTS.weightx = 0.0;
        LABEL_CONSTRAINTS.gridwidth = 1;
    }

    /**
     * Adds a label to the parent component containing the specified text
     *
     * @param text
     * @param parent
     */
    public static void addLabel(String text, Container parent) {
        JLabel label = new JLabel(text + ":");
        label.setAlignmentX(Component.RIGHT_ALIGNMENT);
        addSetWidthField(label, parent);
    }

    /**
     * Same as addLabel() however, this label will be used to signal to the user that the corresponding field is required.
     *
     * @param text
     * @param parent
     */
    public static void addRequiredLabel(String text, Container parent) {
        addLabel("*" + text, parent);
    }

    /**
     * This will add a field that only takes up 1 column.
     *
     * @param c
     * @param parent
     */
    public static void addSetWidthField(Component c, Container parent) {
        addWithConstraints(c, parent, LABEL_CONSTRAINTS);
    }

    /**
     * This will add a field that takes up one column, the pixel width of the field can be specified.
     *
     * @param c
     * @param parent
     * @param width
     */
    public static void addSetWidthField(Component c, Container parent, int width) {
        setFieldPreferredWidth(c, width);
        addWithConstraints(c, parent, LABEL_CONSTRAINTS);
    }


    /**
     * This adds a last field that will take up the remaining columns in the row.
     *
     * @param c
     * @param parent
     */
    public static void addLastField(Component c, Container parent) {
        addWithConstraints(c, parent, LAST_CONSTRAINTS);
    }


    /**
     * This will add a field to the parent that will take up the specified number of columns.
     *
     * @param c
     * @param parent
     * @param widthScale
     */
    public static void addWidthScaledField(Component c, Container parent, int widthScale) {
        GridBagConstraints constraints = (GridBagConstraints) LAST_CONSTRAINTS.clone();
        constraints.gridwidth = widthScale;
        addWithConstraints(c, parent, constraints);
    }

    /**
     * This adds a blank panel to take up the rest of the columns in the row.
     *
     * @param parent
     */
    public static void addBlankLastField(Container parent) {
        addLastField(new JPanel(), parent);
    }

    /**
     * Convenience method to add the component to the parent using the specified constraints.
     *
     * @param c
     * @param parent
     * @param constraints
     */
    private static void addWithConstraints(Component c, Container parent, GridBagConstraints constraints) {
        GridBagLayout gbl = (GridBagLayout) parent.getLayout();
        gbl.setConstraints(c, constraints);
        parent.add(c);
    }

    /**
     * Sets the preferred width for the field
     *
     * @param c
     * @param width
     */
    private static void setFieldPreferredWidth(Component c, int width) {
        Dimension dimension = c.getPreferredSize();
        dimension.width = width;
        c.setPreferredSize(dimension);
    }

    /**
     * Used to change the fields border to red when it fails validation
     *
     * @param field
     */
    public static void validationFailed(JComponent field) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    /**
     * Changes the border back to its original color
     *
     * @param field
     */
    public static void validationSuccess(JComponent field) {
        field.setBorder(UIManager.getBorder("TextField.border"));
    }

    /**
     * This will add a validator to the specified field that will not allow it to be blank.
     *
     * @param field
     * @param handler
     * @param fieldName
     */
    public static void addRequiredValidator(final JTextComponent field, final InputValidationPassFailHandler handler, final String fieldName) {
        field.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                if (field.getText() == null || field.getText().trim().equals("")) {
                    handler.fail(fieldName + " cannot be blank", field);
                    validationFailed(field);
                } else {
                    validationSuccess(field);
                    handler.pass(field);
                }
                return true;
            }
        });
    }

}
