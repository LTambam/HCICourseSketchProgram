package app;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

class FileOperation {
    SketchFrame sf;

    boolean saved;
    boolean newFileFlag;
    String fileName;
    String applicationTitle = "SketchFrame";

    File fileRef;
    JFileChooser chooser;

    boolean isSave() {
        return saved;
    }
    void setSave(boolean saved) {
        this.saved = saved;
    }
    String getFileName() {
        return new String(fileName);
    }
    void setFileName(String fileName) {
        this.fileName = new String(fileName);
    }
    FileOperation(SketchFrame sf) {
        this.sf = sf;

        saved = true;
        newFileFlag = true;
        fileName = new String("Untitled");
        fileRef = new File(fileName);
        this.sf.setTitle(fileName + " - " + applicationTitle);

        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
    }
    boolean saveFile(File temp) {
        try {
            FileOutputStream fos = new FileOutputStream(temp);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(sf.sketchAl);
            oos.close();
        }
        catch (IOException e) {
            updateStatus(temp, false);
            e.printStackTrace();
            return false;
        }
        updateStatus(temp, true);
        return true;
    }
    boolean saveThisFile() {
        if (!newFileFlag) {
            return saveFile(fileRef);
        }
        return saveAsFile();
    }
    boolean saveAsFile() {
        File temp = null;
        chooser.setDialogTitle("Save As...");
        chooser.setApproveButtonText("Save Now");
        chooser.setApproveButtonMnemonic(KeyEvent.VK_S);
        chooser.setApproveButtonToolTipText("Click me to save!");

        do {
            if (chooser.showSaveDialog(this.sf) != JFileChooser.APPROVE_OPTION){
                return false;
            }
            temp = chooser.getSelectedFile();
            if (!temp.exists()){
                break;
            }
            if (JOptionPane.showConfirmDialog(this.sf, "<html>" + temp.getPath() + " already exists.<br>Do you want to replace it?<html>",
                    "Save As", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                break;
            }
        } while (true);
        return saveFile(temp);
    }
    boolean openFile(File temp) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(temp);
            ois = new ObjectInputStream(fis);
            // @SuppressWarnings("unchecked")
            sf.sketchAl = (ArrayList<SketchComponent>) ois.readObject();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            updateStatus(temp, false);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            updateStatus(temp, false);
            return false;
        } finally {
            try {
                ois.close();
                fis.close();
            } catch (IOException e) {
                
            }
        }
        updateStatus(temp, true);
        return true;
    }
    void openFile() {
        if (!confirmSave()){
            return;
        }
        chooser.setDialogTitle("Open File...");
        chooser.setApproveButtonText("Open this");
        chooser.setApproveButtonMnemonic(KeyEvent.VK_O);
        chooser.setApproveButtonToolTipText("Click me to open the selected file.!");

        File temp = null;
        do {
            if (chooser.showOpenDialog(this.sf) != JFileChooser.APPROVE_OPTION){
                return;
            }
            temp = chooser.getSelectedFile();
            if (temp.exists()){
                break;
            }

            JOptionPane.showMessageDialog(this.sf, "<html>" + temp.getName() + "<br>file not found.<br>" +
                            "Please verify the correct file name was given.<html>",
                            "Open", JOptionPane.INFORMATION_MESSAGE);

        } while (true);
        // this.sfd.ta.setText("");
        if (!openFile(temp)) {
            fileName = "Untitled";
            saved = true;
            this.sf.setTitle(fileName + " - " + applicationTitle);
        }
        if (!temp.canWrite()){
            newFileFlag = true;
        }
    }
    void updateStatus(File temp, boolean saved) {
        if (saved) {
            this.saved = true;
            fileName = new String(temp.getName());
            if (!temp.canWrite()) {
                fileName += "(Read only)";
                newFileFlag = true;
            }
            fileRef = temp;
            sf.setTitle(fileName + " - " + applicationTitle);
            // sfd.statusBar.setText("File : " + temp.getPath() + " saved/opened successfully.");
            newFileFlag = false;
        } else {
            // sfd.statusBar.setText("Failed to save/open : " + temp.getPath());
        }
    }
    boolean confirmSave() {
        String strMsg = "<html>The file has been changed.<br>" +
                "Do you want to save the changes?<html>";
        if (!saved) {
            int x = JOptionPane.showConfirmDialog(this.sf, strMsg, applicationTitle,
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (x == JOptionPane.CANCEL_OPTION){
                return false;
            }
            if (x == JOptionPane.YES_OPTION && !saveAsFile()){
                return false;
            }
        }
        return true;
    }
    void newFile() {
        if (!confirmSave()){
            return;
        }
        fileName = new String("Untitled");
        fileRef = new File(fileName);
        saved = true;
        newFileFlag = true;
        this.sf.setTitle(fileName + " - " + applicationTitle);
    }
}