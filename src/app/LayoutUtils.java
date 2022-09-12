package app;

import javax.swing.*;
import java.awt.event.*;

public class LayoutUtils implements MenuConstants {
    public static JMenu createMenu(String s, JMenuBar toMenuBar) {
        JMenu tmp = new JMenu(s);
        toMenuBar.add(tmp);
        return tmp;
    }
    public static JMenuItem createMenuItem(String s, JMenu toMenu, ActionListener al) {
        JMenuItem tmp = new JMenuItem(s);
        tmp.addActionListener(al);
        toMenu.add(tmp);
        return tmp;
    }
    public static JMenuItem createMenuItem(String s, JMenu toMenu, int aclKey, ActionListener al) {
        JMenuItem tmp = new JMenuItem(s);
        tmp.addActionListener(al);
        tmp.setAccelerator(KeyStroke.getKeyStroke(aclKey, ActionEvent.CTRL_MASK));
        toMenu.add(tmp);
        return tmp;
    }
    public static JMenuItem createMenuItem(String s, JMenuBar toMenuBar, int aclKey, ActionListener al) {
        JMenuItem tmp = new JMenuItem(s);
        tmp.addActionListener(al);
        // tmp.setAccelerator(KeyStroke.getKeyStroke(aclKey, ActionEvent.CTRL_MASK));
        toMenuBar.add(tmp);
        return tmp;
    }
    public static JRadioButtonMenuItem createRadioMenuItem(String s, JMenuBar toMenuBar, ButtonGroup toGroup, ActionListener al) {
        JRadioButtonMenuItem tmp = new JRadioButtonMenuItem(s);
        tmp.addActionListener(al);
        toGroup.add(tmp);
        toMenuBar.add(tmp);
        return tmp;
    }
    public static void createMenuBar(SketchFrame sf) {
        JMenuBar mb = new JMenuBar();

        JMenu fileMenu = createMenu(fileText, mb);
        JMenu editMenu = createMenu(editText, mb);
        
        createMenuItem(fileOpen, mb, KeyEvent.VK_O, sf);
        createMenuItem(fileSave, mb, KeyEvent.VK_S, sf);
        createMenuItem(fileOpen, fileMenu, KeyEvent.VK_O, sf);
        createMenuItem(fileSave, fileMenu, KeyEvent.VK_S, sf);
        createMenuItem(fileNew, fileMenu, sf);
        createMenuItem(fileSaveAs, fileMenu, sf);
        fileMenu.addSeparator();
        createMenuItem(fileExit, fileMenu, sf);
        
        createMenuItem(formatColor, editMenu, KeyEvent.VK_Q, sf);
        editMenu.addSeparator();
        createMenuItem(editCut, editMenu, KeyEvent.VK_X, sf);
        createMenuItem(editCopy, editMenu, KeyEvent.VK_C, sf);
        createMenuItem(editPaste, editMenu, KeyEvent.VK_V, sf);
        createMenuItem(editDelete, editMenu, KeyEvent.VK_DELETE, sf);
        createMenuItem(editUndo, editMenu, KeyEvent.VK_Z, sf);
        createMenuItem(editRedo, editMenu, KeyEvent.VK_Y, sf);
        createMenuItem(editMove, editMenu, KeyEvent.VK_M, sf);
        createMenuItem(editSelectAll, editMenu, KeyEvent.VK_A, sf);

        sf.setJMenuBar(mb);
    }
}
