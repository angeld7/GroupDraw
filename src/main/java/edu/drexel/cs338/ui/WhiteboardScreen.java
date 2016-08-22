package edu.drexel.cs338.ui;

import edu.drexel.cs338.data.Whiteboard;
import edu.drexel.cs338.interfaces.DrawHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Angel on 8/15/2016.
 */
public class WhiteboardScreen extends JPanel {
    private Whiteboard whiteboard;
    private AppController controller;
    private DrawHandler drawHandler;

    public WhiteboardScreen(AppController controller, Whiteboard whiteboard) {
        super(new BorderLayout());
        this.controller = controller;
        this.whiteboard = whiteboard;
        initComponents();
    }

    private void initComponents() {
        CanvasPanel drawingPanel = new CanvasPanel();
        drawHandler = new DrawHandler(drawingPanel);
        drawingPanel.setBackground(drawHandler.getBackgroundColor());

        JLabel title = new JLabel(whiteboard.getName());
        title.setAlignmentX(CENTER_ALIGNMENT);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel lowerTopPanel = new JPanel();
        lowerTopPanel.setLayout(new BoxLayout(lowerTopPanel, BoxLayout.X_AXIS));

        Dimension topInputsDimension = new Dimension(30, 16);

        JLabel colorLabel = new JLabel("Color: ");
        JButton colorButton = new JButton();
        colorButton.setPreferredSize(topInputsDimension);
        colorButton.setMaximumSize(colorButton.getPreferredSize());
        colorButton.setBackground(drawHandler.getColor());
        colorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(null, "Choose a color", drawHandler.getColor());
            colorButton.setBackground(color);
            drawHandler.setColor(color);
            controller.refresh();
        });

        JLabel sizeLabel = new JLabel("Size: ");
        JSlider sizeSlider = new JSlider(1, 200);
        sizeSlider.setValue((int) drawHandler.getSize());
        topInputsDimension = new Dimension(200, (int) topInputsDimension.getHeight());
        sizeSlider.setPreferredSize(topInputsDimension);
        sizeSlider.setMaximumSize(sizeSlider.getPreferredSize());
        sizeSlider.addChangeListener(e -> drawHandler.setSize(sizeSlider.getValue()));

        lowerTopPanel.add(colorLabel);
        lowerTopPanel.add(colorButton);
        lowerTopPanel.add(sizeLabel);
        lowerTopPanel.add(sizeSlider);
        lowerTopPanel.add(Box.createHorizontalGlue());

        topPanel.add(title);
        topPanel.add(lowerTopPanel);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        Dimension buttonDimension = new Dimension(50, 50);

        ImageIcon markerIcon = new ImageIcon("img/marker.png");
        Image markerImg = markerIcon.getImage();
        JButton markerButton = new JButton(new ImageIcon(markerImg.getScaledInstance(
                (int) buttonDimension.getWidth(),
                (int) buttonDimension.getHeight(),
                Image.SCALE_SMOOTH)));
        markerButton.setPreferredSize(buttonDimension);
        markerButton.setMaximumSize(buttonDimension);
        markerButton.addActionListener(e -> drawHandler.setErasing(false));

        ImageIcon eraserIcon = new ImageIcon("img/eraser.png");
        Image eraserImg = eraserIcon.getImage();
        JButton eraserButton = new JButton(new ImageIcon(eraserImg.getScaledInstance(
                (int) buttonDimension.getWidth(),
                (int) buttonDimension.getHeight(),
                Image.SCALE_SMOOTH)));
        eraserButton.setPreferredSize(buttonDimension);
        eraserButton.setMaximumSize(buttonDimension);
        eraserButton.addActionListener(e -> drawHandler.setErasing(true));

        sidePanel.add(markerButton);
        sidePanel.add(eraserButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(null, "Are you sure you would like to leave this whiteboard?", "Please Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                controller.goToTitleScreen();
            }
        });
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "PNG Images", "png");
            fileChooser.setFileFilter(filter);
            fileChooser.setSelectedFile(new File(whiteboard.getName() + ".png"));
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String name = selectedFile.getName();
                if(name.length() <= 3 || !name.substring(name.length() - 4, name.length()).toLowerCase().equals(".png")) {
                    selectedFile = new File(selectedFile.getAbsoluteFile() + ".png");
                }
                try {
                    ImageIO.write(drawHandler.getImage(), "png", selectedFile);
                    JOptionPane.showMessageDialog(null, "The whiteboard image has been saved.");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occurred while saving the image.");
                }
            }
            controller.refresh();
        });

        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(saveButton);
        bottomPanel.add(exitButton);

        add(topPanel, BorderLayout.NORTH);
        add(sidePanel, BorderLayout.WEST);
        add(drawingPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void createImage() {
        drawHandler.createImage();
    }

    public DrawHandler getDrawHandler() {
        return drawHandler;
    }
}
