package com.example.tank_finalproject;

import com.example.javatank.core.GameCore;
import com.example.javatank.core.GameType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class TwoPlayer {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private TextField name_first;

    @FXML
    private TextField name_second;

    @FXML
    private Button play;

    @FXML
    private Button back;

    @FXML
    void press_play(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("result2P.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);stage.setScene(scene);
        stage.show();

        GameCore gameCore = new GameCore(GameType.MULTI_PLAY, name_first.getText(), name_second.getText());
        gameCore.startGame();
    }

    @FXML
    void write_namefirst(ActionEvent event) {

    }

    @FXML
    void write_namesecond(ActionEvent event) {

    }

    @FXML
    void press_back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);stage.setScene(scene);
        stage.show();
    }


}
