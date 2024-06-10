package sample.macroobj;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.lab4.Paths;
import sample.microobj.Shaman;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class School extends Territory {
    public School(float x, float y){
        super();
        //String IMAGE_PATH = "/sample/lab4/school.png";
        //super.setImagePath(IMAGE_PATH);
        setxPos(x);
        setyPos(y);
        setName("Магічна школа");
    }

    private final ArrayList<String> microObjectsNames = new ArrayList<>();

    @Override
    public void loadImage() throws IOException {
        URL imageURL = getClass().getResource(Paths.MACRO_SCHOOL);
        URL imageURL1 = getClass().getResource(Paths.shadowMacro);
        if (imageURL != null) {
            this.imageView = new ImageView(imageURL.toExternalForm());

            Image image = new Image(imageURL1.openStream());

            shadowImage = new ImageView(image);
        } else {
            System.out.println("Failed to load image");
        }
    }
}
