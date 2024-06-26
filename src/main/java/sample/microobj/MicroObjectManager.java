package sample.microobj;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.lab4.Main;
import sample.lab4.ParamController;
import sample.lab4.Paths;
import sample.macroobj.*;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class MicroObjectManager {
    private static MicroObjectManager instance;
    private ArrayList<Shaman> shamans;

    private MicroObjectManager() {
        shamans = new ArrayList<>();
    }

    public ArrayList<Shaman> getShamans() {
        return shamans;
    }

    public void addMicroObject(Shaman microObject) {
        getShamans().add(microObject);
    }

    public static synchronized MicroObjectManager getInstance() {
        if (instance == null) {
            instance = new MicroObjectManager();
        }
        return instance;
    }

    public void removeActiveMicroObjects() {
        shamans.removeIf(Shaman::isActive);
    }
//    private MicroObjectManager() {
//        shamans = new ArrayList<>();
//    }

    public void deactivateAllMicroObjects() {
        for (Shaman o : shamans) {
            if (o.isActive()) {
                o.setActive();
            }
        }
    }



    public void saveShamans() {
        // Створення вікна вибору файлу
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Зберегти шаманів");

        // Отримання вибраного користувачем файлу
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                // Створення потоку для запису даних у файл
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
                // Запис об'єктів шаманів у файл
                outputStream.writeObject(shamans);
                // Закриття потоку
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Функція для завантаження шаманів за допомогою сереалізації
    public void loadShamans() {
        // Створення вікна вибору файлу
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Виберіть файл з шаманами для завантаження");

        // Отримання вибраного користувачем файлу
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                // Створення потоку для читання даних з файлу
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
                // Зчитування об'єктів шаманів з файлу
                shamans = (ArrayList<Shaman>) inputStream.readObject();
                // Закриття потоку
                inputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized ArrayList<Shaman> getArray(){
        return shamans;
    }

    public void printHandledMicroObjects() {
        new Thread(() -> {
            System.out.println("""
                Оберіть мікрооб'єкти які належать до :
                1 - School
                2 - Expansion
                3 - Sibyua
                4 - Жодному""");
            Scanner scanner = new Scanner(System.in);
            try {
                int choice = Main.scanner.nextInt();
                switch (choice) {
                    case 1:
                        for(Shaman o: shamans){
                            if((o.getXPos() >= MacroObjectManager.X_POS_SCHOOL && o.getXPos() <= MacroObjectManager.X_POS_SCHOOL + MacroObjectManager.WIDTH) && (o.getYPos() >= MacroObjectManager.Y_POS_SCHOOL && o.getYPos() <= MacroObjectManager.Y_POS_SCHOOL + MacroObjectManager.HEIGHT)   ){
                                //System.out.println(o);
                                displayInfo(o);
                            }
                        }
                        break;
                    case 2:
                        for(Shaman o: shamans){
                            if((o.getXPos() >= MacroObjectManager.X_POS_EXPANCION && o.getXPos() <= MacroObjectManager.X_POS_EXPANCION  + MacroObjectManager.WIDTH) && (o.getYPos() >= MacroObjectManager.Y_POS_EXPANCION && o.getYPos() <= MacroObjectManager.Y_POS_EXPANCION + MacroObjectManager.HEIGHT)   ){
                                //System.out.println(o);
                                displayInfo(o);
                            }
                        }
                        break;
                    case 3:
                        for(Shaman o: shamans){
                            if((o.getXPos() >= MacroObjectManager.X_POS_SIBYUA && o.getXPos() <= MacroObjectManager.X_POS_SIBYUA + MacroObjectManager.WIDTH) && (o.getYPos() >= MacroObjectManager.Y_POS_SIBYUA && o.getYPos() <= MacroObjectManager.Y_POS_SIBYUA + MacroObjectManager.HEIGHT)   ){
                                //System.out.println(o);
                                displayInfo(o);
                            }
                        }
                        break;
                    case 4:
                        for (Shaman o : shamans) {
                            if (!((o.getXPos() >= 70 && o.getXPos() <= 470) && (o.getYPos() >= 50 && o.getYPos() <= 350)) &&
                                    !((o.getXPos() >= 1100 && o.getXPos() <= 1500) && (o.getYPos() >= 50 && o.getYPos() <= 350)) &&
                                    !((o.getXPos() >= 600 && o.getXPos() <= 1000) && (o.getYPos() >= 450 && o.getYPos() <= 750))) {
                                System.out.println(o);
                            }
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid choice");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }).start();
    }

    public void basicContstr() {
        Shaman o = new Shaman();
        shamans.add(o);
    }

    public void deepCopy() {
        if(getOnlyActiveShaman() != null) {
            Shaman copiedShaman = null;
            try {
                copiedShaman = (Shaman) getOnlyActiveShaman().clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            copiedShaman.setxPos(20);
            copiedShaman.setYPos(20);
            shamans.add(copiedShaman);
        }
    }


    public void swichToRedBack() {
//        Shaman activeShaman = getOnlyActiveShaman();
        if(getOnlyActiveShaman() != null){
            if(getOnlyActiveShaman().getClass() == Shaman.class){
                getOnlyActiveShaman().setGradeImageView(new ImageView(Objects.requireNonNull(getClass().getResource(Paths.grade2PathRed)).toExternalForm()));
            } else if(getOnlyActiveShaman().getClass() == ShamanGrade1.class){
                getOnlyActiveShaman().setGradeImageView(new ImageView(Objects.requireNonNull(getClass().getResource(Paths.grade1PathRed)).toExternalForm()));
            } else if(getOnlyActiveShaman().getClass() == ShamanGradeHigh.class){
                getOnlyActiveShaman().setGradeImageView(new ImageView(Objects.requireNonNull(getClass().getResource(Paths.gradeHighPathRed)).toExternalForm()));
            }
        }
    }

    public void swichToWhiteBack() {
//        Shaman activeShaman = getOnlyActiveShaman();
        if(getOnlyActiveShaman() != null){
            if(getOnlyActiveShaman().getClass() == Shaman.class){
                getOnlyActiveShaman().setGradeImageView(new ImageView(Objects.requireNonNull(getClass().getResource(Paths.grade2Path)).toExternalForm()));
            } else if(getOnlyActiveShaman().getClass() == ShamanGrade1.class){
                getOnlyActiveShaman().setGradeImageView(new ImageView(Objects.requireNonNull(getClass().getResource(Paths.grade1Path)).toExternalForm()));
            } else if(getOnlyActiveShaman().getClass() == ShamanGradeHigh.class){
                getOnlyActiveShaman().setGradeImageView(new ImageView(Objects.requireNonNull(getClass().getResource(Paths.gradeHighPath)).toExternalForm()));
            }
        }
    }



    public void searchMicroObject() {
        new Thread(() -> {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Введіть параметри об'єкта:");
                System.out.print("Name: ");
                String name = scanner.nextLine();
                System.out.print("isActive: (true/false) ");
                boolean isActive = Boolean.parseBoolean(scanner.nextLine());
                System.out.print("hp: ");
                int hitpoint = Integer.parseInt(scanner.nextLine());
                System.out.print("CE: ");
                double CE = Double.parseDouble(scanner.nextLine());
                boolean found = false;

                for (Shaman microObject : MicroObjectManager.getInstance().getShamans()) {
                    if (microObject.getName().equals(name) && microObject.isActive() == isActive && microObject.getHp() == hitpoint && microObject.getAmountCursedEnergy() == CE) {
                        found = true;
                        System.out.println("Знайдено:");
                        System.out.println("X: " + microObject.getXPos() + ", Y:" + microObject.getYPos());

                        if((microObject.getXPos() >= MacroObjectManager.X_POS_SCHOOL && microObject.getXPos() <= MacroObjectManager.X_POS_SCHOOL + MacroObjectManager.WIDTH) && (microObject.getYPos() >= MacroObjectManager.Y_POS_SCHOOL && microObject.getYPos() <= MacroObjectManager.Y_POS_SCHOOL + MacroObjectManager.HEIGHT)   ){
                            System.out.println("Перебуває в школі");
                            break;
                        } else if((microObject.getXPos() >= MacroObjectManager.X_POS_EXPANCION && microObject.getXPos() <= MacroObjectManager.X_POS_EXPANCION  + MacroObjectManager.WIDTH) && (microObject.getYPos() >= MacroObjectManager.Y_POS_EXPANCION && microObject.getYPos() <= MacroObjectManager.Y_POS_EXPANCION + MacroObjectManager.HEIGHT)   ){
                            System.out.println("Перебуває в розширеній тереторії");
                            break;
                        } else if((microObject.getXPos() >= MacroObjectManager.X_POS_SIBYUA && microObject.getXPos() <= MacroObjectManager.X_POS_SIBYUA + MacroObjectManager.WIDTH) && (microObject.getYPos() >= MacroObjectManager.Y_POS_SIBYUA && microObject.getYPos() <= MacroObjectManager.Y_POS_SIBYUA + MacroObjectManager.HEIGHT)   ){
                            System.out.println("Перебуває в станції сібуя");
                            break;
                        } else if (!((microObject.getXPos() >= 70 && microObject.getXPos() <= 470) && (microObject.getYPos() >= 50 && microObject.getYPos() <= 350)) &&
                                !((microObject.getXPos() >= 1100 && microObject.getXPos() <= 1500) && (microObject.getYPos() >= 50 && microObject.getYPos() <= 350)) &&
                                !((microObject.getXPos() >= 600 && microObject.getXPos() <= 1000) && (microObject.getYPos() >= 450 && microObject.getYPos() <= 750))) {
                            System.out.println("Не перебуває в жодному мікрооб'єкті");
                        }
                    }
                }
                if (!found) {
                    System.out.println("Мікрооб'єкт не знайдено");
                }
            }catch (IllegalArgumentException e){
                System.out.println("Error");
            }
        }).start();
    }

    public Shaman getOnlyActiveShaman() {
        Shaman activeShaman = null;
        int activeCount = 0;

        for (Shaman shaman : shamans) {
            if (shaman.isActive()) {
                activeShaman = shaman;
                activeCount++;
            }
            if (activeCount > 1) {
                return null; // More than one active Shaman, return null
            }
        }

        return activeShaman; // Return the only active Shaman or null if none
    }

    public void printRequest() {
        int counterActive = 0;
        int counterMana = 0;
        int counterLeft = 0;

        for(Shaman o : getArray()){
            if(o.isActive()){
                counterActive++;
            }
            if(o.getAmountCursedEnergy() > 4750){
                counterMana++;
            }
            if(o.getXPos() < 1000){
                counterLeft++;
            }
        }
        System.out.println("Кількість активних: " + counterActive);
        System.out.println("Проклята енергія яких більша за 4750: " + counterMana);
        System.out.println("Кількість по ліву сторону: " + counterLeft);
    }

    public void sortMicroObjects() {
        System.out.println("Enter sorting param");
        Scanner scanner = new Scanner(System.in);
        new Thread(() ->{
            System.out.println("1 - Сортувати за кількістю проклятої енергії");
            System.out.println("2 - Сортувати за іменем");
            System.out.println("3 - Сортувати за кількістю здоров'я");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    shamans.sort(Comparator.comparingDouble(Shaman::getAmountCursedEnergy));
                    System.out.println(shamans);
                    break;
                case "2":
                    shamans.sort(Comparator.comparing(Shaman::getName));
                    System.out.println(shamans);
                    break;
                case "3":
                    shamans.sort(Comparator.comparing(Shaman::getHp));
                    System.out.println(shamans);
                    break;

            }
        }).start();

    }

    public Shaman getShamanHoldingPos(int x, int y) {
        for(Shaman shaman : getArray()){
            if (x >= shaman.getX() && x <= shaman.getX() + shaman.getWidth() &&
                    y >= shaman.getY() && y <= shaman.getY() + shaman.getHeight()) {
                return shaman;
            }
        }
        return null;
    }

    public void moveSimpleShaman(int x, int y, Shaman shaman) {
        if (shaman != null) {
            if(shaman.isActive()){
                shaman.setXPos(x);
                shaman.setYPos(y);
            }
        }
    }


    public void moveActiveMicroObjects(KeyCode keyCode) {
        for (Shaman o: shamans) {
            if (o.isActive()) {
                handleButton(o, keyCode);
            }
        }
    }

    private void handleButton(Object object, KeyCode keyCode){
        if(object instanceof Shaman){
            switch (keyCode) {
                case UP: {
                    ((Shaman) object).setYPos(((Shaman) object).getY() - 25);
                    break;
                }
                case LEFT: {
                    ((Shaman) object).setXPos(((Shaman) object).getX() - 25);
                    break;
                }
                case DOWN:{
                    ((Shaman) object).setYPos(((Shaman) object).getY() + 25);
                    break;
                }
                case RIGHT:{
                    ((Shaman) object).setXPos(((Shaman) object).getX() + 25);
                    break;
                }
            }

            if (object.getClass() == ShamanGrade1.class) {
                switch (keyCode) {
                    case W: {
                        ((Shaman) object).setYPos(((Shaman) object).getY() - 25);
                        break;
                    }
                    case A: {
                        ((Shaman) object).setXPos(((Shaman) object).getX() - 25);
                        break;
                    }
                    case S:{
                        ((Shaman) object).setYPos(((Shaman) object).getY() + 25);
                        break;
                    }
                    case D:{
                        ((Shaman) object).setXPos(((Shaman) object).getX() + 25);
                        break;
                    }
                }
            } else if (object.getClass() == Shaman.class){
                switch (keyCode) {
                    case W: {
                        ((Shaman) object).setYPos(((Shaman) object).getY() - 20);
                        break;
                    }
                    case A: {
                        ((Shaman) object).setXPos(((Shaman) object).getX() - 20);
                        break;
                    }
                    case S:{
                        ((Shaman) object).setYPos(((Shaman) object).getY() + 20);
                        break;
                    }
                    case D:{
                        ((Shaman) object).setXPos(((Shaman) object).getX() + 20);
                        break;
                    }
                }
            } else if (object.getClass() == ShamanGradeHigh.class){
                switch (keyCode) {
                    case W: {
                        ((Shaman) object).setYPos(((Shaman) object).getY() - 30);
                        break;
                    }
                    case A: {
                        ((Shaman) object).setXPos(((Shaman) object).getX() - 30);
                        break;
                    }
                    case S:{
                        ((Shaman) object).setYPos(((Shaman) object).getY() + 30);
                        break;
                    }
                    case D:{
                        ((Shaman) object).setXPos(((Shaman) object).getX() + 30);
                        break;
                    }
                }
            }
        }
    }

    public void changeActiveParam() {
        int counter = 0;
        for (Shaman shaman : shamans) {
            if (shaman.isActive()) {
                counter++;
                if (counter < 2) {
                    System.out.println(shaman);
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("changeParam.fxml"));//"D:\Projects\java\oop\lab4\src\main\resources\sample\lab4\changeParam.fxml" // "/resources/sample.lab4/changeParam.fxml" //changeParam.fxml
                        Parent root = loader.load();

                        Scene scene = new Scene(root);

                        ParamController controller = loader.getController();

                        controller.setShaman(shaman);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setTitle("Change Knight Details");
                        stage.show();
                        stage.setResizable(false);
                        controller.setStage(stage);
                    } catch (IOException e) {
                        System.out.println("Error loading changeParam.fxml");
                    }
                }
            }
        }
    }





    public void activateMicroObject(double clickX, double clickY) {
        for (Shaman o : shamans) {
            if (clickX >= o.getX() && clickX <= o.getX() + o.getWidth() &&
                    clickY >= o.getY() && clickY <= o.getY() + o.getHeight()) {
                o.setActive();
            }
        }
    }

    public void drawAllMicroObjects(GraphicsContext gc) {
        for (Shaman o : shamans) {
            o.draw(gc);
        }
    }

    public void printActiveMicroObjects() {
        System.out.println("Active MicroObjects: ");
        for(Shaman o : shamans){
            System.out.println(o);
        }
    }

    public void fastCreateMicroObjectGR1() {
        ShamanGrade1 Biden = new ShamanGrade1("Biden", 1, 100, 20000, 300, 300);
        Biden.EnterKilledMobs("Osama Ben Laden", 9, 1);
        Biden.EnterKilledMobs("Poor children in Africa", 1, 100000);

        shamans.add(Biden);
    }

    public void fastCreateMicroObjectGR_HIGH() {
        ShamanGradeHigh PesPatron = new ShamanGradeHigh("Pes Patron", 1, 100, 1000, 400, 400);
        PesPatron.EnterKilledMobs("Osama Ben Laden", 9, 1);
        PesPatron.EnterKilledMobs("Poor children in Africa", 1, 100000);

        shamans.add(PesPatron);
    }


    public void fastCreateMicroObjectGR2() {
        Shaman Poroshenko = new Shaman("Poroshenko", 1, 100, 2019, 200, 200);
        Poroshenko.EnterKilledMobs("Fans of the Yanukovich", 4, 200);
        Poroshenko.EnterKilledMobs("Business competitors", 8, 10);
        Poroshenko.EnterKilledMobs("Political competitors", 9, 150);


        shamans.add(Poroshenko);
    }

        public void createMicroObjectDialog() {
        Dialog<Object> dialog = new Dialog<>();
        dialog.setTitle("Створення мікрооб'єкту");
        dialog.setHeaderText("Введіть деталі мікрооб'єкта");
        //(String Name, int Lvl, int healthpoint, double amountCursedEnergy, float xPos, float yPos)

        TextField nameField = new TextField();
        TextField lvlField = new TextField();
        TextField healthpointField = new TextField();
        TextField amountCursedEnergyField = new TextField();
        CheckBox createDefShaman = new CheckBox("Створити дефолтного мага");
        TextField xPosField = new TextField();
        TextField yPosField = new TextField();
        RadioButton radioButton1 = new RadioButton("ShamanGR2");
        RadioButton radioButton2 = new RadioButton("ShamanGR1");
        RadioButton radioButton3 = new RadioButton("ShamanGR-HIGH");

        nameField.setPromptText("Ім'я(String)");
        lvlField.setPromptText("Рівень(int)");
        healthpointField.setPromptText("Здоров'я(int)");
        amountCursedEnergyField.setPromptText("Проклята енергія(double)");
        xPosField.setPromptText("Позиція X(float)");
        yPosField.setPromptText("Позиція У(float)");


        GridPane grid = new GridPane();
        grid.add(new Label("Ім'я:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Рівень:"), 0, 1);
        grid.add(lvlField, 1, 1);
        grid.add(new Label("Здоров'я:"), 0, 2);
        grid.add(healthpointField, 1, 2);
        grid.add(new Label("Проклята енергія:"), 0, 3);
        grid.add(amountCursedEnergyField, 1, 3);

        grid.add(createDefShaman, 0, 4);
        grid.add(radioButton1, 0, 5);
        grid.add(radioButton2, 1, 5);
        grid.add(radioButton3, 2, 5);
        grid.add(new Label("X Position:"), 0, 6);
        grid.add(xPosField, 1, 6);
        grid.add(new Label("Y Position:"), 0, 7);
        grid.add(yPosField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    String name = nameField.getText();
                    int lvl = Integer.parseInt(lvlField.getText());
                    int healthpoint = Integer.parseInt(healthpointField.getText());
                    double amountCursedEnergy = amountCursedEnergyField.getText().isEmpty() ? 0 : Double.parseDouble(amountCursedEnergyField.getText());
                    float xPos = Float.parseFloat(xPosField.getText());
                    float yPos = Float.parseFloat(yPosField.getText());


                    //(String Name, int Lvl, int healthpoint, double amountCursedEnergy, float xPos, float yPos)
                    if(createDefShaman.isSelected()) {
                        Shaman shaman = new Shaman();
                        shamans.add(shaman);
                        //System.out.println("Мікрочел створений");
                        displayInfo("Мікрочел створений");
                        return shaman;
                    } else if (radioButton1.isSelected()) {
                        Shaman shaman = new Shaman(name, lvl, healthpoint, amountCursedEnergy, xPos, yPos);
                        shamans.add(shaman);
                        return shaman;
                    } else if (radioButton2.isSelected()) {
                        ShamanGrade1 shamangr1 = new ShamanGrade1(name, lvl, healthpoint, amountCursedEnergy, xPos, yPos);
                        shamans.add(shamangr1);
                        return shamangr1;
                    } else if (radioButton3.isSelected()) {
                        ShamanGradeHigh shamangrhigh = new ShamanGradeHigh(name, lvl, healthpoint, amountCursedEnergy, xPos, yPos);
                        shamans.add(shamangrhigh);
                        return shamangrhigh;
                    }

                } catch (NumberFormatException e) {
                    if(createDefShaman.isSelected()) {
                        Shaman shaman = new Shaman();
                        shamans.add(shaman);
                        System.out.println("Мікрочел створений");
                        return shaman;
                    }
                    System.out.println("Invalid input. Default preset");
                }
            }

            if(radioButton1.isSelected()){
                Shaman shaman = new Shaman();
                shamans.add(shaman);
                return shaman;
            }

            if(radioButton2.isSelected()){
                ShamanGrade1 shamangr1 = new ShamanGrade1();
                shamans.add(shamangr1);
                return shamangr1;
            }
            if(radioButton3.isSelected()){
                ShamanGradeHigh wizard = new ShamanGradeHigh();
                shamans.add(wizard);
                return wizard;
            }
            return new Shaman();
        });

        Optional<Object> result = dialog.showAndWait();

        result.ifPresent(microObject -> System.out.println("Created"));
    }

    public void updateAllMicroObjects(){
        for(Territory territory : MacroObjectManager.getMacroObjects()) {
            for (Shaman shaman : getArray()) {
                shaman.update(territory);
            }
        }
    }


    public static void displayInfo(String message) {
        System.out.println("Повідомлення: " + message);
    }


    public static void displayInfo(int number) {
        System.out.println("Число: " + number);
    }


    public static void displayInfo(double value) {
        System.out.println("Значення: " + value);
    }

    public static void displayInfo(Shaman shaman) {
        System.out.println("Значення: " + shaman);
    }
}
