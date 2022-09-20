package app;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D.Double;
import java.io.*;

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
    ArrayList<SketchComponent> deleteAl = new ArrayList<SketchComponent>();
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
    public void switchFocus(){
        p.requestFocusInWindow();
    }
    public void actionPerformed(ActionEvent e) {
        String cmdText = e.getActionCommand();
        switchFocus();
        System.out.println(cmdText);

        switch (cmdText){
            case formatColor:
                handleColorDialog();
                break; 
            case editUndo:
                handleUndo();
                break; 
            case editRedo:
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
                handleDelete();
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
        p.repaint();
    }
    void changeMode(Mode mode){
        this.mode = mode;
        moveCmd = false;
        state = 0;
    }
    public void pushCmd(SketchCmd cmd){
        undoCmdStack.clear();
        cmdStack.push(cmd);
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
            SketchComponent sn = it.next();
            sn.setSelected(true);
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
        if(mode.getMode()==modeSelect && state==1){
            Iterator<SketchComponent> it = sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sn = it.next();
                if(sn.checkSelected()){
                    copyAl.add(sn);
                }
            }
            SketchCmd cmd = new SketchCmd(editCut);
            
            it = copyAl.iterator();
            while (it.hasNext()){
                SketchComponent sn = it.next();
                cmd.addComponent(sketchAl.indexOf(sn), sn);
                
                sketchAl.remove(sn);
            }
            pushCmd(cmd);
        }
    }
    public void handleCopy(){
        copyAl.clear();
        if(mode.getMode()==modeSelect && state==1){
            Iterator<SketchComponent> it = sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sn = it.next();
                if(sn.checkSelected()){
                    copyAl.add(copySketchComponent(sn));
                }
            }
        }
    }
    public void handlePaste(){ // move is not working with groups
        SketchNode tmpNode = new SketchNode();

        Iterator<SketchComponent> it = copyAl.iterator();
        while (it.hasNext()){
            SketchComponent sn = it.next();
            tmpNode.add(copySketchComponent(sn));
        }

        Point tlPoint = new Point();
        int[] outerBounds = tmpNode.getBounds();
        tlPoint.setLocation(outerBounds[0], outerBounds[1]);
        // System.out.println("Top Left " + tlPoint.x+ " " +tlPoint.y);

        Point newtlPoint = new Point();
        if(p.getMousePosition() != null){
            newtlPoint = p.getMousePosition();
            System.out.println("in here");
        }else{
            newtlPoint.setLocation(50, 50);;
        }
        if (newtlPoint.getLocation() == tlPoint.getLocation()){ 
            newtlPoint.translate(100, 100); 
        }
        System.out.println("New Top Left "+newtlPoint.x+" "+ newtlPoint.y);

        int tx = newtlPoint.x-tlPoint.x;
        int ty = newtlPoint.y-tlPoint.y;

        SketchCmd cmd = new SketchCmd(editPaste);
    
        it = copyAl.iterator();
        while (it.hasNext()){
            SketchComponent sn = copySketchComponent(it.next());
            sn.applyTranslation(tx, ty);
            sketchAl.add(sn);
            
            //these lines are for adding cmd to stack
            cmd.addComponent(sketchAl.size()-1, sn);
        }
        pushCmd(cmd);
    }
    public void handleDelete(){
        if(mode.getMode()==modeSelect && state==1){
            Iterator<SketchComponent> it = sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sn = it.next();
                if(sn.checkSelected()){
                    deleteAl.add(sn);
                }
            }
            SketchCmd cmd = new SketchCmd(editDelete);

            it = deleteAl.iterator();
            while (it.hasNext()){
                SketchComponent sn = it.next();
                cmd.addComponent(sketchAl.indexOf(sn), sn);
                sketchAl.remove(sn);
            }
            pushCmd(cmd);
            deleteAl.clear();
        }
    }
    public void handleMove(){
        if(mode.getMode()==modeSelect && state==1){
            moveCmd = true;
        }
    }
    public void handleGroup(){
        if(mode.getMode()==modeSelect && state==1){
            SketchNode sn2 = new SketchNode();

            SketchCmd cmd = new SketchCmd(editGroup);

            int i = 0;
            Iterator<SketchComponent> it = sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sn = it.next();
                if(sn.checkSelected()){
                    deleteAl.add(sn);
                    cmd.addComponent(i, sn);
                    sn2.add(copySketchComponent(sn));
                }
                i++;
            }

            it = deleteAl.iterator();
            while (it.hasNext()){
                SketchComponent sn = it.next();
                sketchAl.remove(sn);
            }
            deleteAl.clear();
            sn2.setSelected(true);
            sketchAl.add(sn2);
            pushCmd(cmd);
        }
    }
    public void handleUngroup(){
        if(mode.getMode()==modeSelect && state==1){
            
            SketchCmd cmd = new SketchCmd(editUngroup);

            SketchComponent sc = new SketchNode();

            int i = 0;
            Iterator<SketchComponent> it = sketchAl.iterator();
            while (it.hasNext()){
                SketchComponent sn = it.next();
                if(sn.checkSelected() && sn.getSize()>1){
                    sc = sn;
                    cmd.addComponent(i, sc);
                }
                i++;
            }
            it = sc.createShallowIterator();
            while(it.hasNext()){
                SketchComponent sn = it.next();
                sn.setSelected(true);
                sketchAl.add(copySketchComponent(sn));
            }
        }
    }
    public SketchComponent copySketchComponent(SketchComponent sc){ //serialize is not working with groups
        SketchComponent clone = null;
        try {
            // Create a byte array output stream
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
 
            // Use byte array output stream to create an object output stream
            ObjectOutputStream oos = new ObjectOutputStream(bos);
 
            // Serialization – Pass the object that we want to copy to the
            // ObjectOutputStream's `writeObject()` method
            oos.writeObject(sc);
            oos.flush();
 
            // toByteArray creates & returns a copy of the stream's byte array
            byte[] bytes = bos.toByteArray();
 
            // Create a byte array input stream
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
 
            // Use byte array input stream to create an object input stream
            ObjectInputStream ois = new ObjectInputStream(bis);
 
            // deserialize the serialized object using ObjectInputStream's
            // `readObject()` method and typecast it to the class of the object
            clone = (SketchComponent) ois.readObject();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
