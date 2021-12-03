/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Manuel
 */
public class MenuController implements Initializable {
    
    @FXML
    private MenuBar menubar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void showPersonPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/hr/algebra/view/People.fxml"));
        Stage stage = (Stage) menubar.getScene().getWindow();
        stage.setTitle("Person Manager");
        stage.setScene(new Scene(root, 600, 420));
    }

    @FXML
    private void showHotelsPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/hr/algebra/view/Hotels.fxml"));
        Stage stage = (Stage) menubar.getScene().getWindow();
        stage.setTitle("Hotel Manager");
        stage.setScene(new Scene(root, 600, 420));
    }

    @FXML
    private void showReservationsPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/hr/algebra/view/Reservations.fxml"));
        Stage stage = (Stage) menubar.getScene().getWindow();
        stage.setTitle("Reservation Manager");
        stage.setScene(new Scene(root, 600, 420));
    }
    
}
