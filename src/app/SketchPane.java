package app;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class SketchPane extends JPanel implements MenuConstants {

    private Stack<SketchShape> stack;
    SketchFrame sf;

    public ArrayList<SketchGroup> sketchal = new ArrayList<SketchGroup>();

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
        
        Iterator<SketchGroup> it = sf.sketchAl.iterator();

        while (it.hasNext()){
            Iterator<SketchShape> it2 = it.next().iterator();
            while(it2.hasNext()){
                SketchShape tmpSS = it2.next();
                g2d.setColor(tmpSS.color);
                for (int i=1; i<tmpSS.size();i++){
                    g2d.drawLine((int)tmpSS.get(i-1).getX(), (int)tmpSS.get(i-1).getY(),
                            (int)tmpSS.get(i).getX(), (int)tmpSS.get(i).getY());
                }
            }
        }
        if (sf.state == 1){
            g2d.setColor(sf.color);
            sf.mode.draw(g2d);
        }
        g2d.dispose();
    }
}