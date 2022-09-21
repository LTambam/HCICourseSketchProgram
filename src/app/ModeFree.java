package app;

import java.awt.*;
import java.awt.event.*;

public class ModeFree implements Mode, MenuConstants {
    private String mode = modeFree;
    SketchFrame sf;
    ModeFree(SketchFrame sf){
        this.sf = sf;
    }
    public String getMode(){
        return this.mode;
    }
    public void draw(Graphics2D g2d){
        // draw the whole scribble
        for (int i=1; i<sf.ss.size();i++){
            g2d.drawLine((int)sf.ss.get(i-1).getX(), (int)sf.ss.get(i-1).getY(),
                        (int)sf.ss.get(i).getX(), (int)sf.ss.get(i).getY());
        }
    }
    public void mouseMove(MouseEvent e){
        Point p = e.getPoint();
        // add a point to the SketchShape
        if (sf.state == 1){
            sf.currPoint = p;

            if (sf.currPoint != sf.prevPoint){
                sf.ss.add(sf.currPoint);
                sf.prevPoint = sf.currPoint;
            }
        }
    }
    public void mouseClickS0(MouseEvent e){
        // create a new object and add the first point
        sf.state = 1;
        sf.ss = new SketchShape();
        sf.ss.add(sf.clickPoint);
    }
    public void mouseClickS1(MouseEvent e){
        SketchShape ss = sf.ss;
        ss.setColor(sf.color);
        SketchComponent sl = new SketchLeaf(ss);
        sf.sketchAl.add(sl);
        
        SketchCmd cmd = new SketchCmd(modeFree);
        cmd.addComponent(sf.sketchAl.size()-1, sl);
        sf.pushCmd(cmd);
    }
    public void keyType(){
        sf.state=0;
    }
}
