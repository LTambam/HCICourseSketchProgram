package app;

import java.util.*;
import java.awt.*;
import java.io.Serializable;

public abstract class SketchComponent implements Serializable{
    public void add(SketchComponent sketchComponent){
        throw new UnsupportedOperationException();
    }
    public void remove(SketchComponent sketchComponent){
        throw new UnsupportedOperationException();
    }
    public SketchComponent getChild(int i){
        throw new UnsupportedOperationException();
    }
    public Boolean checkSelected(){
        throw new UnsupportedOperationException();
    }
    public Boolean checkHovering(Point P, double thr){
        throw new UnsupportedOperationException();
    }
    public void applyTranslation(int tx, int ty){
        throw new UnsupportedOperationException();
    }
    public int[] getBounds(){
        throw new UnsupportedOperationException();
    }
    public void paintComponent(Graphics2D g2d){
        throw new UnsupportedOperationException();
    }
    public int getSize(){
        throw new UnsupportedOperationException();
    }
    public int getGroupSize(){
        throw new UnsupportedOperationException();
    }
    public void setSelected(Boolean sel){
        throw new UnsupportedOperationException();
    }
    public abstract Iterator<SketchComponent> createIterator();
}
