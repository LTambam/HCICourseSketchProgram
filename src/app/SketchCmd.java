package app;

public class SketchCmd{
    String cmd;
    int index;
    SketchShape sketch;

    SketchCmd(String cmd, int index, SketchShape sketch){
        this.cmd = cmd;
        this.index = index;
        this.sketch = sketch;
    }
}
