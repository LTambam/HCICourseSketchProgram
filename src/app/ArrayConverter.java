package app;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;

public class ArrayConverter {
    public static SketchShape getArrayFromRect(Rectangle rect){
        Point tmp = new Point();
        SketchShape tmpArr = new SketchShape();
        tmp.setLocation(rect.x, rect.y);
        tmpArr.add((Point)tmp.clone());
        for(int i=0; i<rect.width; i+=5){
            tmp.setLocation(rect.x+i, rect.y);
            tmpArr.add((Point)tmp.clone());
        }
        tmp.setLocation(rect.x+rect.width, rect.y);
        tmpArr.add((Point)tmp.clone());
        for(int i=0; i<rect.height; i+=5){
            tmp.setLocation(rect.x+rect.width, rect.y+i);
            tmpArr.add((Point)tmp.clone());
        }
        tmp.setLocation(rect.x+rect.width, rect.y+rect.height);
        tmpArr.add((Point)tmp.clone());
        for(int i=0; i<rect.width; i+=5){
            tmp.setLocation(rect.x+rect.width-i, rect.y+rect.height);
            tmpArr.add((Point)tmp.clone());
        }
        tmp.setLocation(rect.x, rect.y+rect.height);
        tmpArr.add((Point)tmp.clone());
        for(int i=0; i<rect.height; i+=5){
            tmp.setLocation(rect.x, rect.y+rect.height-i);
            tmpArr.add((Point)tmp.clone());
        }
        tmp.setLocation(rect.x, rect.y);
        tmpArr.add((Point)tmp.clone());
        return tmpArr;
    }

    public static SketchShape getArrayFromEllipse(Ellipse2D.Double elps){
        SketchShape tempALP = new SketchShape();
        final double flatness = 0.1;
        PathIterator pi = elps.getPathIterator(null, flatness);
        double[] coords = new double[6];
        Point tmpPnt = new Point();
        
        while (!pi.isDone()){
            int s = pi.currentSegment(coords);
            switch (s){
                case PathIterator.SEG_MOVETO:
                    // Ignore
                    break;
                case PathIterator.SEG_LINETO:
                    tmpPnt.setLocation(coords[0], coords[1]); 
                    tempALP.add((Point)tmpPnt.clone());
                    break;
                case PathIterator.SEG_CLOSE:
                    // Ignore
                    break;
                case PathIterator.SEG_QUADTO:
                    throw new AssertionError(
                        "SEG_QUADTO in flattening path iterator");
                case PathIterator.SEG_CUBICTO:
                    throw new AssertionError(
                        "SEG_CUBICTO in flattening path iterator");
            }
            pi.next();
        }
        tmpPnt.setLocation(tempALP.get(0)); 
        tempALP.add((Point)tmpPnt.clone());
        return tempALP;        
    }
}
