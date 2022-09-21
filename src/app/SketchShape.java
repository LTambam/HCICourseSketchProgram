package app;

import java.awt.*;
import java.util.*;

public class SketchShape extends ArrayList<Point>{
    private Color color;
    private transient Boolean isSelected;

    public SketchShape(){
        color = Color.black;
        isSelected = false;
    }
    public SketchShape(Color color){
        this.color = color;
        isSelected = false; 
    }
    public SketchShape(Color color, Boolean isSelected){
        this.color = color;
        this.isSelected = isSelected; 
    }
    public void setSelected(Boolean isSelected){
        this.isSelected = isSelected;
    }
    public Boolean getSelected(){
        return isSelected;
    }
    public void setColor(Color color){
        this.color = color;
    }
    public Color getColor(){
        return color;
    }
    public Boolean checkHovering(Point p, double thr){
        double x, y;
        for (int i=0; i<this.size();i++){
            x = this.get(i).x;
            y = this.get(i).y;
            if ((Math.abs(p.x-x) < thr) && (Math.abs(p.y-y) < thr)){
                return true;
            }
        }
        return false;
    }
    public void applyTranslation(int tx, int ty){
        Point tmpP;
        for (int i=0; i<this.size();i++){
            tmpP = this.get(i);
            tmpP.translate(tx, ty);
            this.set(i, (Point)tmpP.clone());
        }
    }
    public int[] getBounds(){
        int[] bounds = new int[4];
        int x, y;

        bounds[0] = bounds[2] = this.get(0).x; //minX maxX
        bounds[1] = bounds[3] = this.get(0).y; //minY, maxY

        for (int i=1; i<this.size();i++){
            x = this.get(i).x;
            y = this.get(i).y;
            if(x < bounds[0]){
                bounds[0] = x;
            }else if(x > bounds[2]){
                bounds[2] = x;
            }
            if(y < bounds[1]){
                bounds[1] = y;
            }else if(y > bounds[3]){
                bounds[3] = y;
            }
        }
        return bounds;
    }
}