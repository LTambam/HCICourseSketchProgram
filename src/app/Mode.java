package app;

import java.awt.*;
import java.awt.event.*;

interface Mode {
    String getMode();
    void draw(Graphics2D g2d);
    void mouseMove(MouseEvent e);
    void mouseClickS0(MouseEvent e);
    void mouseClickS1(MouseEvent e);
    void mousePress(MouseEvent e);
    void mouseDrag(MouseEvent e);
    void mouseRelease(MouseEvent e);
    void keyType();
}
