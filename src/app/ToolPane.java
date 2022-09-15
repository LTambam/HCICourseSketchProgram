package app;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ToolPane extends JPanel implements MenuConstants, ActionListener{
    SketchFrame sf;
    JToolBar tb1;
    JToggleButton selBtn;
    JToolBar tb2;

    ToolPane(SketchFrame sf){
        this.sf = sf;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        tb1 = createToolBar1();
        add(tb1, gbc);
        // tb2 = createToolBar2();
        // add(tb2, gbc);
        // setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        // setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        // add(Box.createRigidArea(new Dimension(0, 5)));
        // this.tb1 = createToolBar1();
        // add(tb1);
        // add(Box.createRigidArea(new Dimension(0, 5)));
        // this.tb2 = createToolBar2();
        // add(tb2);
        // add(Box.createRigidArea(new Dimension(0, 5)));
    }

    public void actionPerformed(ActionEvent e) {
        
        String cmdText = e.getActionCommand();
        sf.switchFocus();
        System.out.println(cmdText);
        switch (cmdText){
            case formatColor:
                sf.handleColorDialog();
                break;
            case editUndo:
                sf.handleUndo();
                break;
            case editRedo:
                sf.handleRedo();
                break;
            case editCut:
                sf.handleCut();
                break;
            case editCopy:
                sf.handleCopy();
                break;
            case editPaste:
                sf.handlePaste();
                break;
            case editDelete:
                sf.handleUndo();
                break;
            case editMove:
                sf.handleMove();
                break;
            case editGroup:
                sf.handleGroup();
                break;
            case editUngroup:
                sf.handleUngroup();
                break;
            case editSelectAll:
                sf.handleSelectAll();
                break; 
            case modeFree:
                sf.changeMode(new ModeFree(sf));
                break;
            case modeLine:
                sf.changeMode(new ModeLine(sf));
                break;
            case modeRect:
                sf.changeMode(new ModeRect(sf));
                break;
            case modeSqre:
                sf.changeMode(new ModeSqre(sf));
                break;
            case modeElps:
                sf.changeMode(new ModeElps(sf));
                break;
            case modeCirc:
                sf.changeMode(new ModeCirc(sf));
                break;
            case modeSelect:
                sf.changeMode(new ModeSelect(sf));
                break;
            default:
                break;
        }
    }
    public JToggleButton createToggleToolbarButton(String s, JToolBar toToolBar, ButtonGroup toGroup, ActionListener al) {
        JToggleButton tmp = new JToggleButton(s);
        tmp.addActionListener(al);
        toGroup.add(tmp);
        toToolBar.add(tmp);
        toToolBar.add(Box.createRigidArea(new Dimension(5, 0)));
        return tmp;
    }
    public JButton createToolbarButton(String s, JToolBar toToolBar, ActionListener al) {
        JButton tmp = new JButton(s);
        tmp.addActionListener(al);
        toToolBar.add(tmp);
        toToolBar.add(Box.createRigidArea(new Dimension(5, 0)));
        return tmp;
    }
    public JToolBar createToolBar1() {
        JToolBar tb1 = new JToolBar();

        ButtonGroup modeGroup = new ButtonGroup();
        createToggleToolbarButton(modeFree, tb1, modeGroup, this).setSelected(true);
        createToggleToolbarButton(modeLine, tb1, modeGroup, this);
        createToggleToolbarButton(modeRect, tb1, modeGroup, this);
        createToggleToolbarButton(modeSqre, tb1, modeGroup, this);
        createToggleToolbarButton(modeElps, tb1, modeGroup, this);
        createToggleToolbarButton(modeCirc, tb1, modeGroup, this);
        selBtn = createToggleToolbarButton(modeSelect, tb1, modeGroup, this);

        // tb1.add(Box.createRigidArea(new Dimension(5, 5)));
        tb1.add(Box.createGlue());
        // tb1.addSeparator();
        createToolbarButton(formatColor, tb1, this);
        createToolbarButton(editGroup, tb1, this);
        createToolbarButton(editUngroup, tb1, this);
        createToolbarButton(editCut, tb1, this);
        createToolbarButton(editCopy, tb1, this);
        createToolbarButton(editPaste, tb1, this);
        createToolbarButton(editDelete, tb1, this);
        createToolbarButton(editUndo, tb1, this);
        createToolbarButton(editRedo, tb1, this);
        createToolbarButton(editMove, tb1, this);
        createToolbarButton(editSelectAll, tb1, this);
        // tb1.add()

        return tb1;

    }
    public JToolBar createToolBar2() {
        JToolBar tb2 = new JToolBar();

        

        return tb2;
    }
    public void setSelBtn(Boolean bool){
        selBtn.setSelected(bool);
    }
}
