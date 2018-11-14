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
import eu.hansolo.tilesfx.Tile;

/**
 *
 * @author Haakon
 */
public class FXMLDocumentController implements Initializable
{

    @FXML
    Button btn_main, btn_dp, btn_alarms;

    @FXML
    ProgressBar throttleps, throttlesb;

    @FXML
    Gauge speedps, speedsb, podposps_cmd, podposps_fb, podpossb_cmd, podpossb_fb,
            compass, tunnelthruster;

    @FXML
    Tile throttletrend, speedtrend;

    Datahandler dh = GUISystem.dh;
    InputController ic = GUISystem.inputController;

    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        System.out.println("You clicked me!");
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
        throttleps.setProgress(ic.getBtnLyGUI());
        throttlesb.setProgress(ic.getBtnLyGUI());

        speedps.setValue(ic.getBtnLy());
        speedsb.setValue(ic.getBtnLy());
        podposps_cmd.setValue(ic.getBtnLy());
        podpossb_cmd.setValue(ic.getBtnLy());
        podpossb_fb.setValue(ic.getBtnLy());
        podposps_fb.setValue(ic.getBtnLy());
        tunnelthruster.setValue(ic.getBtnLy());
        compass.setValue(ic.getBtnLy());
        throttletrend.setValue(ic.getBtnLy());
        speedtrend.setValue(ic.getBtnLy());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //setController(this);
    }

}
