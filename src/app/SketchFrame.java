package app;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D.Double;

// try the toolbar instead of the menubar for performing certain operations.

public class SketchFrame extends JFrame implements ActionListener, MenuConstants{
    SketchyPad sp;
    int state = 0;
    int cursor = Cursor.DEFAULT_CURSOR;
    boolean moveCmd = false;

    static final int sel_thr = 10;

    SketchPane p;
    JPanel mp;
    ToolPane tbp;

    JColorChooser colorChooser = null;
    JDialog colorDialog = null;
    Color color = Color.black;

    Mode mode;

    public ArrayList<SketchComponent> sketchAl = new ArrayList<SketchComponent>();
    ArrayList<SketchComponent> copyAl = new ArrayList<SketchComponent>();
    Stack<SketchCmd> cmdStack = new Stack<SketchCmd>();
    Stack<SketchCmd> undoCmdStack = new Stack<SketchCmd>();

    Rectangle rect;
    Double elps;
    Point clickPoint, prevPoint, currPoint, startPoint, movePoint;
    SketchShape ss;

    SketchFrame(String title, SketchyPad sp){
        super(title);

        this.sp = sp;

        p = new SketchPane(this);

        LayoutUtils.createMenuBar(this);

        tbp = new ToolPane(this);

        mp = new JPanel();
        mp.setLayout(new BoxLayout(mp, BoxLayout.PAGE_AXIS));
        mp.setOpaque(true);
        mp.setBackground(new Color(0, 0, 0));
        mp.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 2));
        mp.add(Box.createRigidArea(new Dimension(0, 2)));
        mp.add(tbp);
        mp.add(Box.createRigidArea(new Dimension(0, 2)));
        mp.add(p);
        mp.add(Box.createGlue());
        
        this.setContentPane(mp);

        pack();

        setSize(1000, 500);

        WindowListener frameClose = new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                // if (fileHandler.confirmSave())
                System.exit(0);
            }
        };
        addWindowListener(frameClose);
        
        p.requestFocusInWindow();
        p.addKeyListener(new KeyEventAdapter(this));

        setVisible(true);

        mode = new ModeFree(this);
        System.out.println("Free again");
    }

    public void createAndPushCmd(String cmd, int index, SketchShape sketch){
        cmdStack.push(new SketchCmd(cmd, index, sketch));
    }
    public void switchFocus(){
        p.requestFocusInWindow();
    }
    public void actionPerformed(ActionEvent e) {
        String cmdText = e.getActionCommand();
        switchFocus();
        System.out.println("SF is focus: "+isFocusOwner());
        System.out.println(cmdText);
                
        switch (cmdText){
            case formatColor:
                handleColorDialog();
                break; 
            case editUndo:
                handleUndo();
                break; 
            case editRedo:
            //code that pops objects off the new temp stack and puts them on the stack if there is any left
                handleRedo();
                break;
            case editCut:
                handleCut();
                break; 
            case editCopy:
                handleCopy();
                break;
            case editPaste:
                handlePaste();
                break;
            case editDelete:
                handleUndo();
                break; 
            case editMove:
                handleMove();
                break; 
            case editGroup:
                handleGroup();
                break; 
            case editUngroup:
                handleUngroup();
                break; 
            case editSelectAll:
                handleSelectAll();
                break; 
            default:
                break;
        }
    }
    void changeMode(Mode mode){
        this.mode = mode;
        moveCmd = false;
        state = 0;
    }
    void handleColorDialog() {
        if (colorChooser == null){
            colorChooser = new JColorChooser();
        }
        if (colorDialog == null){
            colorDialog = JColorChooser.createDialog(this,
                    formatColor,
                    false,
                    colorChooser,
                    new ActionListener() {
                        public void actionPerformed(ActionEvent evvv) {
                            color = colorChooser.getColor();
                        }
                    },
                    null);
        }
        colorDialog.setVisible(true);
    }
    void handleSelectAll(){
        changeMode(new ModeSelect(this));
        tbp.setSelBtn(true);           
        state = 1;
        Iterator<SketchComponent> it = sketchAl.iterator();
        while (it.hasNext()){
            SketchComponent sg = it.next();
            sg.setSelected(true);
        }
    }
    public void handleUndo(){
        if (!cmdStack.isEmpty()){
            SketchCmd tempSC = cmdStack.pop();
            if(tempSC.cmd == modeSqre || tempSC.cmd == modeCirc || tempSC.cmd == modeRect || tempSC.cmd == modeElps || tempSC.cmd == modeLine || tempSC.cmd == modeFree){
                // remove the item from the arraylist and add it to the undoCmdStack
            }else if(tempSC.cmd == editPaste){
                // remove the most recent item(s) from the list
            }else if(tempSC.cmd == editDelete || tempSC.cmd == editCut){
                // add back the deleted item(s)
            }else if(tempSC.cmd == editMove){
                // return the moved item(s) to the original location
            }else if(tempSC.cmd == editGroup){
                // ungroup the items that were grouped
            }else if(tempSC.cmd == editUngroup){
                // group the items that were ungrouped
            }
            undoCmdStack.push(tempSC);
        }
        
    }
    public void handleRedo(){
        if (!undoCmdStack.isEmpty()){
            SketchCmd tempSC = undoCmdStack.pop();
            if(tempSC.cmd == modeSqre || tempSC.cmd == modeCirc || tempSC.cmd == modeRect || tempSC.cmd == modeElps || tempSC.cmd == modeLine || tempSC.cmd == modeFree){
                // add back the item from the undoCmdStack to the arraylist and the cmdStack
            }else if(tempSC.cmd == editPaste){
                // add back item(s)
            }else if(tempSC.cmd == editDelete || tempSC.cmd == editCut){
                // delete item(s)
            }else if(tempSC.cmd == editMove){
                // move item(s) again
            }else if(tempSC.cmd == editGroup){
                // group the items again
            }else if(tempSC.cmd == editUngroup){
                // ungroup the items again
            }
            cmdStack.push(tempSC);
        }
    }
    public void handleCut(){
        // if(mode==modeSelect && state==1){
        //     copyStack.clear();
        //     for(int i=0; i<sketchAl.size(); i++){
        //         if (sketchAl.get(i).isSelected){
        //             copyStack.push(sketchAl.get(i));
        //             continue;
        //         }
        //     }
        // }
}
    public void handleCopy(){
        // if(mode==modeSelect && state==1){
        //     copyStack.clear();
        //     for(int i=0; i<sketchAl.size(); i++){
        //         if (sketchAl.get(i).isSelected){
        //             copyStack.push(sketchAl.get(i));
        //             continue;
        //         }
        //     }
        // }
    }
    public void handlePaste(){
        // Point newPoint = new Point();
        // newPoint = f.getContentPane().getMousePosition();
        
        // for(int i=0; i<copyStack.size(); i++){
        //     SketchShape tmpSS = new SketchShape();
        //     tmpSS = copyStack.get(i);

        //     Point oldPoint = new Point();
        //     oldPoint.setLocation(tmpSS.get(0));
            
        //     if (newPoint == oldPoint){
        //         newPoint.translate(20, 20);
        //     }
        //     tmpSS.applyTranslation(newPoint.x-oldPoint.x, newPoint.y-oldPoint.y);

        //     sketchStack.push((SketchShape)tmpSS.clone());
        // }
    }
    public void handleDelete(){
        // if(mode==modeSelect && state ==1){
        //     for(int i=0; i<sketchAl.size(); i++){
        //         if (sketchAl.get(i).isSelected){
        //             sketchAl.remove(i);
        //             i = i-1;
        //         }
        //     }
        // }
    }
    public void handleMove(){
        if(mode.getMode()==modeSelect){
            moveCmd = true;
        }
    }
    public void handleGroup(){
        if(mode.getMode()==modeSelect && state==1){
            Iterator<SketchComponent> it = sketchAl.iterator();
            SketchNode sg2 = new SketchNode();
            while (it.hasNext()){
                SketchComponent sg = it.next();
                if(sg.checkSelected()){
                    sg.setSelected(false);
                    sg2.add(sg);
                    sketchAl.remove(sg);
                }
            }
            System.out.println(sg2.getSize());
            if(sg2.getSize()>0){
                sg2.setSelected(true);
                sketchAl.add(sg2);
            }
        }
    }
    public void handleUngroup(){
        if(mode.getMode()==modeSelect && state==1){
            Iterator<SketchComponent> it = sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sg = it.next();
                if(sg.checkSelected() && sg.getSize()>1){
                    int i=0;
                    while(sg.getSize()>0){
                        SketchComponent sc = sg.getChild(i);
                        sc.setSelected(true);
                        sketchAl.add(sc);
                        sg.remove(sc);
                    }
                    sketchAl.remove(sg);
                }
            }
        }
    }
}
