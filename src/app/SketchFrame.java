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
    Boolean pressedOnSketch = false;
    int cursor = Cursor.DEFAULT_CURSOR;
    boolean moveCmd = false;

    static final int sel_thr = 10;

    SketchPane p;
    JPanel mp;
    ToolPane tbp;

    JColorChooser colorChooser = null;
    JDialog colorDialog = null;
    Color color = Color.black;

    FileOperation fileHandler;

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

        fileHandler = new FileOperation(this);

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
                if (fileHandler.confirmSave()){
                    System.exit(0);
                }
            }
        };
        addWindowListener(frameClose);
        
        p.requestFocusInWindow();
        p.addKeyListener(new KeyEventAdapter(this));

        setVisible(true);

        mode = new ModeFree(this);
    }
    public void switchFocus(){
        p.requestFocusInWindow();
    }
    public void actionPerformed(ActionEvent e) {
        String cmdText = e.getActionCommand();
        switchFocus();

        System.out.println(cmdText);
        switch (cmdText){
            case fileNew        -> handleNew();
            case fileOpen       -> handleOpen();
            case fileSave       -> handleSave();
            case fileSaveAs     -> handleSaveAs();
            case fileExit       -> handleExit();
            case fileExport     -> handleExport();
            case formatColor    -> handleColorDialog();
            case editUndo       -> handleUndo();
            case editRedo       -> handleRedo();
            case editCut        -> handleCut();
            case editCopy       -> handleCopy();
            case editPaste      -> handlePaste();
            case editDelete     -> handleDelete();
            case editMove       -> handleMove();
            case editGroup      -> handleGroup();
            case editUngroup    -> handleUngroup();
            case editSelectAll  -> handleSelectAll();
            default             -> throw new IllegalStateException("Invalid Command");
        };
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
    void handleNew(){

    }
    void handleOpen(){

    }
    void handleSave(){

    }
    void handleSaveAs(){

    }
    void handleExit(){

    }
    void handleExport(){

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
            SketchCmd sCmd = cmdStack.pop();
            String cmd = sCmd.cmd;
            if(cmd == modeSqre || cmd == modeCirc || cmd == modeRect || cmd == modeElps || cmd == modeLine || cmd == modeFree || cmd == editPaste){
                // remove the item from the arraylist and add it to the undoCmdStack
                for(int i=sCmd.size()-1; i>=0; i--){
                    sketchAl.remove(sCmd.getComponentIdx(i));
                }
            }else if(cmd == editDelete || cmd == editCut){// issue with undo on groups after cut command
                // add back the deleted item(s)
                for(int i=0; i<sCmd.size(); i++){
                    System.out.println(sCmd.getComponentIdx(i));
                    sketchAl.add(sCmd.getComponentIdx(i), sCmd.getComponent(i));
                }
            }else if(cmd == editMove){
                // return the moved item(s) to the original location
                int tx = sCmd.tx;
                int ty = sCmd.ty;
                for(int i=0; i<sCmd.size(); i++){
                    sketchAl.get(sCmd.getComponentIdx(i)).applyTranslation(-tx, -ty);
                }
            }else if(cmd == editGroup){
                // ungroup the items that were grouped
                sketchAl.remove(sketchAl.size()-1);

                for(int i=0; i<sCmd.size(); i++){
                    sketchAl.add(sCmd.getComponentIdx(i), sCmd.getComponent(i));
                }
            }else if(cmd == editUngroup){
                // group the items that were ungrouped
                for(int i = 0; i<sCmd.size(); i++){
                    Boolean selFlag = true;
                    SketchComponent sc = sCmd.getComponent(i);
                    int idx = sCmd.getComponentIdx(i);
                    for(int j=sc.getGroupSize(); j>0; j--){
                        if (!sketchAl.get(sketchAl.size()-1).checkSelected()){
                            selFlag=false;
                        }
                        sketchAl.remove(sketchAl.size()-1);
                    }
                    sc.setSelected(selFlag);
                    sketchAl.add(idx, sc);
                }
            }
            undoCmdStack.push(sCmd);
        }
    }
    public void handleRedo(){
        if (!undoCmdStack.isEmpty()){
            SketchCmd sCmd = undoCmdStack.pop();
            String cmd = sCmd.cmd;
            if(cmd == modeSqre || cmd == modeCirc || cmd == modeRect || cmd == modeElps || cmd == modeLine || cmd == modeFree || cmd == editPaste){
                // add back the item from the undoCmdStack to the arraylist and the cmdStack
                for(int i=0; i<sCmd.size(); i++){
                    sketchAl.add(sCmd.getComponentIdx(i), sCmd.getComponent(i));
                }
            }else if(cmd == editDelete || cmd == editCut){
                // delete item(s)
                for(int i=sCmd.size()-1; i>=0; i--){
                    sketchAl.remove(sCmd.getComponentIdx(i));
                }
            }else if(cmd == editMove){
                // move item(s) again
                int tx = sCmd.tx;
                int ty = sCmd.ty;
                for(int i=0; i<sCmd.size(); i++){
                    sketchAl.get(sCmd.getComponentIdx(i)).applyTranslation(tx, ty);
                }
            }else if(cmd == editGroup){
                // group the items again
                Boolean selFlag = true;
                SketchComponent sc = new SketchNode();
                for(int i=sCmd.size()-1; i>=0; i--){
                    if (!sketchAl.get(sCmd.getComponentIdx(i)).checkSelected()){
                        selFlag=false;
                    }
                    sketchAl.remove(sCmd.getComponentIdx(i));
                }
                for (int i=0; i<sCmd.size(); i++){
                    sc.add(sCmd.getComponent(i));
                }
                if (selFlag){
                    sc.setSelected(true);
                }
                sketchAl.add(sc);
            }else if(cmd == editUngroup){
                // ungroup the items again
                for(int i = 0; i<sCmd.size(); i++){
                    SketchComponent sc = sCmd.getComponent(i);
                    int idx = sCmd.getComponentIdx(i);
                    Boolean selFlag =  sketchAl.get(idx).checkSelected();

                    Iterator<SketchComponent> it = sc.createShallowIterator();
                    while(it.hasNext()){
                        SketchComponent sn = it.next();
                        sn.setSelected(selFlag);
                        sketchAl.add(copySketchComponent(sn));
                    }
                    sketchAl.remove(idx);
                }
            }
            cmdStack.push(sCmd);
        }
    }
    public void handleCut(){
        if(mode.getMode()==modeSelect && state==1){
            for(int i=0; i < sketchAl.size(); i++){
                SketchComponent sn = sketchAl.get(i);
                if(sn.checkSelected()){
                    copyAl.add(sn);
                }
            }
            SketchCmd cmd = new SketchCmd(editCut);
            
            for(int i=0; i < copyAl.size(); i++){
                SketchComponent sn = copyAl.get(i);
                cmd.addComponent(i, sn);
                sketchAl.remove(sn);
            }
            pushCmd(cmd);
        }
    }
    public void handleCopy(){
        copyAl.clear();
        if(mode.getMode()==modeSelect && state==1){
            for(int i=0; i < sketchAl.size(); i++){
                SketchComponent sn = sketchAl.get(i);
                if(sn.checkSelected()){
                    copyAl.add(copySketchComponent(sn));
                }
            }
        }
    }
    public void handlePaste(){
        SketchNode tmpNode = new SketchNode();

        for(int i=0; i < copyAl.size(); i++){
            SketchComponent sn = copyAl.get(i);
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
    
        for(int i=0; i < copyAl.size(); i++){
            SketchComponent sn = copySketchComponent(copyAl.get(i));
            sn.applyTranslation(tx, ty);
            sketchAl.add(sn);
            cmd.addComponent(sketchAl.size()-1, sn);
        }
        pushCmd(cmd);
    }
    public void handleDelete(){
        if(mode.getMode()==modeSelect && state==1){
            for(int i=0; i < sketchAl.size(); i++){
                SketchComponent sn = sketchAl.get(i);
                if(sn.checkSelected()){
                    deleteAl.add(sn);
                }
            }
            SketchCmd cmd = new SketchCmd(editDelete);

            for(int i=0; i < deleteAl.size(); i++){
                SketchComponent sn = deleteAl.get(i);
                cmd.addComponent(i, sn);
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

            for(int i=0; i < sketchAl.size(); i++){
                SketchComponent sn = sketchAl.get(i);
                if(sn.checkSelected()){
                    deleteAl.add(sn);
                    cmd.addComponent(i, sn);
                    sn2.add(copySketchComponent(sn));
                }
            }
            for(int i=0; i < deleteAl.size(); i++){
                SketchComponent sn = deleteAl.get(i);
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
            int sz = sketchAl.size();

            for(int i=0; i<sz; i++){
                System.out.println(sketchAl.size());
                SketchComponent sn = sketchAl.get(i);
                if(sn.checkSelected() && sn.getSize()>1){
                    cmd.addComponent(i, copySketchComponent(sn));
                    Iterator<SketchComponent> it2 = sn.createShallowIterator();
                    while(it2.hasNext()){
                        SketchComponent sn2 = copySketchComponent(it2.next());
                        sn2.setSelected(true);
                        sketchAl.add(sn2);
                    }
                }
            }
            pushCmd(cmd);
            for(int j=0; j<cmd.size(); j++){
                sketchAl.remove(cmd.getComponentIdx(j));
            }
            
            
        }
    }
    public SketchComponent copySketchComponent(SketchComponent sc){
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
