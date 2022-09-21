// package PaintUtils;

// import java.awt.*;
// import java.awt.geom.Ellipse2D.Double;
// import java.io.Serializable;
// import java.awt.geom.PathIterator;
// import java.awt.geom.FlatteningPathIterator;
// // import java.awt.geom.Circle;
// // import java.io.

// import java.awt.event.*;
// import java.util.*;

// import javax.swing.SwingUtilities;
// import javax.swing.JFrame;
// import javax.swing.JMenu;
// import javax.swing.JMenuBar;
// import javax.swing.JMenuItem;
// import javax.swing.JRadioButtonMenuItem;
// import javax.swing.JButton;
// import javax.swing.ButtonGroup;
// import javax.swing.KeyStroke;
// import javax.swing.ImageIcon;
// import javax.swing.JPanel;
// import javax.swing.colorchooser.*;
// import javax.swing.*;
// import javax.swing.event.MouseInputAdapter;
// import javax.swing.event.MouseInputListener;

// // import org.w3c.dom.events.MouseEvent;

// import javax.swing.BorderFactory;

// public class SketchFrame implements ActionListener, MenuConstants, KeyListener, Serializable {
//     JFrame f;
//     JPanel p;
//     int state = 0;
//     int x = 0, y = 0, xp = 0, yp = 0;

//     private String fileName = "Untitled";
//     // private boolean saved = true;
//     String applicationName = "SketchFrame";

//     // FileOperation fileHandler;
//     JColorChooser colorChooser = null;
//     Color color = Color.black;
//     JDialog colorDialog = null;
//     JMenuItem cutItem, copyItem, deleteItem, findItem, findNextItem,
//             replaceItem, gotoItem, selectAllItem;

//     String mode = modeFree;
//     Stack stack = new Stack();
//     Stack tmpStack = new Stack();
//     Stack undoStack = new Stack();
//     Stack<Boolean> selStack = new Stack<Boolean>();
//     Stack<Boolean> tmpSelStack = new Stack<Boolean>();
//     // Stack<String> cmdStack = new Stack<String>();
//     // Stack<String> tmpCmdStack = new Stack<String>();
//     // Stack<String> undoCmdStack = new Stack<String>();
//     Stack<Color> colorStack = new Stack<Color>();
//     Stack<Color> tmpColorStack = new Stack<Color>();
//     Stack<Color> undoColorStack = new Stack<Color>();

//     /****************************/
//     SketchFrame() {
//         f = new JFrame(fileName + " - " + applicationName);
        
//         createMenuBar(f);
        
//         f.pack();

//         p = new SketchPane();
//         f.setContentPane(p);
        
//         // f.setExtendedState(JFrame.MAXIMIZED_BOTH);
//         f.setSize(800, 500);
               
//         // f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

//         // create the filehandling object to deal with saving images from the panel
//         // fileHandler = new FileOperation(this);

//         // Check to automatically save file can I even adapt this for my JPanel?
//         // Document listener normally, doesn't work for ours.
//         // Check if new commands?

//         /////////
//         WindowListener frameClose = new WindowAdapter() {
//             public void windowClosing(WindowEvent we) {
//                 // if (fileHandler.confirmSave())
//                 System.exit(0);
//             }
//         };
//         f.addWindowListener(frameClose);
//         f.addKeyListener(this);
//         f.setVisible(true);
//     }
//     public ArrayList<Point> getArrayFromEllipse(Double elps){
//         ArrayList<Point> tempALP = new ArrayList<Point>();

//         final double flatness = 0.1;
//         PathIterator pi = elps.getPathIterator(null, flatness);
//         double[] coords = new double[6];
        
//         double sumX = 0;
//         double sumY = 0;
//         int numPoints = 0;
//         Point tmpPnt = new Point();
        
//         while (!pi.isDone())
//         {
//             int s = pi.currentSegment(coords);
//             switch (s)
//             {
//                 case PathIterator.SEG_MOVETO:
//                     // Ignore
//                     break;
//                 case PathIterator.SEG_LINETO:
//                     tmpPnt.setLocation(coords[0], coords[1]); 
//                     tempALP.add(tmpPnt);
//                     break;
//                 case PathIterator.SEG_CLOSE:
//                     // Ignore
//                     break;
//                 case PathIterator.SEG_QUADTO:
//                     throw new AssertionError(
//                         "SEG_QUADTO in flattening path iterator");
//                 case PathIterator.SEG_CUBICTO:
//                     throw new AssertionError(
//                         "SEG_CUBICTO in flattening path iterator");
//             }
//             pi.next();
//         }
//         return tempALP;        
//     }
//     public class SketchShape extends ArrayList<Point>{
//         public Color color;
//         public Boolean selected;
//         SketchShape(){
//             color = Color.black;
//             selected = false;
//         }
//         public Boolean checkSelected(Point p, double thr){
//             double x, y;
//             for (int i=1; i<this.size();i++){
//                 x = this.get(i).x;
//                 y = this.get(i).y;

//                 if (Math.abs(p.x-x) < thr && Math.abs(p.y-y) < thr)
//                     return true;
//             }
//             return false;
//         }
//     }
//     public class SketchPane extends JPanel {

//         private Rectangle rectObj; 
//         private Double elpsObj;
//         private Point clickPoint, prevPoint, currPoint, startPoint;
//         private ArrayList<Point> ssay;
//         private PathIterator fpi;
//         private Point[] pA;
//         private Color tmpColor;
//         private int sel = 0;
//         private Boolean itemSelected = false;

//         public SketchPane() {
//             setBorder(BorderFactory.createLineBorder(Color.black));
//             setBackground(Color.white);
//             // setSize(500, 300);
//             MouseAdapter ma = new MouseAdapter() {
//                 @Override
//                 public void mouseClicked(MouseEvent e) {
//                     clickPoint = new Point(e.getPoint());
//                     System.out.println(clickPoint.getX());
//                     System.out.println(clickPoint.getY());
//                     if (state == 0) {
//                         state = 1;
//                         currPoint = clickPoint;
//                         prevPoint = clickPoint;
//                         startPoint = clickPoint;
                        
//                         if (mode==modeFree){
//                             //create an array to save the points
//                             ssay = new ArrayList<Point>();
//                             ssay.add(clickPoint);
//                         }
//                         else if (mode==modeLine){
//                             ssay = new ArrayList<Point>();
//                             ssay.add(clickPoint);
//                         }
//                         else if (mode==modeElps ||mode==modeCirc )
//                             elpsObj = new Double();
//                         else if (mode==modeRect || mode==modeSqre)
//                             rectObj = new Rectangle();
//                         else if (mode==modeSelect){
//                             // selStack.clear();
//                             // while(!stack.empty()){
//                             //     // tmpColor = colorStack.pop();
//                             //     if(stack.peek().getClass()==ssay.getClass()){
//                             //         ArrayList<Point> tempPA = (ArrayList<Point>)stack.pop();
//                             //         for (int i=1; i<tempPA.size();i++){
//                             //             if(itemSelected(clickPoint, tempPA.get(i), 0.1)){
//                             //                 sel = 1; 
//                             //                 break;
//                             //             }
//                             //         }
//                             //         tmpStack.push(tempPA);
//                             //     }
//                             //     else if (stack.peek().getClass()==rectObj.getClass()){ 
//                             //         Rectangle tempRect = (Rectangle)stack.pop();
//                             //         if(itemSelected(clickPoint, tempRect, 0.1)){
//                             //             sel = 1; 
//                             //         }
//                             //         tmpStack.push(tempRect);
//                             //     }
//                             //     else if(stack.peek().getClass()==elpsObj.getClass()){
//                             //         Double tempElps = (Double)stack.pop();
//                             //         if(itemSelected(clickPoint, tempElps, 0.1)){
//                             //             sel = 1; 
//                             //         }
//                             //         tmpStack.push(tempElps);
//                             //     }
//                             //     selStack.push(sel);
//                             //     sel = 0;
//                             // }
//                             // while(!tmpStack.empty()){
//                             //     stack.push(tmpStack.pop());
//                             // }
//                         }
//                     // save vals
//                     } else {
//                         //save the previous object to the stack and create new object
//                         state=0;
//                         colorStack.push(color);
//                         if (mode==modeFree)
//                             stack.push(ssay);
//                         else if(mode==modeLine){
//                             ssay.add(currPoint);
//                             stack.push(ssay);
//                             startPoint=currPoint;
//                             ssay = new ArrayList<Point>();
//                             ssay.add(startPoint);
//                             state=1;
//                         }
//                         else if (mode==modeElps || mode==modeCirc )
//                             stack.push(elpsObj);

//                         else if (mode==modeRect || mode==modeSqre)
//                             stack.push(rectObj);
//                         // else if (mode==modeSelect){
//                         //     //check if any of the objects in the stack are fully surrounded by the drawn rectangle
                            
//                         // }
                        
//                         undoStack.clear();
//                         undoColorStack.clear();

//                         repaint();
//                     }
//                     System.out.println(state);
//                 }

//                 @Override
//                 public void mouseMoved(MouseEvent e) {
//                     if (state == 1) {
//                         currPoint = e.getPoint();
//                         if (mode == modeFree) {
//                             if (currPoint != prevPoint){
//                                 ssay.add(currPoint);
//                                 prevPoint = currPoint;
//                             }
//                         } else if (mode == modeLine) {
//                             if (currPoint != prevPoint){
//                                 //Change line to 
//                                 prevPoint = currPoint;
//                             }
//                         } else if (mode == modeRect) {

//                             int minX = Math.min(e.getX(), startPoint.x);
//                             int minY = Math.min(e.getY(), startPoint.y);
//                             int maxX = Math.max(e.getX(), startPoint.x);
//                             int maxY = Math.max(e.getY(), startPoint.y);

//                             rectObj.x = minX;
//                             rectObj.y = minY;
//                             rectObj.width = maxX - minX;
//                             rectObj.height = maxY - minY;
    
//                         } else if (mode == modeSqre) {
//                             int tempX = e.getX(); int tempY = e.getY();
//                             int tempXlen = startPoint.x-tempX; int tempYlen = startPoint.y-tempY;
//                             int tempL = Math.min(Math.abs(tempXlen), Math.abs(tempYlen));

//                             if (tempXlen>0)
//                                 rectObj.x = startPoint.x-tempL;
//                             else
//                                 rectObj.x = startPoint.x;
//                             if (tempYlen>0)
//                                 rectObj.y = startPoint.y-tempL;
//                             else
//                                 rectObj.y = startPoint.y;
                            
//                             rectObj.width = tempL;
//                             rectObj.height = tempL;

//                         } else if (mode == modeElps) {
//                             int minX = Math.min(e.getX(), startPoint.x);
//                             int minY = Math.min(e.getY(), startPoint.y);
//                             int maxX = Math.max(e.getX(), startPoint.x);
//                             int maxY = Math.max(e.getY(), startPoint.y);

//                             elpsObj.x = minX;
//                             elpsObj.y = minY;
//                             elpsObj.width = maxX - minX;
//                             elpsObj.height = maxY - minY;    
//                         } else if (mode == modeCirc) {
//                             int tempX = e.getX(); int tempY = e.getY();
//                             int tempXlen = startPoint.x-tempX; int tempYlen = startPoint.y-tempY;
//                             int tempL = Math.min(Math.abs(tempXlen), Math.abs(tempYlen));
                            
//                             if (tempXlen>0)
//                                 elpsObj.x = startPoint.x-tempL;
//                             else
//                                 elpsObj.x = startPoint.x;
//                             if (tempYlen>0)
//                                 elpsObj.y = startPoint.y-tempL;
//                             else
//                                 elpsObj.y = startPoint.y;
                            
//                             elpsObj.width = tempL;
//                             elpsObj.height = tempL;
//                         }
//                     }
//                     repaint();
//                 } 
//             };
//             addMouseListener(ma);
//             addMouseMotionListener(ma);
//         }

//         public Boolean itemSelected(Point cp, Point p, double thr){
//             if (Math.abs(cp.x-p.x) < thr && Math.abs(cp.y-p.y) < thr)
//                 return true;
//             else 
//                 return false;
//         }
//         public Boolean itemSelected(Point cp, Rectangle r, double thr){
//             if (cp.x>r.x-thr && cp.x < r.x+r.width+thr){
//                 if((cp.y>r.y-thr&&cp.y<r.y+thr)||(cp.y<r.y+r.height+thr&&cp.y>r.y+r.height-thr))
//                     return true;
//             }
//             if (cp.y<r.y+r.height+thr && cp.y>r.y-thr){
//                 if((cp.x>r.x-thr&&cp.x<r.x+thr)||(cp.x<r.x+r.width+thr&&cp.x>r.x+r.width-thr))
//                     return true;
//             }
//             return false;
//         }
//         public Boolean itemSelected(Point cp, Double e, double thr){
//             double p = (Math.pow((cp.x-e.getCenterX()), 2)/Math.pow(e.height,2))
//                         +(Math.pow((cp.y-e.getCenterY()),2)/Math.pow(e.width,2));

//             if (Math.abs(p)-1< thr)
//                 return true;
//             else 
//                 return false;
//         }

//         @Override
//         public Dimension getPreferredSize() {
//             return new Dimension(200, 200);
//         }

//         @Override
//         protected void paintComponent(Graphics g) {
//             super.paintComponent(g);
//             Graphics2D g2d = (Graphics2D) g.create();
            
//             while(!stack.empty()){
//                 tmpColor = colorStack.pop();
//                 g2d.setColor(tmpColor);
//                 if(stack.peek().getClass()==ssay.getClass()){

//                     ArrayList<Point> tempPA = (ArrayList<Point>)stack.pop();
                    
//                     for (int i=1; i<tempPA.size();i++){
//                         g2d.drawLine((int)tempPA.get(i-1).getX(), (int)tempPA.get(i-1).getY(),
//                                         (int)tempPA.get(i).getX(), (int)tempPA.get(i).getY());
//                     }

//                     tmpStack.push(tempPA);
                    
//                 }
//                 else if (stack.peek().getClass()==rectObj.getClass()){
                    
//                     Rectangle tempRect = (Rectangle)stack.pop();

//                     g2d.draw(tempRect);

//                     tmpStack.push(tempRect);
                    
//                 }
//                 else if(stack.peek().getClass()==elpsObj.getClass()){
//                     Double tempElps = (Double)stack.pop();
                    
//                     g2d.draw(tempElps);
//                     tmpStack.push(tempElps);
//                 }
//                 tmpColorStack.push(tmpColor);
//             }
//             while(!tmpStack.empty()){
//                 stack.push(tmpStack.pop());
//                 colorStack.push(tmpColorStack.pop());
//             }
//             if (state==1){
//                 g2d.setColor(color);
//                 if (mode==modeFree){
//                     for (int i=1; i<ssay.size();i++){
//                         g2d.drawLine((int)ssay.get(i-1).getX(), (int)ssay.get(i-1).getY(),
//                                         (int)ssay.get(i).getX(), (int)ssay.get(i).getY());
//                     }
//                 }
//                 else if (mode==modeLine){
//                     g2d.drawLine((int)startPoint.getX(), (int)startPoint.getY(),
//                                     (int)currPoint.getX(), (int)currPoint.getY());
//                 }
//                 else if (mode==modeRect){
//                     if (rectObj.width > 0 && rectObj.height > 0)
//                         g2d.draw(rectObj);
//                 }
//                 else if (mode==modeSqre){
//                     if (rectObj.width > 0 && rectObj.height > 0)
//                         g2d.draw(rectObj);
//                 }
//                 else if (mode==modeElps){
//                     if (elpsObj.width > 0 && elpsObj.height > 0)
//                         g2d.draw(elpsObj);
//                 }
//                 else if (mode==modeCirc){
//                     if (elpsObj.width > 0 && elpsObj.height > 0)
//                         g2d.draw(elpsObj);
//                 }
//             }
//             g2d.disfose();
//         }
//     }

//     /** Handle the key typed event from the text field. */
//     public void keyTyped(KeyEvent e) {
//         if (e.getKeyChar() == 0x1B)
//             state = 0;
//             f.getContentPane().repaint();
//     }

//     /** Handle the key-pressed event from the text field. */
//     public void keyPressed(KeyEvent e) {
//         if (e.getKeyChar() == 0x1B)
//             state = 0;
//     }

//     /** Handle the key-released event from the text field. */
//     public void keyReleased(KeyEvent e) {
//         if (e.getKeyChar() == 0x1B)
//             state = 0;
//     }

//     public void actionPerformed(ActionEvent e) {
//         String cmdText = e.getActionCommand();
//         // ////////////////////////////////////
//         // if (cmdText.equals(fileNew))
//         // fileHandler.newFile();
//         // else if (cmdText.equals(fileOpen))
//         // fileHandler.openFile();
//         // ////////////////////////////////////
//         // else if (cmdText.equals(fileSave))
//         // fileHandler.saveThisFile();
//         // ////////////////////////////////////
//         // else if (cmdText.equals(fileSaveAs))
//         // fileHandler.saveAsFile();
//         // ////////////////////////////////////
//         // else if (cmdText.equals(fileExit)) {
//         // if (fileHandler.confirmSave())
//         // System.exit(0);
//         // }
//         // ////////////////////////////////////
//         // else if (cmdText.equals(editCut))
//         // p.cut();
//         // ////////////////////////////////////
//         // else if (cmdText.equals(editCopy))
//         // p.copy();
//         // ////////////////////////////////////
//         // else if (cmdText.equals(editPaste))
//         // p.paste();
//         // ////////////////////////////////////
//         // else if (cmdText.equals(editDelete))
//         // p.replaceSelection("");
//         // ////////////////////////////////////
//         // else if (cmdText.equals(editSelectAll))
//         // ta.selectAll();
//         ////////////////////////////////////
//         if (cmdText.equals(formatColor)) {
//             showColorDialog();
//             state = 0;
//         }else if (cmdText.equals(editUndo)){
//             //code that pops objects off the stack and puts them on another temp stack until a new object is created
//             if (!stack.isEmpty()){
//                 undoStack.push(stack.pop());
//                 undoColorStack.push(colorStack.pop());
//                 f.getContentPane().repaint();
//             }

//             state = 0;
//         }else if (cmdText.equals(editRedo)){
//             //code that pops objects off the new temp stack and puts them on the stack if there is any left
//             if (!undoStack.isEmpty()){
//                 stack.push(undoStack.pop());
//                 colorStack.push(undoColorStack.pop());
//                 f.getContentPane().repaint();
//             }
//             state = 0;
//         }else if (cmdText.equals(modeFree)) {
//             mode = modeFree;
//             state = 0;
//         } else if (cmdText.equals(modeLine)) {
//             mode = modeLine;
//             state = 0;
//         } else if (cmdText.equals(modeRect)) {
//             mode = modeRect;
//             state = 0;
//         } else if (cmdText.equals(modeSqre)) {
//             mode = modeSqre;
//             state = 0;
//         } else if (cmdText.equals(modeElps)) {
//             mode = modeElps;
//             state = 0;
//         } else if (cmdText.equals(modeCirc)) {
//             mode = modeCirc;
//             state = 0;
//         }else if (cmdText.equals(modeSelect)){
//             mode = modeSelect;
//             state = 0;
//         }

//         ////////////////////////////////////
//         System.out.println(mode);
//     }

//     void showColorDialog() {
//         if (colorChooser == null)
//             colorChooser = new JColorChooser();
//         if (colorDialog == null)
//             colorDialog = JColorChooser.createDialog(SketchFrame.this.f,
//                     formatColor,
//                     false,
//                     colorChooser,
//                     new ActionListener() {
//                         public void actionPerformed(ActionEvent evvv) {
//                             SketchFrame.this.color = colorChooser.getColor();
//                         }
//                     },
//                     null);

//         colorDialog.setVisible(true);
//     }

//     ///////////////////////////////////
//     JMenuItem createMenuItem(String s, int key, JMenu toMenu, ActionListener al) {
//         JMenuItem temp = new JMenuItem(s, key);
//         temp.addActionListener(al);
//         toMenu.add(temp);

//         return temp;
//     }

//     ////////////////////////////////////
//     JMenuItem createMenuItem(String s, int key, JMenu toMenu, int aclKey, ActionListener al) {
//         JMenuItem temp = new JMenuItem(s, key);
//         temp.addActionListener(al);
//         temp.setAccelerator(KeyStroke.getKeyStroke(aclKey, ActionEvent.CTRL_MASK));
//         toMenu.add(temp);

//         return temp;
//     }

//     ////////////////////////////////////
//     JMenu createMenu(String s, int key, JMenuBar toMenuBar) {
//         JMenu temp = new JMenu(s);
//         temp.setMnemonic(key);
//         toMenuBar.add(temp);
//         return temp;
//     }

//     JRadioButtonMenuItem createRadioMenuItem(String s, JMenuBar toMenuBar, ButtonGroup toGroup, ActionListener al) {
//         JRadioButtonMenuItem temp = new JRadioButtonMenuItem(s);
//         // temp.setMnemonic(key);
//         temp.setActionCommand(s);
//         temp.addActionListener(al);
//         toGroup.add(temp);
//         toMenuBar.add(temp);

//         return temp;
//     }

//     /*********************************/
//     void createMenuBar(JFrame f) {
//         JMenuBar mb = new JMenuBar();
//         JMenuItem temp;

//         JMenu fileMenu = createMenu(fileText, KeyEvent.VK_F, mb);
//         JMenu editMenu = createMenu(editText, KeyEvent.VK_E, mb);
//         JMenu formatMenu = createMenu(formatText, KeyEvent.VK_O, mb);
        
//         createMenuItem(fileNew, KeyEvent.VK_N, fileMenu, KeyEvent.VK_N, this);
//         createMenuItem(fileOpen, KeyEvent.VK_O, fileMenu, KeyEvent.VK_O, this);
//         createMenuItem(fileSave, KeyEvent.VK_S, fileMenu, KeyEvent.VK_S, this);
//         createMenuItem(fileSaveAs, KeyEvent.VK_A, fileMenu, this);
//         fileMenu.addSeparator();
//         createMenuItem(fileExit, KeyEvent.VK_X, fileMenu, this);

//         createMenuItem(editUndo, KeyEvent.VK_U, editMenu, KeyEvent.VK_Z, this);
//         createMenuItem(editRedo, KeyEvent.VK_R, editMenu, KeyEvent.VK_Y, this);
//         editMenu.addSeparator();
//         cutItem = createMenuItem(editCut, KeyEvent.VK_T, editMenu, KeyEvent.VK_X, this);
//         copyItem = createMenuItem(editCopy, KeyEvent.VK_C, editMenu, KeyEvent.VK_C, this);
//         createMenuItem(editPaste, KeyEvent.VK_P, editMenu, KeyEvent.VK_V, this);
//         deleteItem = createMenuItem(editDelete, KeyEvent.VK_L, editMenu, this);
//         deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
//         editMenu.addSeparator();
//         selectAllItem = createMenuItem(editSelectAll, KeyEvent.VK_A, editMenu, KeyEvent.VK_K, this);

//         createMenuItem(formatColor, KeyEvent.VK_Q, formatMenu, this);

//         ButtonGroup modeGroup = new ButtonGroup();
//         temp = createRadioMenuItem(modeFree, mb, modeGroup, this);
//         temp.setSelected(true);
//         createRadioMenuItem(modeLine, mb, modeGroup, this);
//         createRadioMenuItem(modeRect, mb, modeGroup, this);
//         createRadioMenuItem(modeSqre, mb, modeGroup, this);
//         createRadioMenuItem(modeElps, mb, modeGroup, this);
//         createRadioMenuItem(modeCirc, mb, modeGroup, this);
//         createRadioMenuItem(modeSelect, mb, modeGroup, this);

//         // implement this in future for enabling and disabling menu buttons
//         // MenuListener editMenuListener = new MenuListener() {
//         // public void menuSelected(MenuEvent evvvv) {}
//         // public void menuDeselected(MenuEvent evvvv) {}
//         // public void menuCanceled(MenuEvent evvvv) {}};
//         // editMenu.addMenuListener(editMenuListener);
//         f.setJMenuBar(mb);
//     }
//     public static void main(String[] args) {
//         new SketchFrame();
//     }
// }

// // public
// interface MenuConstants {
//     final String fileText = "File";
//     final String editText = "Edit";
//     final String formatText = "Format";

//     final String fileNew = "New";
//     final String fileOpen = "Open...";
//     final String fileSave = "Save";
//     final String fileSaveAs = "Save As...";
//     final String fileExit = "Exit";

//     final String editUndo = "Undo";
//     final String editRedo = "Redo";
//     final String editCut = "Cut";
//     final String editCopy = "Copy";
//     final String editPaste = "Paste";
//     final String editDelete = "Delete";
//     final String editSelectAll = "Select All";

//     final String formatColor = "Set Sketch color...";

//     final String modeFree = "Free";
//     final String modeLine = "Line";
//     final String modeRect = "Rectangle";
//     final String modeSqre = "Square";
//     final String modeElps = "Ellipse";
//     final String modeCirc = "Circle";
//     final String modeSelect = "Select";

//     final String aboutText = "Your Javapad";
// }


// // ssay = new ArrayList<Point>();
// // // PathIterator pi = elpsObj.getPathIterator(null);
// // fpi = new FlatteningPathIterator(elpsObj.getPathIterator(null), 1);
// // float[] tempArr = new float[6];
// // Point tmpP = new Point();
// // int i=0;
// // while(!fpi.isDone()){
// //     int tempCS = fpi.currentSegment(tempArr);

// //     tmpP.setLocation(tempArr[0], tempArr[1]);
// //     ssay.add(new Point((int)tempArr[0],(int)tempArr[1]));
// //     fpi.next();
// // }
// // stack.push(ssay);
// // private static void createAndShowGUI() {
// // System.out.println("Created GUI on EDT? "+
// // SwingUtilities.isEventDisfatchThread());

// // JFrame frame = new JFrame("Sketch App");
// // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// // // f.setIconImage(new ImageIcon(imgURL).getImage()); when you need to set the
// // icon for the

// // SketchCanvas sketchcanvas = new SketchCanvas();
// // frame.setJMenuBar(sketchcanvas.createMenuBar());
// // frame.setContentPane(sketchcanvas.createContentPane());
// // frame.getContentPane().addMouseListener(new MouseAdapter() {
// // public void mousePressed(MouseEvent e){
// // System.out.println(e.getX());
// // System.out.println(e.getY());
// // }
// // });
// // frame.getContentPane().addMouseMotionListener(new MouseAdapter() {
// // public void mouseMoved(MouseEvent e){
// // Graphics g=frame.getContentPane().getGraphics();
// // g.setColor(Color.RED);
// // g.fillOval(e.getX(),e.getY(),20,20);
// // }
// // });
// // frame.setSize(500,300);
// // frame.setVisible(true);
// // }
// // public JMenuBar createMenuBar() {
// // JMenuBar menubar;
// // JRadioButtonMenuItem rbFree, rbLine, rbRect, rbSqre, rbElps, rbCirc; //
// // remove icons of dots?

// // menubar = new JMenuBar();
// // menubar.setBorder(BorderFactory.createLineBorder(Color.black));

// // ButtonGroup group = new ButtonGroup();
// // rbFree = new JRadioButtonMenuItem(freeStr);
// // rbFree.setSelected(true);
// // rbFree.setMnemonic(KeyEvent.VK_R);
// // rbFree.setActionCommand(freeStr);

// // rbLine = new JRadioButtonMenuItem(lineStr);
// // rbLine.setMnemonic(KeyEvent.VK_R);
// // rbLine.setActionCommand(lineStr);

// // rbRect = new JRadioButtonMenuItem(rectStr);
// // rbRect.setMnemonic(KeyEvent.VK_R);
// // rbRect.setActionCommand(rectStr);

// // rbSqre = new JRadioButtonMenuItem(sqreStr);
// // rbSqre.setMnemonic(KeyEvent.VK_R);
// // rbSqre.setActionCommand(sqreStr);

// // rbElps = new JRadioButtonMenuItem(elpsStr);
// // rbElps.setMnemonic(KeyEvent.VK_R);
// // rbElps.setActionCommand(elpsStr);

// // rbCirc = new JRadioButtonMenuItem(circStr);
// // rbCirc.setMnemonic(KeyEvent.VK_R);
// // rbCirc.setActionCommand(circStr);

// // group.add(rbFree);
// // group.add(rbLine);
// // group.add(rbRect);
// // group.add(rbSqre);
// // group.add(rbElps);
// // group.add(rbCirc);

// // menubar.add(rbFree);
// // menubar.add(rbLine);
// // menubar.add(rbRect);
// // menubar.add(rbSqre);
// // menubar.add(rbElps);
// // menubar.add(rbCirc);

// // rbFree.addActionListener(this);
// // rbLine.addActionListener(this);
// // rbRect.addActionListener(this);
// // rbSqre.addActionListener(this);
// // rbElps.addActionListener(this);
// // rbCirc.addActionListener(this);

// // return menubar;
// // }
// // class SketchPanel extends JPanel {

// // public SketchPanel(){
// // setBorder(BorderFactory.createLineBorder(Color.black));
// // setBackground(Color.white);

// // // addMouseListener(new MouseAdapter(){
// // // public void mousePressed(MouseEvent e){
// // // System.out.println(e.getX());
// // // System.out.println(e.getY());
// // // }
// // // });

// // // addMouseMotionListener(new MouseAdapter(){
// // // public void mouseDragged(MouseEvent e){
// // // System.out.println(e.getX());
// // // System.out.println(e.getY());
// // // Graphics g=getGraphics();
// // // g.setColor(Color.RED);
// // // g.fillOval(e.getX(),e.getY(),20,20);
// // // }
// // // });
// // }
// // }