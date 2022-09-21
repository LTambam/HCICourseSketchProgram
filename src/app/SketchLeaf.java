package app;

import java.util.Iterator;
import java.awt.*;

public class SketchLeaf extends SketchComponent{
    
    SketchShape sketchShape;
    Boolean isSelected;

    public SketchLeaf(){
        isSelected = false;
    }
    public SketchLeaf(Boolean isSelected){
        this.isSelected = isSelected;
    }
    public SketchLeaf(SketchShape sketchShape){
        this.sketchShape = sketchShape;
        isSelected = false;
    }
    @Override
    public Boolean checkSelected(){
        return isSelected;
    }
    @Override
    public Boolean checkHovering(Point p, double thr){
        return sketchShape.checkHovering(p, thr);
    }
    @Override
    public void applyTranslation(int tx, int ty){
        sketchShape.applyTranslation(tx, ty);
    }
    @Override
    public int[] getBounds(){
        return sketchShape.getBounds();
    }
    @Override
    public int getSize(){
        return 1;
    }
    @Override
    public int getGroupSize(){
        return 1;
    }
    @Override 
    public void paintComponent(Graphics2D g2d){
        g2d.setColor(sketchShape.getColor());
        for (int i=1; i<sketchShape.size();i++){
            g2d.drawLine(sketchShape.get(i-1).x, sketchShape.get(i-1).y,
                        sketchShape.get(i).x, sketchShape.get(i).y);
        }
    }
    @Override 
    public void setSelected(Boolean sel){
        sketchShape.setSelected(sel);
        this.isSelected = sel;
    }
    @Override
    public Iterator<SketchComponent> createIterator(){
        return new NullIterator();
    }
    @Override
    public Iterator<SketchComponent> createShallowIterator(){
        return new NullIterator();
    }
    
}