package app;

import java.util.*;

public class SketchCmd implements MenuConstants{
    String cmd;
    private ArrayList<Integer> indices;
    private ArrayList<SketchComponent> sketches;
    int tx, ty;

    SketchCmd(String cmd){
        this.cmd = cmd;
        this.indices = new ArrayList<Integer>();
        this.sketches = new ArrayList<SketchComponent>();
    }
    SketchCmd(String cmd, int tx, int ty){
        this.cmd = cmd;
        this.tx = tx;
        this.ty = ty;
        this.indices = new ArrayList<Integer>();
        this.sketches = new ArrayList<SketchComponent>();
    }
    SketchCmd(String cmd, ArrayList<Integer> indices, ArrayList<SketchComponent> sketches){
        this.cmd = cmd;
        this.indices = indices;
        this.sketches = sketches;
    }
    public void addComponent(int idx, SketchComponent sketch){
        indices.add(idx);
        sketches.add(sketch);
    }
    public int size(){
        return sketches.size();
    }
    public SketchComponent getComponent(int i){
        return sketches.get(i);
    }
    public int getComponentIdx(int i){
        return indices.get(i);
    }
}
