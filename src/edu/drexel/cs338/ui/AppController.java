package edu.drexel.cs338.ui;

import javax.swing.*;
import java.util.Stack;

/**
 * Created by Angel on 8/15/2016.
 */
public class AppController {
    Stack<JPanel> navStack = new Stack<>();
    JPanel current = null;
    JFrame frame;

    public AppController(JFrame frame) {
        this.frame = frame;
        frame.setResizable(false);
    }

    private void display(JPanel panel, boolean pushCurrent) {
        if (current != null) {
            if(pushCurrent) navStack.push(current);
            frame.remove(current);
        }
        current = panel;
        frame.add(panel);
        refresh();
    }

    public void goToTitleScreen() {
        navStack.clear();
        display(new TitleScreen(this), false);
    }

    public void display(JPanel panel) {
        display(panel, true);
    }

    public void goBack() {
        display(navStack.pop(), false);
    }

    public void refresh() {
        frame.revalidate();
        frame.repaint();
        frame.pack();
    }
}
