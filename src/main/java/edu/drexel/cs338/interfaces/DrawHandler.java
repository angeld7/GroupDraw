package edu.drexel.cs338.interfaces;

import edu.drexel.cs338.ui.CanvasPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 * Created by Angel on 8/15/2016.
 */
public class DrawHandler implements MouseListener, MouseMotionListener {
    CanvasPanel panel;
    Color color = Color.BLACK;
    Color backgroundColor = Color.WHITE;
    float size = 5;
    BasicStroke stroke;
    BufferedImage image;
    Graphics2D g2d;

    Point lastPoint;

    public DrawHandler(CanvasPanel panel) {
        this.panel = panel;
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
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

    public void clearImage(){
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.setColor(color);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        g2d.setColor(color);
    }

    public float getSize() {
        return size;
    }

    public void setErasing(boolean erasing) {
        if (erasing) {
            g2d.setColor(backgroundColor);
        } else {
            g2d.setColor(color);
        }
    }

    public void setSize(float size) {
        this.size = size;
        stroke = new BasicStroke(size);
        g2d.setStroke(stroke);
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
        drawLine((int) Math.round(lastPoint.getX()), (int) Math.round(lastPoint.getY()), x, y);
        lastPoint = new Point(x, y);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        g2d.drawLine(x1, y1, x2, y2);
        panel.revalidate();
        panel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastPoint = new Point(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawTo(e.getX(), e.getY());
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
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
