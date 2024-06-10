package sample.microobj;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sample.lab4.Paths;

import java.util.Objects;

public class ShamanGradeHigh extends ShamanGrade1{
    private String BackPath;
    public ShamanGradeHigh(String Name, int Lvl, int healthpoint, double amountCursedEnergy, float xPos, float yPos) {
        super(Name, Lvl, healthpoint, amountCursedEnergy, xPos, yPos);
        this.BackPath = randomPngForMicroObj(Paths.MICRO_GR_HIGH);
        ImageView imageView = new ImageView(Objects.requireNonNull(getClass().getResource("/sample/lab4/gr2_01.png")).toExternalForm());
        setImagePng(imageView);
    };

    public ShamanGradeHigh() {
        this("ShamanGradeHigh", 7, 100, 5000.0, 300, 300);
    }

    @Override
    public void draw(GraphicsContext gc) {
        float x = super.getXPos();
        float y = super.getYPos();

        gc.setFill(Color.BLACK);
        gc.setStroke(Color.WHITE); // Задаємо білий колір обводки
        gc.setLineWidth(3); // Задаємо товщину обводки

        gc.setFont(new Font("Arial", 12));
        gc.strokeText((getName() + "\nCE " + getAmountCursedEnergy() + "\nHP " + getHp()), x + 27, y-29); // Рисуємо обводку
        gc.fillText((getName() + "\nCE " + getAmountCursedEnergy() + "\nHP " + getHp()), x + 27, y-29);

        if (this.getImageView() == null) {
            this.loadImage();
        }

        if (super.isActive()) {
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeRect(x, y, super.getImageView().getImage().getWidth(), super.getImageView().getImage().getHeight());

        }

        gc.drawImage(super.getGradeImageView().getImage(), x-3, y-24);
        gc.drawImage(super.getImageView().getImage(), x, y);
    }


    @Override
    public void loadImage() {
        if (this.getImageView() == null || this.getGradeImageView() == null) {
            this.setImageView(new ImageView(Objects.requireNonNull(getClass().getResource(this.BackPath)).toExternalForm()));
            this.setGradeImageView(new ImageView(Objects.requireNonNull(getClass().getResource(Paths.gradeHighPath)).toExternalForm()));
            this.setHeight((int) this.getImageView().getImage().getHeight());
            this.setWidth((int) this.getImageView().getImage().getWidth());
        }
    }

}
