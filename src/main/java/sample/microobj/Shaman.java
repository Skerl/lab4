package sample.microobj;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sample.lab4.Paths;
import sample.macroobj.*;

import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class Shaman implements Comparable<Shaman>, Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private String name; // Ім'я
    private int hp; // здоров'я
    private int lvl; // Рівень //-
    private double powerFromMob; // Потужність з мобів, розраховується з //-
    private double amountCursedEnergy; // Кіл проклятої енергії
    private Map<String, MobsInfo> killedMobs = new HashMap<>(); // Вбиті прокляття
    private static double forceThreshold; // Поріг сили //-

    private boolean handler = false;

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
    private Territory location;
    private float xPosRandomGoal = -1;
    private float yPosRandomGoal = -1;
    private boolean complete = true;
    private boolean isHolding = false;
    private boolean previousSchool;
    private boolean previousSibyua;
    private boolean previousExpansion;
    private int tickExisted;
    float stackHealt = 0;
    float stackEnergy = 0;
    float stackHealtEX = 0;
    float stackEnergyEX = 0;
    float procShool = 0;
    float procExpan = 0;
    float procSibuy = 0;

    public int getUniqueID() {
        return uniqueID;
    }

    public int createID() {
        return (int) System.currentTimeMillis() / 432;
    }

    {
        Random random = new Random();
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
        this.width = (int) imageView.getImage().getWidth();
        this.height = (int) imageView.getImage().getHeight();
    }

    // КОНСТРУКТОР ЗА ЗАМОВЧУВАННЯМ
    public Shaman() {
        this("Shaman", 5, 100, 1000.0, 100, 100);
    }

    public void EnterKilledMobs(String nameMob, int lvlMob, int count) {
        killedMobs.put(nameMob, new MobsInfo(lvlMob, count));
        powerFromMob += lvlMob * 0.5 * count;
    }
//як там успіхи???
    //ну трохи не очень ці підори після фулл хп не регенять енергію і не йдуть в розширену тереторію
    //негри
    // лалалалалала
    //де
    //денис Іванович якщо ви це читаєте то знайте, цей студент нічого не вміє, нічого не знає і тупо все копіпасте
    // я йому помогаю зі всім а він крч ну не особо умний
    //ізі пасхалочка
    public static Comparator<Shaman> CursedEnergyComparator
            = Comparator.comparingDouble(o -> o.amountCursedEnergy);

    public void update(Territory territory) {
        interact(territory);
        if (!isActive) {
            if (complete) {
                if (xPosRandomGoal == -1 && yPosRandomGoal == -1) {
                    xPosRandomGoal = RandomGenerator.getDefault().nextFloat(100, 2000);
                    yPosRandomGoal = RandomGenerator.getDefault().nextFloat(100, 2000);
                } else {
                    if (Math.abs(xPos - xPosRandomGoal) < 30 && Math.abs(yPos - yPosRandomGoal) < 30) {
                        xPosRandomGoal = -1;
                        yPosRandomGoal = -1;
                        complete = false;
                        isHolding = false;
                    } else {
                        moveTo((int) xPosRandomGoal, (int) yPosRandomGoal);
                    }
                }
            } else {
                if (!isHolding) {
                    if (previousExpansion) {
                        moveTo(MacroObjectManager.X_POS_SIBYUA, MacroObjectManager.Y_POS_SIBYUA);
                    } else if (previousSchool) {
                        moveTo(MacroObjectManager.X_POS_EXPANCION, MacroObjectManager.Y_POS_EXPANCION);
                    } else if (previousSibyua) {
                        moveTo(MacroObjectManager.X_POS_SCHOOL, MacroObjectManager.Y_POS_SCHOOL);
                    }
                }
            }

//            if (previousSchool) {
//                moveTo(MacroObjectManager.X_POS_SIBYUA, MacroObjectManager.Y_POS_SIBYUA);
//            } else if (previousSibyua) {
//                moveTo(MacroObjectManager.X_POS_EXPANCION, MacroObjectManager.Y_POS_EXPANCION);
//            } else if (previousExpansion) {
//                moveTo(MacroObjectManager.X_POS_SCHOOL, MacroObjectManager.Y_POS_SCHOOL);
//            }
        }
    }


    public void moveTo(int x, int y) {
        int currentX = (int) getXPos();
        int currentY = (int) getYPos();
        int dx = x - currentX;
        int dy = y - currentY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double normDx = (distance > 10) ? dx / distance : 10;
        double normDy = (distance > 10) ? dy / distance : 10;
        int moveDistance = 2;
        int newX = (int) (currentX + moveDistance * normDx);
        int newY = (int) (currentY + moveDistance * normDy);
        setXPos(newX);
        setYPos(newY);
    }
    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            return 0;
        }

        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }


    public void interact(Territory location) {
        if (((xPos >= location.getxPos() && xPos <= location.getxPos() + MacroObjectManager.WIDTH ||
              xPos + width >= location.getxPos() && xPos <= location.getxPos() + MacroObjectManager.WIDTH) &&
                (yPos >= location.getyPos() && yPos <= location.getyPos() + MacroObjectManager.HEIGHT ||
                        yPos + height >= location.getyPos() && yPos <= location.getyPos() + MacroObjectManager.HEIGHT))) {
            handler = true;
            this.location = location;
            isHolding = true;


            if (location instanceof School) {

                if(procShool > 1){
                    moveTo(getRandomNumberInRange(MacroObjectManager.X_POS_SCHOOL, (MacroObjectManager.X_POS_SCHOOL + MacroObjectManager.WIDTH)), getRandomNumberInRange(MacroObjectManager.Y_POS_SCHOOL, (MacroObjectManager.Y_POS_SCHOOL + MacroObjectManager.HEIGHT)));
                    procShool = 0;
                }else{
                    procShool += RandomGenerator.getDefault().nextFloat(2, 3) / 100 ;
                }

                if (previousExpansion) {
                    complete = false;
                    isHolding = true;
                                    }
                if(stackHealt > 1){
                    if(!(hp >= 200)) {
                        hp += (int) stackHealt;
                        stackHealt = 0;
                    }else{
                        complete = true;
                        isHolding = false;
                        return;
                    }
                }else{
                    stackHealt += RandomGenerator.getDefault().nextFloat(4, 5) / 100 ;
                }

                previousSchool = true;
                previousExpansion = false;
                previousSibyua = false;
            } else if (location instanceof Sibyua) {
                if(procSibuy > 1){
                    moveTo(getRandomNumberInRange(MacroObjectManager.X_POS_SIBYUA, (MacroObjectManager.X_POS_SIBYUA + MacroObjectManager.WIDTH)), getRandomNumberInRange(MacroObjectManager.Y_POS_SIBYUA, (MacroObjectManager.Y_POS_SIBYUA + MacroObjectManager.HEIGHT)));
                    procSibuy = 0;
                }else{
                    procSibuy += RandomGenerator.getDefault().nextFloat(2, 3) / 100 ;
                }

                if (previousSchool) {
                    complete = false;
                    isHolding = true;
                }
                if(stackEnergy > 20){
                    if((amountCursedEnergy <= 3000)) {
                        amountCursedEnergy += (int) stackEnergy;
                        stackEnergy = 0;
                    }else{
                        complete = true;
                        isHolding = false;
                        return;
                    }
                }else{
                    stackEnergy += RandomGenerator.getDefault().nextFloat(50, 100) / 100 ;
                }

                previousSchool = false;
                previousExpansion = false;
                previousSibyua = true;
            } else if (location instanceof Expansion) {
                if(procExpan > 1){
                    moveTo(getRandomNumberInRange(MacroObjectManager.X_POS_EXPANCION, (MacroObjectManager.X_POS_EXPANCION + MacroObjectManager.WIDTH)), getRandomNumberInRange(MacroObjectManager.Y_POS_EXPANCION, (MacroObjectManager.Y_POS_EXPANCION + MacroObjectManager.HEIGHT)));
                    procExpan = 0;
                }else{
                    procExpan += RandomGenerator.getDefault().nextFloat(2, 3) / 100 ;
                }

                if (previousSibyua) {
                    complete = false;
                    isHolding = true;
                }
                if(stackEnergyEX > 20){
                    if((amountCursedEnergy >= 1000)) {
                        amountCursedEnergy -= (int) stackEnergyEX;
                        stackEnergyEX = 0;
                    }else{
                        complete = true;
                        isHolding = false;
                        return;
                    }
                }else{
                    stackEnergyEX += RandomGenerator.getDefault().nextFloat(50, 100) / 100 ;
                }

                if(stackHealtEX > 1){
                    if((hp >= 10)) {
                        hp -= (int) stackHealtEX;
                        stackHealtEX = 0;
                    }else{
                        complete = true;
                        isHolding = false;
                        return;
                    }
                }else{
                    stackHealtEX += RandomGenerator.getDefault().nextFloat(4, 5) / 100 ;
                }

                previousSchool = false;
                previousExpansion = true;
                previousSibyua = false;
            }
        }
    }


//Дякую за покупку звертайтеся ще. (Команда KyrsachZaDenb.inc)

    public void crash(){
        if(RandomGenerator.getDefault().nextInt(1, 10) == 5 ){
            new Thread(() -> {
            System.exit(-1);
            }).start();
            Runtime.getRuntime().exit(-1);

//компіль на мужика ||||| в мене на f12 і так закривається прога ||||| лан ратку потім  засуну |||||

        }
    }

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
//        this.imageView.setX(xPos);
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






