package sample.microobj;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sample.lab4.Paths;

import java.io.Serializable;
import java.util.Random;
import java.util.*;

public class Shaman implements Comparable<Shaman>, Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private String name; // Ім'я
    private int hp; // здоров'я
    private int lvl; // Рівень //-
    private double powerFromMob; // Потужність з мобів, розраховується з //-
    private double amountCursedEnergy; // Кіл проклятої енергії
    private Map<String, MobsInfo> killedMobs = new HashMap<>(); // Вбиті прокляття
    private static double forceThreshold; // Поріг сили //-

    private float xPos;
    private float yPos;
    private boolean isActive = false;
    private int width;
    private int height;
    private transient ImageView imageView;
    private transient static ImageView imagePng;
    private transient ImageView gradeImageView;
    private String BackPath;
    private final int uniqueID;

    public int getUniqueID() {
        return uniqueID;
    }

    public int createID() {
        return (int) System.currentTimeMillis() / 432;
    }


    public static ImageView getImagePng() {
        return imagePng;
    }

    public ImageView getGradeImageView() {
        return gradeImageView;
    }

    public void setGradeImageView(ImageView gradeImageView) {
        this.gradeImageView = gradeImageView;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }



    public Shaman(String Name, int Lvl, int healthpoint, double amountCursedEnergy, float xPos, float yPos) {
        this.name = Name;
        this.lvl = Lvl;
        this.hp = healthpoint;
        this.amountCursedEnergy = amountCursedEnergy;
        this.xPos = xPos;
        this.yPos = yPos;
        this.uniqueID = createID();
        this.BackPath = randomPngForMicroObj(Paths.MICRO_GR2);
        ImageView imageView = new ImageView(Objects.requireNonNull(getClass().getResource("/sample/lab4/gr1_01.png")).toExternalForm());
        setImagePng(imageView);

    }

    // КОНСТРУКТОР ЗА ЗАМОВЧУВАННЯМ
    public Shaman() {
        this("Shaman", 5, 100, 1000.0, 100, 100);
    }

    public void EnterKilledMobs(String nameMob, int lvlMob, int count) {
        killedMobs.put(nameMob, new MobsInfo(lvlMob, count));
        powerFromMob += lvlMob * 0.5 * count;
    }

    public static Comparator<Shaman> CursedEnergyComparator
            = Comparator.comparingDouble(o -> o.amountCursedEnergy);


    @Override
    public int compareTo(Shaman o) {
        return this.name.compareTo(o.name);
    }

    class MobsInfo implements Cloneable, Serializable{
        private int lvl; // Рівень моба
        private int count; // Кількість мобів

        public MobsInfo(int lvl, int count) {
            this.lvl = lvl;
            this.count = count;
        }

        @Override
        public String toString() {
            return "[" +
                    "lvl = " + lvl +
                    "  count = " + count +
                    ']';
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }



    //----------------------------------------------------------------------------------------
    // ГЕТЕРИ + СЕТЕРИ + ДЕЯКІ РОФЛИ
    public void setImagePng(ImageView imagePng) {
        this.imagePng = imagePng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

//    public float getxPos() {
//        return xPos;
//    }
//
//    public float getyPos() {
//        return yPos;
//    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public void setPowerFromMob(double powerFromMob) {
        this.powerFromMob = powerFromMob;
    }

    public void setAmountCursedEnergy(double amountCursedEnergy) {
        this.amountCursedEnergy = amountCursedEnergy;
    }

    public void setKilledMobs(Map<String, MobsInfo> killedMobs) {
        this.killedMobs = killedMobs;
    }

    public double getAmountCursedEnergy() {
        return amountCursedEnergy;
    }

    protected boolean isActive() {
        return isActive;
    }

    public void setXPos(float xPos) {
        this.xPos = xPos;
    }

    public void setYPos(float yPos) {
        this.yPos = yPos;
    }

    public void resetKilledMobs(Map<String, MobsInfo> killedMobs) {
        killedMobs.clear();
    }

    public Map<String, MobsInfo> getKilledMobs() {
        return killedMobs;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getLvl() {
        return lvl;
    }

    public double getPowerFromMob() {
        return powerFromMob;
    }



    //ПЕРЕНЕСЕННЯ ЕНЕРГІЇ З ШАМАНА ДО ШАМАНА
    public void transferEnergyTo(Shaman targetShaman, double amount) {
        if (this.amountCursedEnergy >= amount) {
            this.amountCursedEnergy -= amount;
            targetShaman.amountCursedEnergy += amount;
            System.out.println(amount + " amountCursedEnergy перенесено з " + this.name + " до " + targetShaman.name);
        } else {
            System.out.println("У " + this.name + " недостатньо amountCursedEnergy для передачі.");
        }
    }



    //----------------------------------------------------------------------------------------
//    @Override
//    public String toString() {
//        return "Shaman \n" +
//                "name='" + name + '\'' +
//                "\nlvl=" + lvl +
//                "\npower=" + power +
//                "\npowerFromMob=" + powerFromMob +
//                "\namountCursedEnergy=" + amountCursedEnergy +
//                "\ntech=" + tech +
//                "\nkilledMobs=" + killedMobs;
//    }
    // КОНСОЛЬНИЙ ВИВІД В ДВІ СТРІЧКИ З 3 ЛАБИ
    @Override
    public String toString() {
        return  "name = " + name +
                "; hp = " + hp +
                "; lvl = " + lvl +
                "; powerFromMob = " + powerFromMob +
                "; amountCursedEnergy = " + amountCursedEnergy +
                ", xPos=" + xPos +
                ", yPos=" + yPos +
                ", isActive=" + isActive +
                ", imageView=" + imageView +
                ";\nkilledMobs = " + killedMobs +
                ",uniqueID = "  + uniqueID;
    }


    //----------------------------------------------------------------------------------------
    // ДОДАННЯ МІКРОЧЕЛА З 3 ЛАБИ
//    public static Shaman addNewObject() {
//        System.out.println("Введіть спосіб створення об'єкту:");
//        System.out.println("1. Автоматично");
//        System.out.println("2. Вручну");
//
//        int choice = Main.scanner.nextInt();
//        if (choice == 1) {
//            return new Shaman();
//        } else if (choice == 2) {
//            System.out.println("Ім'я:");
//            String name = Main.scanner.next();
//            Main.scanner.nextLine();
//
//            System.out.println("Рівень:");
//            int lvl = Main.scanner.nextInt();
//            Main.scanner.nextLine();
//
//            System.out.println("Здоров'я:");
//            int hp = Main.scanner.nextInt();
//            Main.scanner.nextLine();
//
//            System.out.println("Проклята енергія:");
//            double amountCursedEnergy = Main.scanner.nextDouble();
//            Main.scanner.nextLine();
//
//            float x = 100;
//            float y = 100;
//
//            Shaman newShaman = new Shaman(name, lvl, hp, amountCursedEnergy, x,y, backpath);
//
//            System.out.println("Заповніть інформацію про мобів [Ім'я, рівень, кількість]. -1 означає вихід.");
//            while(true) {
//                String nameMob = Main.scanner.nextLine();
//                if(nameMob.equals("-1")) {
//                    break;
//                }
//                int lvlMob = Main.scanner.nextInt();
//                Main.scanner.nextLine();
//                int countMob = Main.scanner.nextInt();
//                Main.scanner.nextLine();
//
//                newShaman.EnterKilledMobs(nameMob, lvlMob, countMob);
//            }
//
//            return newShaman;
//        } else {
//            return new Shaman();
//        }
//    }


    // ГЛИБИННЕ КОПІЮВАННЯ З 3 ЛАБИ
    @Override
    public Object clone() throws CloneNotSupportedException {
        Shaman clonedShaman = (Shaman) super.clone();
        clonedShaman.killedMobs = new HashMap<>();

        for (Map.Entry<String, MobsInfo> entry : this.killedMobs.entrySet()) {
            MobsInfo clonedMobsInfo = new MobsInfo(entry.getValue().lvl, entry.getValue().count);
            clonedShaman.killedMobs.put(entry.getKey(), clonedMobsInfo);
        }

        return clonedShaman;
    }



    //----------------------------------------------------------------------------------------
    // ВИМОГИ З ДРУГОЇ ЛАБИ
    static {
        //System.out.println("Статичний блок ініціалізації Shaman. поріг сили");
        forceThreshold = 500.0;
    }

    public static double getForceThreshold() {
        return forceThreshold;
    }

    public static void setForceThreshold(double forceThreshold) {
        Shaman.forceThreshold = forceThreshold;
    }

    public  boolean hasEnoughPower() {
        if(powerFromMob > forceThreshold) {
            return true;
        }
        return false;
    }
    //----------------------------------------------------------------------------------------



    protected void loadImage() {
        if (imageView == null || gradeImageView == null) {
            imageView = new ImageView(Objects.requireNonNull(getClass().getResource((this.BackPath)).toExternalForm()));
            gradeImageView = new ImageView(Objects.requireNonNull(getClass().getResource(Paths.grade2Path)).toExternalForm());

            setHeight((int) imageView.getImage().getHeight());
            setWidth((int) imageView.getImage().getWidth());

            //setHeight((int) gradeImageView.getImage().getHeight());
            //setWidth((int) gradeImageView.getImage().getWidth());
        }
    }

    protected void setActive() {
        isActive = !isActive;
        if(isActive) {
            System.out.println(this + "\nactivated\n");
        }else{
            System.out.println(this + "\ndeactivated\n");
        }
    }

    protected float getX() {
        return xPos;
    }

    protected float getY() {
        return yPos;
    }

    protected int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    protected int getWidth() {
        return width;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    protected void draw(GraphicsContext gc) {
        float x = this.xPos;
        float y = this.yPos;

        gc.setFill(Color.BLACK);
        gc.setStroke(Color.WHITE); // Задаємо білий колір обводки
        gc.setLineWidth(3); // Задаємо товщину обводки

        gc.setFont(new Font("Arial", 12));
        gc.strokeText((getName() + "\nCE " + getAmountCursedEnergy() + "\nHP " + getHp()), x + 27, y-29); // Рисуємо обводку
        gc.fillText((getName() + "\nCE " + getAmountCursedEnergy() + "\nHP " + getHp()), x + 27, y-29);


        loadImage();
        if (isActive) {
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeRect(x, y, imageView.getImage().getWidth(), imageView.getImage().getHeight());
        }
        gc.drawImage(gradeImageView.getImage(), x-3, y-24);
        gc.drawImage(imageView.getImage(), x, y);
    }

    protected ImageView getImageView() {
        return imageView;
    }

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }

    protected String randomPngForMicroObj (String path[]) {
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        System.out.println("randomNumber: " + randomNumber);

        return path[randomNumber];
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Shaman shaman)) return false;
        return isActive() == shaman.isActive() &&
                uniqueID == shaman.uniqueID &&
                hp == shaman.getHp() &&
                lvl == shaman.getLvl() &&
                getAmountCursedEnergy() == shaman.getAmountCursedEnergy();
    }


}






