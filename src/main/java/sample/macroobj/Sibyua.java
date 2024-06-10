package sample.macroobj;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.lab4.Paths;
import sample.microobj.Shaman;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Sibyua extends Territory{
    public Sibyua(float x, float y) {
        super();
        //String IMAGE_PATH = "/sample/lab4/sibyya.png";
        //super.setImagePath(IMAGE_PATH);
        setxPos(x);
        setyPos(y);
        setName("Станція Сібуя");
    }

    private final ArrayList<String> microObjectsNames = new ArrayList<>();

    @Override
    public void loadImage() throws IOException {
        URL imageURL = getClass().getResource(Paths.MACRO_SIBYUA);
        URL imageURL1 = getClass().getResource(Paths.shadowMacro);
        if (imageURL != null) {
            this.imageView = new ImageView(imageURL.toExternalForm());

            Image image = new Image(imageURL1.openStream());

            shadowImage = new ImageView(image);
        } else {
            System.out.println("Failed to load image");
        }
    }

//    protected void addNewMicroObject(Object object){
//        if (object instanceof Shaman) {
//            if(microObjects.contains((Shaman) object)) {
//                return;
//            }
//            microObjectsNames.add(((Shaman) object).getName());
//            microObjects.add((Shaman) object);
//        }
//    }
//
//    protected void removeNewMicroObject(Object object) {
//        if (object instanceof Shaman) {
//            if(microObjects.contains((Shaman)object)) {
//                microObjects.remove(object);
//            }
//
//            int indexToRemove = microObjectsNames.indexOf(((Shaman) object).getName());
//            if (indexToRemove != -1) {
//                microObjectsNames.remove(indexToRemove);
//            }
//        }
//    }
}
