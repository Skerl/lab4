package sample.microobj;

import sample.lab4.Main;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Shaman implements Comparable<Shaman>, Cloneable {
    private String name; // Ім'я
    private int hp; // здоров'я
    private int lvl; // Рівень //-
    private double powerFromMob; // Потужність з мобів, розраховується з //-
    private double amountCursedEnergy; // Кіл проклятої енергії
    private Map<String, MobsInfo> killedMobs = new HashMap<>(); // Вбиті прокляття
    private static double forceЕhreshold; // Поріг сили //-
    private float xPos;
    private float yPos;
    //----------------------------------------------------------------------------------------

    static {
        //System.out.println("Статичний блок ініціалізації Shaman. поріг сили");
        forceЕhreshold = 500.0;
    }

    public static double getForceЕhreshold() {
        return forceЕhreshold;
    }

    public static void setForceЕhreshold(double forceЕhreshold) {
        Shaman.forceЕhreshold = forceЕhreshold;
    }

    public  boolean hasEnoughPower() {
        if(powerFromMob > forceЕhreshold) {
            return true;
        }
        return false;
    }
    //    ----------------------------------------------------------------------------------------
    public Shaman(String Name, int Lvl, int healthpoint, double amountCursedEnergy) {
        this.name = Name;
        this.lvl = Lvl;
        this.hp = healthpoint;
        this.amountCursedEnergy = amountCursedEnergy;
    }



    public Shaman() {
        this("Shaman", 5, 100, 1000.0);
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

    class MobsInfo implements Cloneable{
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

    public void setName(String name) {
        this.name = name;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

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



    //--------------------------------------------------------------------------

    public void resetKilledMobs(Map<String, MobsInfo> killedMobs) {
        killedMobs.clear();
    }

    //--------------------------------------------------------------------------



    public Map<String, MobsInfo> getKilledMobs() {
        return killedMobs;
    }

//----------------------------------------------------------------------------------------

    public void transferEnergyTo(Shaman targetShaman, double amount) {
        if (this.amountCursedEnergy >= amount) {
            this.amountCursedEnergy -= amount;
            targetShaman.amountCursedEnergy += amount;
            System.out.println(amount + " amountCursedEnergy перенесено з " + this.name + " до " + targetShaman.name);
        } else {
            System.out.println("У " + this.name + " недостатньо amountCursedEnergy для передачі.");
        }
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

    @Override
    public String toString() {
        return  "name = " + name +
                "; lvl = " + lvl +
                "; powerFromMob = " + powerFromMob +
                "; amountCursedEnergy = " + amountCursedEnergy +
                ";\nkilledMobs = " + killedMobs;
    }


    public static Shaman addNewObject() {
        System.out.println("Введіть спосіб створення об'єкту:");
        System.out.println("1. Автоматично");
        System.out.println("2. Вручну");

        int choice = Main.scanner.nextInt();
        if (choice == 1) {
            return new Shaman();
        } else if (choice == 2) {
            System.out.println("Ім'я:");
            String name = Main.scanner.next();
            Main.scanner.nextLine();

            System.out.println("Рівень:");
            int lvl = Main.scanner.nextInt();
            Main.scanner.nextLine();

            System.out.println("Здоров'я:");
            int hp = Main.scanner.nextInt();
            Main.scanner.nextLine();

            System.out.println("Проклята енергія:");
            double amountCursedEnergy = Main.scanner.nextDouble();
            Main.scanner.nextLine();

            Shaman newShaman = new Shaman(name, lvl, hp, amountCursedEnergy);

            System.out.println("Заповніть інформацію про мобів [Ім'я, рівень, кількість]. -1 означає вихід.");
            while(true) {
                String nameMob = Main.scanner.nextLine();
                if(nameMob.equals("-1")) {
                    break;
                }
                int lvlMob = Main.scanner.nextInt();
                Main.scanner.nextLine();
                int countMob = Main.scanner.nextInt();
                Main.scanner.nextLine();

                newShaman.EnterKilledMobs(nameMob, lvlMob, countMob);
            }

            return newShaman;
        } else {
            return new Shaman();
        }
    }



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
}
