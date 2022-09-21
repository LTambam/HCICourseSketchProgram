package app;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MouseEventAdapter extends MouseAdapter implements MenuConstants{
    SketchFrame sf;
    
    public MouseEventAdapter(SketchFrame sf) {
        this.sf = sf;
    }
    public void mouseMoved(MouseEvent e) {
        // call the move function from the current mode
        sf.mode.mouseMove(e);
        sf.getContentPane().repaint();
    }
    public void mouseClicked(MouseEvent e) {
        // call the mouse clicked function for state 0 and 1 for the current mode
        sf.clickPoint = new Point(e.getPoint());

        if (sf.state == 0) {
            Iterator<SketchComponent> it = sf.sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sg = it.next();
                sg.setSelected(false);
            }
            sf.currPoint = sf.prevPoint = sf.startPoint = sf.clickPoint;
            sf.mode.mouseClickS0(e);
        } else {
            sf.state=0;
            //save the previous object to the stack and create new object
            sf.mode.mouseClickS1(e);
            // sf.undoCmdStack.clear();
        }
        sf.getContentPane().repaint();
    }

}