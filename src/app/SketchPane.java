package app;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class SketchPane extends JPanel implements MenuConstants {

    private Stack<SketchShape> stack;
    SketchFrame sf;

    public ArrayList<SketchComponent> sketchal = new ArrayList<SketchComponent>();

    public SketchPane(SketchFrame sf){
        this.sf = sf;

        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.white);

        addMouseListener(new MouseEventAdapter(sf));
        addMouseMotionListener(new MouseEventAdapter(sf));

        
    }
    public void push(SketchShape ss){
        stack.push(ss);
    }
    public SketchShape pop(){
        return stack.pop();
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }
    @Override
    protected void paintComponent(Graphics g) {
        sf.setCursor(Cursor.getPredefinedCursor(sf.cursor));
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        Iterator<SketchComponent> it = sf.sketchAl.iterator();
        while (it.hasNext()){
            SketchComponent sc = it.next();
            sc.paintComponent(g2d);
        }
        if (sf.state == 1){
            g2d.setColor(sf.color);
            sf.mode.draw(g2d);
        }
        g2d.dispose();
    }
}