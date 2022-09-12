package app;

import java.awt.*;
import java.util.*;

// so that each sketch can have mutliple sketchshapes within it.
public class SketchGroup extends ArrayList<SketchShape>{

    Boolean isSelected;

    SketchGroup(){
        isSelected = false;
    }
    SketchGroup(Boolean isSelected){
        this.isSelected = isSelected;
    }
    SketchGroup(SketchShape ss){
        this.add(ss);
        isSelected = false;
    }
    public Boolean checkSelected(Point p, double thr){
        for (int i=0; i<this.size();i++){
            if (this.get(i).checkSelected(p, thr)){
                return true;
            }
        }
        return false;
    }
    public void applyTranslation(int tx, int ty){
        SketchShape tmpSS;
        for (int i=0; i<this.size();i++){
            tmpSS = this.get(i);
            tmpSS.applyTranslation(tx, ty);
            this.set(i, (SketchShape)tmpSS.clone());
        }
    }
    public int[] getBounds(){
        int[] bounds = this.get(0).getBounds(); //minX maxX

        for (int i=1; i<this.size();i++){
            int[] tmpBounds = new int[4];

            if(tmpBounds[0] < bounds[0]){
                bounds[0] = tmpBounds[0];
            }
            if(tmpBounds[2] > bounds[2]){
                bounds[2] = tmpBounds[2];
            }
            if(tmpBounds[1] < bounds[1]){
                bounds[1] = tmpBounds[1];
            }
            if(tmpBounds[3] > bounds[3]){
                bounds[3] = tmpBounds[3];
            }
        }
        return bounds;
    }
}
