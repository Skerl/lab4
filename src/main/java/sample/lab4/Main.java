package sample.lab4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.ls.LSOutput;
import sample.microobj.Shaman;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {
    public static final Scanner scanner;
    static {
        scanner = new Scanner(System.in);
    }


    public static final int WINDOW_WIDTH = 1920; // Ширина вікна
    public static final int WINDOW_HEIGHT = 950; // Висота вікна

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Shaman nowik = new Shaman();
        System.out.println(nowik);
        launch();
    }

}