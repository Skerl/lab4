package sample.lab4;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.microobj.Shaman;
import sample.microobj.MicroObjectManager;

public class ParamController {

    public Button okButton;

    public Button cancelButton;

    private Stage stage;

    private Shaman shaman;
    @FXML
    private ImageView imageView;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField ceInput;

    @FXML
    private TextField hpinput;

    @FXML
    private TextField lvlinput;

    @FXML
    private TextField xPosInput;

    @FXML
    private TextField yPosInput;
    @FXML
    void initialize() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void closeStage(){
        stage.close();
    }

    public void setShaman(Shaman shaman) {
        this.shaman = shaman;
        nameInput.setText(shaman.getName());
        ceInput.setText(String.valueOf(shaman.getAmountCursedEnergy()));
        hpinput.setText(String.valueOf(shaman.getHp()));
        lvlinput.setText(String.valueOf(shaman.getLvl()));
        xPosInput.setText(String.valueOf(shaman.getXPos()));
        yPosInput.setText(String.valueOf(shaman.getYPos()));
    }

    public ParamController(){
    }


    public void okButtonClicked() {
        try {
            shaman.setXPos(Float.parseFloat(xPosInput.getText()));
            shaman.setYPos(Float.parseFloat(yPosInput.getText()));
            shaman.setAmountCursedEnergy(Double.parseDouble(ceInput.getText()));
            shaman.setHp(Integer.parseInt(hpinput.getText()));
            shaman.setLvl(Integer.parseInt(lvlinput.getText()));
            shaman.setName(nameInput.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid input");
            alert.setContentText("Please enter a valid number");
            alert.showAndWait();
        }
        closeStage();
    }

    public void cancel() {
        closeStage();
    }
}