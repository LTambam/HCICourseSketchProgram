package app;



public class SketchyPad implements MenuConstants {
    
    SketchFrame sf;
    private String fileName = "Untitled";
    // private boolean saved = true;
    private String applicationName = "SketchyPad";
    // FileOperation fileHandler;
    
    SketchyPad() {
        sf = new SketchFrame(fileName + " - " + applicationName, this);
        
    }
    public static void main(String[] args) {
        new SketchyPad();
    }
}

// ////////////////////////////////////
        // if (cmdText.equals(fileNew))
        // fileHandler.newFile();
        // else if (cmdText.equals(fileOpen))
        // fileHandler.openFile();
        // ////////////////////////////////////
        // else if (cmdText.equals(fileSave))
        // fileHandler.saveThisFile();
        // ////////////////////////////////////
        // else if (cmdText.equals(fileSaveAs))
        // fileHandler.saveAsFile();
        // ////////////////////////////////////
        // else if (cmdText.equals(fileExit)) {
        // if (fileHandler.confirmSave())
        // System.exit(0);
        // }