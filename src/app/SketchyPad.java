package app;

/*
 * Features to add:
 * - box select
 * - make line add a group of lines instead of many individual lines
 * - enforce a file type?
 * Things I need to fix:
 * - figure out why there are additional cmds to undo and redo
 * - enforce maximum stack size
 * - change color of objects after the fact
 */

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

/*
 * List of Commands:
 * Action: Adding a sketch object
 * Mode: Free, Line, Square, Rect, Elps, Circle
 * Cmd: modeFree, modeLine, etc...
 * Undo: Remove object from sketch list (can use object as input to remove method)
 * Redo: Add object back to sketch list (use add method)
 * Requirements: Store SketchComponent object on stack
 * 
 * Action: Pasting sketch Objects
 * Mode: Select
 * Cmd: editPaste
 * Undo: Remove object(s) from sketchAl
 * Redo: add back object(s) in correct location to SketchAl
 * 
 * Action: Delete or cut object
 * Mode: Select
 * Cmd: editDelete or editCut
 * Undo: Add object(s) back to spot in sketchAL (can use add(index, element))
 * Redo: Remove object(s) from sketchAl again (use remove(element)) - Do I need to copy the element again?? or do I just leave whatever was already copied? I think the latter
 * Requirements: Store SketchComponent on stack and index on SketchAl
 * 
 * Action: Move
 * Mode: Select
 * Cmd: editMove (need to add at the end though)
 * Undo: Translate Object(s) back to original location (use applyTranslation(-tx, -ty))
 * Redo: Translate object(s) to final location again (same command as original)
 * Requirements: Index of the object(s), and tx, ty values
 * 
 * Action: Group
 * Mode: Select
 * Cmd: editGroup
 * Undo: Ungroup objects in original index
 * Redo: Group objects again
 * Requirements: Need to know index of objects in sketchAL list, both the group and the original locations.
 * 
 * Action: Ungroup
 * Mode: Select
 * Cmd: editUngroup
 * Undo: Group objects in original index
 * Redo: Ungroup objects again
 * Requirements: Need to know index of objects in sketchAL list, both the group and the original locations.
 */

 