package sample.macroobj;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sample.microobj.Shaman;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Territory {
    protected String name;
    protected static String imagePath;
    protected static String imagePath1;
    protected static ArrayList<Shaman> microObjects;
    protected ArrayList<String> microObjectsNames;
    protected final static int height = 300;
    protected final static int width = 400;
    protected float xPos;
    protected float yPos;
    protected ImageView imageView;
    protected static  URL shadowURL;
    protected static  ImageView shadowImage;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 24));

        gc.fillText(getName(), getxPos() + 82 , getyPos() + 320);

        if(imageView == null || shadowImage == null) {
            try {
                loadImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        gc.drawImage(shadowImage.getImage(), getxPos() - 128 , getyPos() + 87);
        gc.drawImage(imageView.getImage(), getxPos(), getyPos());

//        // Draw micro-object names
//        gc.setFont(new Font("Arial", 16));
//        gc.setFill(Color.BLUE);
//        String microObjectNames = getMicroObjectsNames();
//        gc.fillText(microObjectNames, getxPos(), getyPos() - 10);
    }
//
//    public String getMicroObjectsNames() {
//        return String.join(", ", microObjectsNames);
//    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        Territory.imagePath = imagePath;
    }

    public void loadImage() throws IOException {
//        URL imageURL = getClass().getResource(getImagePath());
//        URL imageURL1 = getClass().getResource(getImagePath1());
//        if (imageURL != null) {
//              this.imageView = new ImageView(imageURL.toExternalForm());
//
//              Image image = new Image(imageURL1.openStream());
//
//              shadowImage = new ImageView(image);
//        } else {
//            System.out.println("Failed to load image");
//        }

        //пришлось закоментити та перевизначити методи в кожному макрооб'єкті щоб об'єкти малювались з різними фонами
    }


    protected void addNewMicroObject(Object object){
        if (object instanceof Shaman) {
            if(microObjects.contains((Shaman) object)) {
                return;
            }
            microObjectsNames.add(((Shaman) object).getName());
            microObjects.add((Shaman) object);
        }
    }

    protected void removeNewMicroObject(Object object) {
        if (object instanceof Shaman) {
            if(microObjects.contains((Shaman)object)) {
                microObjects.remove(object);
            }

            int indexToRemove = microObjectsNames.indexOf(((Shaman) object).getName());
            if (indexToRemove != -1) {
                microObjectsNames.remove(indexToRemove);
            }
        }
    }


}