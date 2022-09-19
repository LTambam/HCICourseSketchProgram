package app;

public class SketchCmd implements MenuConstants{
    String cmd;
    int[] index;
    SketchComponent[] sketch;
    int tx, ty;

    SketchCmd(String cmd, int[] index, SketchComponent[] sketch){
        this.cmd = cmd;
        this.index = index;
        this.sketch = sketch;
    }
    SketchCmd(String cmd, int[] index, SketchComponent[] sketch, int tx, int ty){
        this.cmd = cmd;
        this.index = index;
        this.sketch = sketch;
        this.tx = tx;
        this.ty = ty;
    }
}
