package edu.drexel.cs338.ui;

import edu.drexel.cs338.data.FirebaseController;
import edu.drexel.cs338.data.UserTableModel;
import edu.drexel.cs338.data.Whiteboard;
import edu.drexel.cs338.interfaces.DrawHandler;
import edu.drexel.cs338.interfaces.PassFailHandler;
import edu.drexel.cs338.ui.components.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Angel on 8/15/2016.
 */
public class WhiteboardScreen extends JPanel {
    private Whiteboard whiteboard;
    private AppController controller;
    private DrawHandler drawHandler;
    private JLabel colorLabel;
    private JButton colorButton;
    private JLabel sizeLabel;
    private JSlider sizeSlider;
    private JButton markerButton;
    private JButton eraserButton;
    private JLabel title;
    private JButton exitButton;
    private JButton saveButton;
    private CanvasPanel drawingPanel;
    private JTable userTable;
    private String userName;

    private WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            exit(ev -> System.exit(0));
        }
    };

    public WhiteboardScreen(AppController controller, Whiteboard whiteboard, String username) {
        super(new BorderLayout());
        this.userName = username;
        this.controller = controller;
        this.whiteboard = whiteboard;
        initComponents();
        addComponents();
        FirebaseController.get().joinWhiteboard(username, whiteboard, drawHandler);
        controller.addWindowListener(windowListener);
    }

    private void initComponents() {
        drawingPanel = new CanvasPanel();

        drawHandler = new DrawHandler(drawingPanel);
        drawingPanel.setBackground(drawHandler.getBackgroundColor());

        title = new JLabel(whiteboard.getName());
        title.setAlignmentX(CENTER_ALIGNMENT);

        Dimension topInputsDimension = new Dimension(30, 16);

        colorLabel = new JLabel("Color: ");
        colorButton = new JButton();
        colorButton.setPreferredSize(topInputsDimension);
        colorButton.setMaximumSize(colorButton.getPreferredSize());
        colorButton.setBackground(drawHandler.getColor());
        colorButton.setForeground(drawHandler.getColor());
        colorButton.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(10)));
        colorButton.setBorderPainted(true);
        colorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Choose a color", drawHandler.getColor());
            colorButton.setBackground(color);
            colorButton.setForeground(color);
            drawHandler.setColor(color);

            controller.refresh();
        });

        sizeLabel = new JLabel("Size: ");
        sizeSlider = new JSlider(1, 200);
        sizeSlider.setValue((int) drawHandler.getSize());
        topInputsDimension = new Dimension(200, (int) topInputsDimension.getHeight());
        sizeSlider.setPreferredSize(topInputsDimension);
        sizeSlider.setMaximumSize(sizeSlider.getPreferredSize());
        sizeSlider.addChangeListener(e -> drawHandler.setSize(sizeSlider.getValue()));

        Dimension buttonDimension = new Dimension(50, 50);

        ImageIcon markerIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/marker.png"));
        Image markerImg = markerIcon.getImage();
        markerButton = new JButton(new ImageIcon(markerImg.getScaledInstance(
                (int) buttonDimension.getWidth(),
                (int) buttonDimension.getHeight(),
                Image.SCALE_SMOOTH)));
        markerButton.setPreferredSize(buttonDimension);
        markerButton.setMaximumSize(buttonDimension);
        markerButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        markerButton.addActionListener(e -> {
            markerButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            eraserButton.setBorder(BorderFactory.createEmptyBorder());
            drawHandler.setErasing(false);
        });

        ImageIcon eraserIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/eraser.png"));
        Image eraserImg = eraserIcon.getImage();
        eraserButton = new JButton(new ImageIcon(eraserImg.getScaledInstance(
                (int) buttonDimension.getWidth(),
                (int) buttonDimension.getHeight(),
                Image.SCALE_SMOOTH)));
        eraserButton.setPreferredSize(buttonDimension);
        eraserButton.setMaximumSize(buttonDimension);
        eraserButton.addActionListener(e -> {
            eraserButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            markerButton.setBorder(BorderFactory.createEmptyBorder());
            drawHandler.setErasing(true);
        });

        exitButton = new JButton("Exit");
        exitButton.addActionListener(e ->
            exit(ev -> {
                controller.goToTitleScreen();
                controller.removeWindowListener(windowListener);
            })
        );
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "PNG Images", "png");
            fileChooser.setFileFilter(filter);
            fileChooser.setSelectedFile(new File(whiteboard.getName() + ".png"));
            int returnValue = fileChooser.showSaveDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String name = selectedFile.getName();
                if (name.length() <= 3 || !name.substring(name.length() - 4, name.length()).toLowerCase().equals(".png")) {
                    selectedFile = new File(selectedFile.getAbsoluteFile() + ".png");
                }
                try {
                    ImageIO.write(drawHandler.getImage(), "png", selectedFile);
                    JOptionPane.showMessageDialog(this, "The whiteboard image has been saved.");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this, "An error occurred while saving the image.");
                }
            }
            controller.refresh();
        });

        userTable = new UserTable();
        UserTableModel model = new UserTableModel(whiteboard.getName(), userTable);
        userTable.setModel(model);
    }

    private void exit(ActionListener afterExit) {
        boolean exit = JOptionPane.showConfirmDialog(this, "The room will be DELETED if you are the last to leave. Unsaved work will be LOST.\n\nAre you sure you would like to leave this whiteboard?", "Please Confirm", JOptionPane.YES_NO_OPTION) == 0;
        if (exit) {
            FirebaseController.get().exitWhiteboard(userName, whiteboard.getName(), afterExit);
        }
    }

    private void addComponents() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel lowerTopPanel = new JPanel();
        lowerTopPanel.setLayout(new BoxLayout(lowerTopPanel, BoxLayout.X_AXIS));

        lowerTopPanel.add(colorLabel);
        lowerTopPanel.add(colorButton);
        lowerTopPanel.add(sizeLabel);
        lowerTopPanel.add(sizeSlider);
        lowerTopPanel.add(Box.createHorizontalGlue());

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        sidePanel.add(markerButton);
        sidePanel.add(eraserButton);

        topPanel.add(title);
        topPanel.add(lowerTopPanel);

        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(saveButton);
        bottomPanel.add(exitButton);

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(120, 800));

        add(topPanel, BorderLayout.NORTH);
        add(sidePanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.EAST);
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
