package app;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.event.*;

public class ModeElps implements Mode, MenuConstants {
    private String mode = modeElps;
    SketchFrame sf;
    ModeElps(SketchFrame sf){
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
            int minX = Math.min(sf.currPoint.x, sf.startPoint.x);
            int minY = Math.min(sf.currPoint.y, sf.startPoint.y);
            int maxX = Math.max(sf.currPoint.x, sf.startPoint.x);
            int maxY = Math.max(sf.currPoint.y, sf.startPoint.y);

            sf.elps.x = minX;
            sf.elps.y = minY;
            sf.elps.width = maxX - minX;
            sf.elps.height = maxY - minY; 
        }
    }
    public void mouseClickS0(MouseEvent e){
        sf.state = 1;
        sf.elps = new Ellipse2D.Double();
    }
    public void mouseClickS1(MouseEvent e){
        SketchShape ss = ArrayConverter.getArrayFromEllipse(sf.elps);
        ss.setColor(sf.color);
        SketchComponent sl = new SketchLeaf(ss);
        sf.sketchAl.add(sl);
        SketchCmd cmd = new SketchCmd(modeElps);
        cmd.addComponent(sf.sketchAl.size()-1, sl);
        sf.pushCmd(cmd);
    }
    public void keyType(){
        sf.state=0;
    }
}
