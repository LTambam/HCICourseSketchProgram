package app;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.event.*;

public class ModeCirc implements Mode, MenuConstants {
    private String mode = modeCirc;
    SketchFrame sf;
    ModeCirc(SketchFrame sf){
        this.sf = sf;
    }
    public String getMode(){
        return this.mode;
    }
    public void draw(Graphics2D g2d){
        if (sf.elps.width > 0 && sf.elps.height > 0){
            g2d.draw(sf.elps);
        }
    }
    public void mouseMove(MouseEvent e){
        Point p = e.getPoint();
        if (sf.state == 1){
            sf.currPoint = p;
            int tempX = sf.currPoint.x; int tempY = sf.currPoint.y;
            int tempXlen = sf.startPoint.x-tempX; int tempYlen = sf.startPoint.y-tempY;
            int tempL = Math.min(Math.abs(tempXlen), Math.abs(tempYlen));
            
            if (tempXlen>0){ sf.elps.x = sf.startPoint.x-tempL; }
            else{ sf.elps.x = sf.startPoint.x; }
            if (tempYlen>0){ sf.elps.y = sf.startPoint.y-tempL; }
            else{ sf.elps.y = sf.startPoint.y; }
            
            sf.elps.width = tempL;
            sf.elps.height = tempL;
        }
    }
    public void mouseClickS0(MouseEvent e){
        sf.state = 1;
        sf.elps = new Ellipse2D.Double();
    }
    public void mouseClickS1(MouseEvent e){
        SketchShape ss = ArrayConverter.getArrayFromEllipse(sf.elps);
        ss.color = sf.color;
        SketchGroup sg = new SketchGroup(ss);
        sf.sketchAl.add(sg);
    }
    public void keyType(){

    }
}
