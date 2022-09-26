package app;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ModeSelect implements Mode, MenuConstants{
    private String mode = modeSelect;
    SketchFrame sf;
    final static float dash1[] = {10.0f};
    final static BasicStroke dashed =
        new BasicStroke(1.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1, 0.0f);
    
    public boolean moveCmd = false;

    ModeSelect(SketchFrame sf){
        this.sf = sf;
    }
    public String getMode(){
        return this.mode;
    }
    public void draw(Graphics2D g2d){
        g2d.setStroke(new BasicStroke());
        g2d.setStroke(dashed);
        g2d.setColor(Color.GREEN);

        Iterator<SketchComponent> it = sf.sketchAl.iterator();
        // int i = 0;
        while (it.hasNext()){
            // System.out.println("loop "+i);
            SketchComponent sn = it.next();
            if(sn.checkSelected()){
                Rectangle tmpRect = new Rectangle();
                int[] bounds = sn.getBounds();
                tmpRect.setFrameFromDiagonal(bounds[0], bounds[1], bounds[2], bounds[3]);
                g2d.draw(tmpRect);
            }
            // i = i+1;
        }
    }
    public void mouseMove(MouseEvent e){
        Point p = e.getPoint();
        if (sf.moveCmd){
            if(sf.state==1){
            // move the object with the mouse movement
                sf.currPoint = p;
                if (sf.currPoint != sf.prevPoint){
                    int tx = (sf.currPoint.x-sf.prevPoint.x);
                    int ty = (sf.currPoint.y-sf.prevPoint.y);

                    Iterator<SketchComponent> it = sf.sketchAl.iterator();
                    while (it.hasNext()){
                        SketchComponent sg = it.next();
                        if(sg.checkSelected()){
                            sg.applyTranslation(tx, ty);
                            
                        }
                    }
                    sf.prevPoint = sf.currPoint;
                }
            }
        }else{
            // change the cursor to show that it is on an object boundary
            Boolean selFlag = false;
            sf.movePoint = p;
            Iterator<SketchComponent> it = sf.sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sg = it.next();
                if(sg.checkHovering(sf.movePoint, SketchFrame.sel_thr)){
                    selFlag = true;
                }
            }
            if (selFlag){
                sf.cursor = Cursor.HAND_CURSOR;
            }else{
                sf.cursor = Cursor.DEFAULT_CURSOR;
            }
        }
    }
    public void mouseClickS0(MouseEvent e){
        sf.state = 0;
        Iterator<SketchComponent> it = sf.sketchAl.iterator();
        while (it.hasNext()){
            SketchComponent sg = it.next();
            if(sg.checkHovering(sf.clickPoint, SketchFrame.sel_thr)){
                sg.setSelected(true);
                sf.state = 1;
            }else{
                sg.setSelected(false);
            }
        }
    }
    public void mouseClickS1(MouseEvent e){
        // need to add functionality

        if(e.isControlDown() && !sf.moveCmd){
            sf.state = 1;
            Iterator<SketchComponent> it = sf.sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sg = it.next();
                if(!sg.checkSelected()){
                    if(sg.checkHovering(sf.clickPoint, SketchFrame.sel_thr)){
                        sg.setSelected(true);
                    }else{
                        sg.setSelected(false);
                    }
                }
            }
        }else if(sf.moveCmd){
            sf.state = 1;
            sf.moveCmd=false;

            int tx = sf.currPoint.x-sf.startPoint.x;
            int ty = sf.currPoint.y-sf.startPoint.y;
            SketchCmd cmd = new SketchCmd(editMove, tx, ty);

            int i = 0;
            Iterator<SketchComponent> it = sf.sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sg = it.next();
                if(sg.checkSelected()){
                    cmd.addComponent(i, sf.copySketchComponent(sg));
                }
                i++;
            }
            sf.pushCmd(cmd);
        }else{
            sf.state = 0;
            Iterator<SketchComponent> it = sf.sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sg = it.next();
                if(sg.checkHovering(sf.clickPoint, SketchFrame.sel_thr)){
                    sg.setSelected(true);
                    sf.state=1;
                }else{
                    sg.setSelected(false);
                }
            }
            sf.moveCmd=false;
        }
    }
    public void mousePress(MouseEvent e){
        if(!e.isControlDown()){
            Iterator<SketchComponent> it = sf.sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sg = it.next();
                if(sg.checkHovering(sf.startPoint, SketchFrame.sel_thr)){
                    sf.pressedOnSketch = true;
                    sg.setSelected(true);
                    sf.state = 1;
                }else{
                    sg.setSelected(false);
                }
            }
        }
    }
    public void mouseDrag(MouseEvent e){
        sf.currPoint = e.getPoint();
        if (sf.currPoint != sf.prevPoint){
            int tx = (sf.currPoint.x-sf.prevPoint.x);
            int ty = (sf.currPoint.y-sf.prevPoint.y);

            Iterator<SketchComponent> it = sf.sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sg = it.next();
                if(sg.checkSelected()){
                    sg.applyTranslation(tx, ty);
                }
            }
            sf.prevPoint = sf.currPoint;
        }
    }
    public void mouseRelease(MouseEvent e){
        sf.state = 1;
        sf.moveCmd=false;

        int tx = sf.currPoint.x-sf.startPoint.x;
        int ty = sf.currPoint.y-sf.startPoint.y;
        SketchCmd cmd = new SketchCmd(editMove, tx, ty);

        int i = 0;
        Iterator<SketchComponent> it = sf.sketchAl.iterator();
        while (it.hasNext()){
            SketchComponent sg = it.next();
            if(sg.checkSelected()){
                cmd.addComponent(i, sf.copySketchComponent(sg));
            }
            i++;
        }
        sf.pushCmd(cmd);

        sf.pressedOnSketch = false;
    }
    public void keyType(){
        System.out.println("esc pressed while in select");
        if(sf.state==1){
            System.out.println("esc pressed while in select state 1");
            if(sf.moveCmd){
                int tx = sf.startPoint.x-sf.currPoint.x;
                int ty = sf.startPoint.y-sf.currPoint.y;

                Iterator<SketchComponent> it = sf.sketchAl.iterator();
                while (it.hasNext()){
                    SketchComponent sg = it.next();
                    if(sg.checkSelected()){
                        sg.applyTranslation(tx, ty);
                    }
                }
                sf.moveCmd=false;
            }else{
                Iterator<SketchComponent> it = sf.sketchAl.iterator();
                while (it.hasNext()){
                    SketchComponent sg = it.next();
                    sg.setSelected(false);
                }
                sf.state = 0;
            }
            
        }
    }
}
