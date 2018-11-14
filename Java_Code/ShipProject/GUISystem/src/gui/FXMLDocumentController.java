/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import InputController.InputController;
import eu.hansolo.medusa.Gauge;
import guisystem.Datahandler;
import guisystem.GUISystem;

import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.gluonhq.charm.glisten.control.ProgressBar;

/**
 *
 * @author Haakon
 */
public class FXMLDocumentController implements Initializable
{

    @FXML
    Button btn_main, btn_dp, btn_alarms;

    @FXML
    ProgressBar throttlePS;

    @FXML
    Gauge speedps;

//    Datahandler dh = GUISystem.dh;
//    InputController ic = GUISystem.inputController;
    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        System.out.println("You clicked me!");
        btn_main.setText("pressed");
        //throttlePS.setProgress(0.6);
        //speedps.setValue(0);

    }

    @FXML
    private void enterHoverButtonAction(MouseEvent event)
    {
        String button = event.getSource().toString();
        if (button.toLowerCase().contains("btn_main"))
        {
            btn_main.setStyle("-fx-background-color: #3d4b5b;");
        }
        if (button.toLowerCase().contains("btn_dp"))
        {
            btn_dp.setStyle("-fx-background-color: #3d4b5b;");
        }
        if (button.toLowerCase().contains("btn_alarms"))
        {
            btn_alarms.setStyle("-fx-background-color: #3d4b5b;");
        }
    }

    @FXML
    private void exitHoverButtonAction(MouseEvent event)
    {
        String button = event.getSource().toString();
        if (button.toLowerCase().contains("btn_main"))
        {
            btn_main.setStyle("-fx-background-color: #364250;");
        }
        if (button.toLowerCase().contains("btn_dp"))
        {
            btn_dp.setStyle("-fx-background-color: #364250;");
        }
        if (button.toLowerCase().contains("btn_alarms"))
        {
            btn_alarms.setStyle("-fx-background-color: #364250;");
        }
    }

    public void updatePage()
    {
        System.out.println("updated");
        throttlePS.setProgress(0.6);
        //btn_main.setText("pressed");
        speedps.setValue(80);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //setController(this);
    }

}
