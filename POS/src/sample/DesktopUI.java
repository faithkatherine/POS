package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.json.JSONObject;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DesktopUI implements Initializable {
    @FXML
    private Button btn_sales;

    @FXML
    private Button btn_purchases;

    @FXML
    private Button btn_items;

    @FXML
    private Button btn_stock;

    @FXML
    private Button btn_dashboard;

    @FXML
    private Button btn_log;

    @FXML
    private BorderPane mainPane;

    public void btnBarchartAction(ActionEvent actionEvent) {
    }

    public void btnProceedAction(ActionEvent actionEvent) {
    }

    public void loadAgain(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void handleClicks(ActionEvent event){
        FxmlLoader object = new FxmlLoader();
         Pane view = object.getPage("sales");
         mainPane.setCenter(view);
    }

    public void handleClicks2(ActionEvent event){
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("purchases");
        mainPane.setCenter(view);
    }

    public void handleClicks3(ActionEvent event){
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("items");
        mainPane.setCenter(view);
    }

    public void handleClicks4(ActionEvent event){
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("stock");
        mainPane.setCenter(view);
    }

    public void handleClicks5(ActionEvent event){
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("dashboard");
        mainPane.setCenter(view);
    }


}
