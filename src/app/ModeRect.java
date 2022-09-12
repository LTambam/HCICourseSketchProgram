package app;

import java.awt.*;
import java.awt.event.*;

public class ModeRect implements Mode, MenuConstants{
    private String mode = modeRect;
    SketchFrame sf;
    ModeRect(SketchFrame sf){
        this.sf = sf;
    }
    public String getMode(){
        return this.mode;
    }
    public void draw(Graphics2D g2d){
        if (sf.rect.width > 0 && sf.rect.height > 0){
            g2d.draw(sf.rect);
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
            sf.rect.x = minX;
            sf.rect.y = minY;
            sf.rect.width = maxX - minX;
            sf.rect.height = maxY - minY;
        }
    }
    public void mouseClickS0(MouseEvent e){
        sf.state = 1;
        sf.rect = new Rectangle();
    }
    public void mouseClickS1(MouseEvent e){
        SketchShape ss = ArrayConverter.getArrayFromRect(sf.rect);
        ss.color = sf.color;
        SketchGroup sg = new SketchGroup(ss);
        sf.sketchAl.add(sg);
    }
    public void keyType(){

    }
}
