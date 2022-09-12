package app;

// import java.awt.*;
import java.awt.event.*;

public class KeyEventAdapter extends KeyAdapter implements MenuConstants{
    SketchFrame sf;
    
    public KeyEventAdapter(SketchFrame sf) {
        this.sf = sf;
    }
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 0x1B){ //escape key
            sf.mode.keyType();
            sf.getContentPane().repaint();
        }
    }
}
