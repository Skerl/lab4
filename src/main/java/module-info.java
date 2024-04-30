module sample.lab4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens sample.lab4 to javafx.fxml;
    exports sample.lab4;
}