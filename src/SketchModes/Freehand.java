package SketchModes;

import java.awt.*;
import java.awt.event.*;

public class Freehand{
    private Frame mainFrame;
    private Label modeLabel;
    private GridBagConstraints c = new GridBagConstraints();
    private Button[] modeButtons = new Button[8];
    private String[] modeButtonText = {"sel", "free", "line", "rect", "sqre", "elps", "circ", "ply"};
    private Canvas sketchCanvas = new Canvas();
    private int x0, y0, x, y;

    public Freehand(){prepGUI();}
    
    private void prepGUI(){
        mainFrame = new Frame("Sketch by LT");
        mainFrame.setSize(400, 400);
        mainFrame.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent windowEvent){System.exit(0);}});
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        for (int i = 0; i < 8; i++) {
            c.gridx = i;
            c.gridy = 0;
            modeButtons[i] = new Button(modeButtonText[i]); // call button constructor with names
            // modeButtons[i].addActionListener(this); // add the ability to track the button events
            mainFrame.add(modeButtons[i], c);
        }
        
        c.ipady = 150;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 8;
        c.gridx = 0;
        c.gridy = 1;
        mainFrame.add(sketchCanvas, c);
        
        modeLabel = new Label();
        modeLabel.setText("Select Mode");
        modeLabel.setAlignment(Label.CENTER);
        
    }
    private void showLineSketch(){

        // sketchCanvas.addMouseMotionListener(new MouseMotionAdapter(){
        //     public void mouseMoved(MouseEvent e) {
                
        //     } 
        // });
        sketchCanvas.setBackground(Color.black);

        
 
        mainFrame.setVisible(true);  
    }
    // public class myMouseHandler extends MouseAdapter {
    //     public void mousePressed(MouseEvent e){ x0=e.getX(); y0=e.getY(); }
    //     public void mouseReleased(MouseEvent e) { }
    // }
    // public class myMouseMotionHandler extends MouseMotionAdapter {
    //     public void mouseMoved(MouseEvent e) {  }
    //     public void mouseDragged(MouseEvent e){ x=e.getX(); y=e.getY(); repaint(); }
    // }

    // public void paint(Graphics g) {  g.drawLine(x0,y0,x,y); }
    
    public static void main(String[] args) {
        Freehand freehand = new Freehand();
        freehand.showLineSketch();
    }
}
