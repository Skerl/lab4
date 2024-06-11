package sample.macroobj;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class MacroObjectManager {
    private static MacroObjectManager instance;
    protected static final ArrayList<Territory> macroObjects = new ArrayList<>();
    public static final int X_POS_SCHOOL = 70;
    public static final int Y_POS_SCHOOL = 50;
    public static final int X_POS_EXPANCION = 1100;
    public static final int Y_POS_EXPANCION = 50;
    public static final int X_POS_SIBYUA = 600;
    public static final int Y_POS_SIBYUA = 450;

    public final static int HEIGHT = 300;
    public final static int WIDTH = 400;


    public MacroObjectManager() {
        School school = new School(X_POS_SCHOOL, Y_POS_SCHOOL);
        addMacroObject(school);
        //getMacroObjects().add(macroObject);
        Expansion expansion = new Expansion(X_POS_EXPANCION, Y_POS_EXPANCION);
        addMacroObject(expansion);

        Sibyua sibyua = new Sibyua(X_POS_SIBYUA, Y_POS_SIBYUA);
        addMacroObject(sibyua);
    }

    public synchronized ArrayList<Territory> getMacroObjects() {
        return macroObjects;
    }

    public static MacroObjectManager getInstance() {
        if (instance == null) {
            instance = new MacroObjectManager();
        }
        return instance;
    }

    public void addMacroObject(Territory macroObject) {
        getMacroObjects().add(macroObject);
    }

    public void drawAllMacroObjects(GraphicsContext gc) {
        for (Territory macroObject : getMacroObjects()) {
            macroObject.draw(gc);
        }
    }

}
