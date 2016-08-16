package edu.drexel.cs338.ui;

import edu.drexel.cs338.constants.UIConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Angel on 7/28/2016.
 */
public class TitleScreen extends JPanel {
    AppController controller;

    public TitleScreen(AppController controller) {
        super();
        this.controller = controller;
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        initComponents();
    }

    private void initComponents() {
        JLabel title = new JLabel(UIConstants.TITLE);
        title.setFont(new Font("Sans-Serif", Font.BOLD, 75));
            title.setBorder(BorderFactory.createEmptyBorder(50,50,100,50));
        Dimension buttonDimensions = new Dimension(10000,50);
        JButton create = new JButton("Create Whiteboard");
        create.addActionListener(e -> {
            WhiteboardScreen whiteboard = new WhiteboardScreen(controller);
            controller.display(whiteboard);
            whiteboard.createImage();
        });

        JButton join = new JButton("Join Whiteboard");
        join.setEnabled(false);

        create.setPreferredSize(buttonDimensions);
        join.setPreferredSize(buttonDimensions);
        title.setAlignmentX(CENTER_ALIGNMENT);
        create.setAlignmentX(CENTER_ALIGNMENT);
        join.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(title);
        add(create);
        add(join);
        add(Box.createVerticalGlue());
    }
}
