package sample.lab4;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;

import sample.microobj.*;
import sample.macroobj.*;

public class Main extends Application {
    public static final Scanner scanner;
    static {
        scanner = new Scanner(System.in);
    }


    public static final int WINDOW_WIDTH = 1520; // Ширина вікна
    public static final int WINDOW_HEIGHT = 780; // Висота вікна


    private MicroObjectManager microObjectManager;
    private MacroObjectManager macroObjectManager;
    private Canvas canvas;
    private Canvas miniMap;
    public final static int FPS = 360;
    private double initialOffsetX;
    private double initialOffsetY;
    private static boolean HOLD = false;

    @Override
    public void start(Stage primaryStage) {

        ScrollPane mainScrollPane = new ScrollPane();
        AnchorPane mainPane = new AnchorPane();
        mainPane.setPrefSize(2000, 2000);

        mainScrollPane.setContent(mainPane);

        StackPane stackPane = new StackPane(mainScrollPane);

        Scene mainScene = new Scene(stackPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Jujutsu Kaisen");
        primaryStage.show();

        canvas = new Canvas(mainPane.getWidth(), mainPane.getHeight());
        miniMap = new Canvas(200, 200);
        Rectangle miniMapBorder = new Rectangle(210, 210, Color.TRANSPARENT);
        miniMapBorder.setStroke(Color.BLACK);

        mainPane.getChildren().add(canvas);
        stackPane.getChildren().addAll(miniMapBorder, miniMap);
        StackPane.setAlignment(miniMapBorder, Pos.TOP_RIGHT);
        StackPane.setMargin(miniMapBorder, new Insets(10));
        StackPane.setAlignment(miniMap, Pos.TOP_RIGHT);
        StackPane.setMargin(miniMap, new Insets(15));

        microObjectManager = MicroObjectManager.getInstance();
        //microObjectManager.createMicroObjectDialog();
        macroObjectManager = MacroObjectManager.getInstance();

        mainScene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case Q:
                    microObjectManager.swichToWhiteBack(); // поміняти колір іконки рангу на червоний
                    break;
                case TAB:
                    microObjectManager.swichToRedBack(); // поміняти колір іконки рангу на червоний
                    break;
                case F1:
                    microObjectManager.fastCreateMicroObjectGR2(); // швидке створення мікрочела початкового рівця
                    break;
                case F2:
                    microObjectManager.fastCreateMicroObjectGR1(); // швидке створення мікрочела середнього рівня
                    break;
                case F3:
                    microObjectManager.fastCreateMicroObjectGR_HIGH(); // швидке створення мікрочела максимального рівня
                    break;
                case INSERT:
                    microObjectManager.createMicroObjectDialog(); // меню створення мікрочела
                    break;
                case ESCAPE:
                    microObjectManager.deactivateAllMicroObjects(); // деактивувати активні мікрооб'єкти
                    break;
                case DELETE:
                    microObjectManager.removeActiveMicroObjects(); // видалити мікрочелів
                    break;
                case F5:
                    microObjectManager.changeActiveParam(); // редагувати мікрочелів
                    break;
                case Z:
                    microObjectManager.printActiveMicroObjects(); // вивести всі мікрооб'єкти в консоль
                    break;
                case I:
                    microObjectManager.saveShamans(); // зберегтись
                    break;
                case O:
                    microObjectManager.loadShamans(); // відкрити сейв
                    break;
                case X:
                    microObjectManager.printRequest(); // вевести запит, об'єкти які мають більшо 50Хп, мають більше 4750 енергії...
                    break;
                case C:
                    microObjectManager.sortMicroObjects(); // сортування за певним параметром
                    break;
                case V:
                    microObjectManager.printHandledMicroObjects(); // пошук об'єктів які належать до макрооб'єктів
                    break;
                case B:
                    microObjectManager.searchMicroObject(); // пошук мікрочелів по параметрам
                    break;
                case N:
                    microObjectManager.deepCopy(); // глибоке копіювання
                    break;
                case F12:
                    System.exit(0); // завершити роботу програми

            }
            microObjectManager.moveActiveMicroObjects(event.getCode());
        });


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.0 / FPS), event -> {
            render(mainScrollPane);
            update();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        addPressAndHoldHandler(mainPane, event -> {
            double xPos = event.getX();
            double yPos = event.getY();
            Shaman shaman = MicroObjectManager.getInstance().getShamanHoldingPos((int) xPos, (int) yPos);

            if (shaman != null) {
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    initialOffsetX = xPos - shaman.getXPos();
                    initialOffsetY = yPos - shaman.getYPos();
                    HOLD = true;
                } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && HOLD) {
                    double newX = xPos - initialOffsetX;
                    double newY = yPos - initialOffsetY;
                    MicroObjectManager.getInstance().moveSimpleShaman((int) newX, (int) newY, shaman);
                } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    HOLD = false;
                }
            }
        });

        canvas.setOnMouseClicked(event -> {
            if (!HOLD) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    microObjectManager.activateMicroObject(event.getX(), event.getY());
                }
            }
        });

        miniMap.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double xRatio = event.getX() / miniMap.getWidth();
                double yRatio = event.getY() / miniMap.getHeight();
                mainScrollPane.setHvalue(xRatio * (mainScrollPane.getContent().getBoundsInLocal().getWidth() - mainScrollPane.getViewportBounds().getWidth()) / mainScrollPane.getContent().getBoundsInLocal().getWidth());
                mainScrollPane.setVvalue(yRatio * (mainScrollPane.getContent().getBoundsInLocal().getHeight() - mainScrollPane.getViewportBounds().getHeight()) / mainScrollPane.getContent().getBoundsInLocal().getHeight());
            }
        });
    }



    public static void main(String[] args) {
        launch();
    }

//    private void handleKeyInput(KeyCode keyCode) {
//        microObjectManager.moveActiveMicroObjects(keyCode);
//    }

    private void update() {
        microObjectManager.updateAllMicroObjects();
    }


    private void render(ScrollPane scrollPane) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        GraphicsContext miniMapGc = miniMap.getGraphicsContext2D();
        miniMapGc.clearRect(0, 0, miniMap.getWidth(), miniMap.getHeight());
        miniMapGc.setFill(Color.WHITE); // Set the background color of the minimap
        miniMapGc.fillRect(0, 0, miniMap.getWidth(), miniMap.getHeight()); // Fill the background

        double miniMapScaleX = miniMap.getWidth() / ((AnchorPane) canvas.getParent()).getWidth();
        double miniMapScaleY = miniMap.getHeight() / ((AnchorPane) canvas.getParent()).getHeight();
        miniMapGc.save();
        miniMapGc.scale(miniMapScaleX, miniMapScaleY);

        // Draw macro objects first
        macroObjectManager.drawAllMacroObjects(miniMapGc);
        // Draw micro objects on top
        microObjectManager.drawAllMicroObjects(miniMapGc);

        miniMapGc.restore();

        // Draw macro objects on main canvas
        macroObjectManager.drawAllMacroObjects(gc);
        // Draw micro objects on main canvas
        microObjectManager.drawAllMicroObjects(gc);

        double viewportX = scrollPane.getHvalue() * scrollPane.getContent().getLayoutBounds().getWidth() * miniMapScaleX;
        double viewportY = scrollPane.getVvalue() * scrollPane.getContent().getLayoutBounds().getHeight() * miniMapScaleY;
        double viewportWidth = scrollPane.getViewportBounds().getWidth() * miniMapScaleX;
        double viewportHeight = scrollPane.getViewportBounds().getHeight() * miniMapScaleY;
        miniMapGc.setStroke(Color.RED);
        miniMapGc.strokeRect(viewportX, viewportY, viewportWidth, viewportHeight);
    }


    private void addPressAndHoldHandler(Node node, EventHandler<MouseEvent> handler) {
        class Wrapper<T> { T content; }
        Wrapper<MouseEvent> eventWrapper = new Wrapper<>();

        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            eventWrapper.content = event;
            handler.handle(event);
        });
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            eventWrapper.content = event;
            handler.handle(event);
        });
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            eventWrapper.content = event;
            handler.handle(event);
        });
    }
}