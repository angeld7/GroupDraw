package edu.drexel.cs338.ui.components;

import edu.drexel.cs338.ui.AppController;

import javax.swing.*;

/**
 * Created by Angel on 8/21/2016.
 */
public class CancelButton extends JButton {

    public CancelButton(AppController controller) {
        super("Cancel");
        addActionListener(e -> controller.goBack());
    }
}
