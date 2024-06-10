package sample.macroobj;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class MacroObjectManager {
    private static MacroObjectManager instance;
    protected static final ArrayList<Territory> macroObjects = new ArrayList<>();

    public MacroObjectManager() {
        School school = new School(70, 50);
        addMacroObject(school);
        //getMacroObjects().add(macroObject);
        Expansion expansion = new Expansion(1100, 50);
        addMacroObject(expansion);

        Sibyua sibyua = new Sibyua(600, 450);
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
