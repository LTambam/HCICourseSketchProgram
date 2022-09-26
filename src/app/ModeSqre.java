package app;

import java.awt.*;
import java.awt.event.*;

public class ModeSqre implements Mode, MenuConstants {
    private String mode = modeSqre;
    SketchFrame sf;
    ModeSqre(SketchFrame sf){
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
            int tempX = sf.currPoint.x; int tempY = sf.currPoint.y;
            int tempXlen = sf.startPoint.x-tempX; int tempYlen = sf.startPoint.y-tempY;
            int tempL = Math.min(Math.abs(tempXlen), Math.abs(tempYlen));

            if (tempXlen>0){ sf.rect.x = sf.startPoint.x-tempL; }
            else{ sf.rect.x = sf.startPoint.x; }
            if (tempYlen>0){ sf.rect.y = sf.startPoint.y-tempL; }
            else{ sf.rect.y = sf.startPoint.y; }

            sf.rect.width = tempL;
            sf.rect.height = tempL;
        }
    }
    public void mouseClickS0(MouseEvent e){
        sf.state = 1;
        sf.rect = new Rectangle();
    }
    public void mouseClickS1(MouseEvent e){
        SketchShape ss = ArrayConverter.getArrayFromRect(sf.rect);
        ss.setColor(sf.color);
        SketchComponent sl = new SketchLeaf(ss);
        sf.sketchAl.add(sl);

        SketchCmd cmd = new SketchCmd(modeSqre);
        cmd.addComponent(sf.sketchAl.size()-1, sl);
        sf.pushCmd(cmd);
    }
    public void mousePress(MouseEvent e){
        
    }
    public void mouseDrag(MouseEvent e){

    }
    public void mouseRelease(MouseEvent e){

    }
    public void keyType(){
        sf.state=0;
    }
}
