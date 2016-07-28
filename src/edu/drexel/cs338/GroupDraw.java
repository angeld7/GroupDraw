package edu.drexel.cs338;

import edu.drexel.cs338.ui.TitleScreen;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Angel on 7/28/2016.
 */
public class GroupDraw {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new GroupDraw().createAndShowGUI());
    }

    /**
     * This creates the basic frame of teh application and attaches the main screen to it
     */
    private void createAndShowGUI() {
        final JFrame frame = new JFrame("Group Draw");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));

        //Attach the main screen
        frame.add(new TitleScreen());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
