package edu.drexel.cs338;

import edu.drexel.cs338.constants.UIConstants;
import edu.drexel.cs338.data.FirebaseController;
import edu.drexel.cs338.ui.AppController;
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
        final JFrame frame = new JFrame(UIConstants.TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 720));

        AppController controller = new AppController(frame);
        controller.display(new TitleScreen(controller));

        //Display the window.
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
