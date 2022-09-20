package app;

import java.awt.*;
import java.util.*;


// so that each sketch can have mutliple sketchshapes within it.
public class SketchNode extends SketchComponent{
    transient Iterator<SketchComponent> iterator = null;
    ArrayList<SketchComponent> sketchComponents = new ArrayList<SketchComponent>();
    Boolean isSelected;

    public SketchNode(){
        isSelected = false;
    }
    public SketchNode(Boolean isSelected){
        this.isSelected = isSelected;
    }
    public SketchNode(SketchComponent sketchComponent){
        sketchComponents.add(sketchComponent);
        isSelected = false;
    }
    public void add(SketchComponent sketchComponent){
        sketchComponents.add(sketchComponent);
    }
    public void remove(SketchComponent sketchComponent){
        sketchComponents.remove(sketchComponent);
    }
    @Override
    public Boolean checkSelected(){
        return this.isSelected;
    }
    @Override
    public Boolean checkHovering(Point p, double thr){
        for (SketchComponent sketch:sketchComponents){
            if (sketch.checkHovering(p, thr)){
                return true;
            }
        }
        return false;
    }
    @Override
    public void applyTranslation(int tx, int ty){
        // sketchComponents.forEach(sketch -> applyTranslation(tx, ty)); Why does this lambda function fail, but manually iterating through succeeds?

        Iterator<SketchComponent> iterator = sketchComponents.iterator();
        while(iterator.hasNext()){
            SketchComponent sketchComponent = iterator.next();
            sketchComponent.applyTranslation(tx, ty);
        }
    }
    @Override
    public int[] getBounds(){
        int[] bounds = sketchComponents.get(0).getBounds(); //minX maxX

        for (int i=1; i<sketchComponents.size(); i++){
            int[] tmpBounds = sketchComponents.get(i).getBounds();

            if(tmpBounds[0] < bounds[0]){
                bounds[0] = tmpBounds[0];
            }if(tmpBounds[2] > bounds[2]){
                bounds[2] = tmpBounds[2];
            }if(tmpBounds[1] < bounds[1]){
                bounds[1] = tmpBounds[1];
            }if(tmpBounds[3] > bounds[3]){
                bounds[3] = tmpBounds[3];
            }
        }
        return bounds;
    }
    @Override
    public SketchComponent getChild(int i){
        return sketchComponents.get(i);
    }
    @Override
    public int getSize(){
        Iterator<SketchComponent> iterator = sketchComponents.iterator();
        int size = 0;
        while(iterator.hasNext()){
            SketchComponent sketchComponent = iterator.next();
            size += sketchComponent.getSize();
        }
        return size;
    }
    @Override
    public int getGroupSize(){
        return sketchComponents.size();
    }
    @Override 
    public void paintComponent(Graphics2D g2d){
        Iterator<SketchComponent> iterator = sketchComponents.iterator();
        while(iterator.hasNext()){
            SketchComponent sketchComponent = iterator.next();
            sketchComponent.paintComponent(g2d);
        }
    }
    @Override 
    public void setSelected(Boolean sel){
        Iterator<SketchComponent> iterator = sketchComponents.iterator();
        while(iterator.hasNext()){
            SketchComponent sketchComponent = iterator.next();
            sketchComponent.setSelected(sel);
        }
        this.isSelected = sel;
    }
    @Override
    public Iterator<SketchComponent> createIterator(){
        if (iterator ==null){
            iterator = new CompositeIterator(sketchComponents.iterator());
        }
        return iterator;
    }
    @Override
    public Iterator<SketchComponent> createShallowIterator(){
        if (iterator ==null){
            iterator = sketchComponents.iterator();
        }
        return iterator;
    }
}
