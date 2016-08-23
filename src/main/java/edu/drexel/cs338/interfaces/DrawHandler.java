package edu.drexel.cs338.interfaces;

import edu.drexel.cs338.ui.CanvasPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 8/15/2016.
 */
public class DrawHandler implements MouseListener, MouseMotionListener {
    private CanvasPanel panel;
    private Color color = Color.BLACK;
    private Color backgroundColor = Color.WHITE;
    private float size = 5;
    private BasicStroke stroke;
    private BufferedImage image;
    private final Object GRAPHICS_LOCK = new Object();
    private Graphics2D g2d;

    private List<Point> currentLine = new ArrayList<>();
    private List<LineDrawnListener> lineDrawnListeners = new ArrayList<>();

    private boolean drawing;
    private boolean erasing;

    private Point lastPoint;

    public DrawHandler(CanvasPanel panel) {
        this.panel = panel;
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        drawing = false;
    }

    public void createImage() {
        Dimension dimension = panel.getSize();
        int width = (int) Math.round(dimension.getWidth());
        int height = (int) Math.round(dimension.getHeight());
        image = (BufferedImage) panel.createImage(width, height);
        g2d = image.createGraphics();
        clearImage();
        stroke = new BasicStroke(size);
        g2d.setStroke(stroke);
        panel.setImage(image);
    }

    private void clearImage() {
        synchronized (GRAPHICS_LOCK) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
            g2d.setColor(color);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        synchronized (GRAPHICS_LOCK) {
            g2d.setColor(color);
        }
    }

    public float getSize() {
        return size;
    }

    public void setErasing(boolean erasing) {
        synchronized (GRAPHICS_LOCK) {
            this.erasing = erasing;
            if (erasing) {
                g2d.setColor(backgroundColor);
            } else {
                g2d.setColor(color);
            }
        }
    }

    public void setSize(float size) {
        this.size = size;
        stroke = new BasicStroke(size);
        synchronized (GRAPHICS_LOCK) {
            g2d.setStroke(stroke);
        }
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public BufferedImage getImage() {
        return image;
    }

    private void drawTo(int x, int y) {
        drawLine((int) lastPoint.getX(), (int) lastPoint.getY(), x, y);
        lastPoint = new Point(x, y);
    }

    private void drawLine(int x1, int y1, int x2, int y2) {
        synchronized (GRAPHICS_LOCK) {
            g2d.drawLine(x1, y1, x2, y2);
        }
        panel.revalidate();
        panel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        drawing = true;
        lastPoint = new Point(e.getX(), e.getY());
        currentLine.clear();
        currentLine.add(lastPoint);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawTo(e.getX(), e.getY());
        currentLine.add(new Point(e.getX(), e.getY()));
        drawing = false;
        notifyListeners();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        drawTo(e.getX(), e.getY());
        currentLine.add(new Point(e.getX(), e.getY()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public void addOnLineDrawnListner(LineDrawnListener lineDrawnListener) {
        lineDrawnListeners.add(lineDrawnListener);
    }

    public void removeOnLineDrawnListener(LineDrawnListener lineDrawnListener) {
        lineDrawnListeners.remove(lineDrawnListener);
    }

    private void notifyListeners() {
        for (LineDrawnListener listener : lineDrawnListeners) {
            listener.onLineDrawn(image);
        }
    }


    public void setImage(BufferedImage image) {
        this.image = image;
        panel.setImage(image);
        synchronized (GRAPHICS_LOCK) {
            g2d = image.createGraphics();
            g2d.setStroke(stroke);
            if(erasing) {
                g2d.setColor(backgroundColor);
            } else {
                g2d.setColor(color);
            }
        }
        if(drawing) {
            Point previous = currentLine.get(0);
            synchronized (GRAPHICS_LOCK) {
                for (Point point : new ArrayList<>(currentLine)) {
                    g2d.drawLine(
                            (int) previous.getX(),
                            (int) previous.getY(),
                            (int) point.getX(),
                            (int) point.getY());
                }
            }
        }
        panel.revalidate();
        panel.repaint();
    }

}
