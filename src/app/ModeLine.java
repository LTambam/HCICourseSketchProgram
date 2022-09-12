package app;

import java.awt.*;
import java.awt.event.*;

public class ModeLine implements Mode, MenuConstants{
    private String mode = modeLine;
    SketchFrame sf;
    ModeLine(SketchFrame sf){
        this.sf = sf;
    }
    public String getMode(){
        return this.mode;
    }
    public void draw(Graphics2D g2d){
        g2d.drawLine((int)sf.startPoint.getX(), (int)sf.startPoint.getY(),
                                (int)sf.currPoint.getX(), (int)sf.currPoint.getY());
    }
    public void mouseMove(MouseEvent e){
        Point p = e.getPoint();
        if (sf.state == 1){
            sf.currPoint = p;
            if (sf.currPoint != sf.prevPoint){
                sf.prevPoint = sf.currPoint;
            }
        }
    }
    public void mouseClickS0(MouseEvent e){
        sf.state = 1;
        sf.ss = new SketchShape();
        sf.ss.add(sf.clickPoint);
    }
    public void mouseClickS1(MouseEvent e){
        sf.ss.add(sf.currPoint);
        SketchShape ss = sf.ss;
        SketchGroup sg = new SketchGroup(ss);
        sf.sketchAl.add(sg);
        sf.startPoint=sf.currPoint;
        sf.ss = new SketchShape();
        sf.ss.add(sf.startPoint);
        sf.state=1;        
    }
    public void keyType(){
        sf.state=0;
    }
}
