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
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

/**
 *
 * @author Haakon
 */
public class FXMLDocumentController implements Initializable
{

    private boolean buttonAlreadyPressed = false;
    private boolean btn_main_active = true;
    private boolean btn_dp_active = false;
    private boolean btn_alarms_active = false;

    @FXML
    Button btn_main, btn_dp, btn_alarms, btn_lock, btn_exit, newButton;

    @FXML
    ProgressBar throttleps, throttlesb;

    @FXML
    Gauge speedps, speedsb, podposps_cmd, podposps_fb, podpossb_cmd,
            podpossb_fb, compass, tunnelthruster;

    @FXML
    Tile throttletrend, speedtrend, clock;

    @FXML
    Label port_lbl, starboard_lbl, title_lbl, speedps_lbl, speedsb_lbl, podposps_lbl, podpossb_lbl,
            tunnelthruster_lbl, compass_lbl, alarm_lbl;

    @FXML
    VBox centerBox;

    Datahandler dh = GUISystem.dh;
    InputController ic = GUISystem.inputController;

    @FXML
    private void handleMainButtonAction(ActionEvent event)
    {
        btn_main_active = true;
        btn_dp_active = false;
        btn_alarms_active = false;
//        btn_dp.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
//        btn_alarms.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");

        centerBox.getChildren().clear();
        centerBox.getChildren().add(speedtrend);
        centerBox.getChildren().add(throttletrend);
    }

    @FXML
    private void handleDpButtonAction(ActionEvent event)
    {
        btn_main_active = false;
        btn_dp_active = true;
        btn_alarms_active = false;
//        btn_main.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
//        btn_alarms.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");

        centerBox.getChildren().clear();
        centerBox.getChildren().add(newButton);

    }

    @FXML
    private void handleAlarmsButtonAction(ActionEvent event)
    {
        btn_main_active = false;
        btn_dp_active = false;
        btn_alarms_active = true;

//        btn_main.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
//        btn_dp.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
        centerBox.getChildren().clear();
        centerBox.getChildren().add(newButton);

    }

    @FXML
    private void handleLockButtonAction(ActionEvent event)
    {
        if (!buttonAlreadyPressed)
        {
            buttonAlreadyPressed = true;
            btn_lock.setText("UNLOCK");
        }
        else if (buttonAlreadyPressed)
        {
            buttonAlreadyPressed = false;
            btn_lock.setText("LOCK");
        }
    }

    @FXML
    private void exitButtonAction(ActionEvent event)
    {
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    @FXML
    private void onMouseClickedAction(MouseEvent event)
    {
        setButtonSelectedOn(event);
    }

    @FXML
    private void onMouseReleasedAction(MouseEvent event)
    {
        setButtonSelectedOff(event);
    }

    @FXML
    private void enterHoverButtonAction(MouseEvent event)
    {
        setButtonSelectedOn(event);
    }

    @FXML
    private void exitHoverButtonAction(MouseEvent event)
    {
        setButtonSelectedOff(event);
    }

    private void setButtonSelectedOn(MouseEvent event)
    {
        String button = event.getSource().toString();
        if (button.toLowerCase().contains("btn_main"))
        {
            btn_main.setStyle("-fx-background-color: #3d4b5b; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
        }
        if (button.toLowerCase().contains("btn_dp"))
        {
            btn_dp.setStyle("-fx-background-color: #3d4b5b; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
        }
        if (button.toLowerCase().contains("btn_alarms"))
        {
            btn_alarms.setStyle("-fx-background-color: #3d4b5b; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
        }
        if (button.toLowerCase().contains("btn_lock"))
        {
            btn_lock.setStyle("-fx-background-color: #3d4b5b; -fx-font-weight: bold; -fx-font-size: 14;");
        }
        if (button.toLowerCase().contains("btn_exit"))
        {
            btn_exit.setStyle("-fx-background-color: #3d4b5b; -fx-font-size: 14;");
        }
    }

    private void setButtonSelectedOff(MouseEvent event)
    {
        String button = event.getSource().toString();
        if (button.toLowerCase().contains("btn_main"))
        {
            btn_main.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
        }
        if (button.toLowerCase().contains("btn_dp"))
        {
            btn_dp.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
        }
        if (button.toLowerCase().contains("btn_alarms"))
        {
            btn_alarms.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
        }
        if (button.toLowerCase().contains("btn_lock"))
        {
            btn_lock.setStyle("-fx-background-color: #364250; -fx-font-weight: bold; -fx-font-size: 14;");
        }
        if (button.toLowerCase().contains("btn_exit"))
        {
            btn_exit.setStyle("-fx-background-color: #364250; -fx-font-size: 14;");
        }
    }

    public void updatePage()
    {
        //System.out.println("updated");
        //clock.

        throttleps.setProgress(ic.getBtnLyGUI());
        throttlesb.setProgress(ic.getBtnLyGUI());

        speedps_lbl.setText(Integer.toString(dh.getFb_speedPS()));
        speedsb_lbl.setText(Integer.toString(dh.getFb_speedSB()));
        speedps.setValue(dh.getFb_speedPS());
        speedsb.setValue(dh.getFb_speedSB());

        podposps_cmd.setRotate(ic.getAngleForGUI());
        podpossb_cmd.setRotate(ic.getAngleForGUI());
        podposps_fb.setRotate(dh.getFb_podPosPS());
        podpossb_fb.setRotate(dh.getFb_podPosSB());
        podposps_lbl.setText(Integer.toString(dh.getFb_podPosPS()));
        podpossb_lbl.setText(Integer.toString(dh.getFb_podPosSB()));

        compass.setRotate(dh.getFb_heading());
        compass_lbl.setText(Integer.toString(dh.getFb_heading()));

        tunnelThruster();

        throttletrend.setValue(ic.getBtnLy());
        speedtrend.setValue(ic.getBtnLy());

        throttletrend.setValue(ic.getBtnLyGUI());
        speedtrend.setValue(dh.getFb_speedPS());
        //speedtrend.setChartData(DATA);

//        //System.out.println("updated (test)");
//        throttleps.setProgress(ic.getBtnLyGUI());
//        throttlesb.setProgress(ic.getBtnLyGUI());
//
//        speedps_lbl.setText(Integer.toString(ic.getBtnLy()));
//        speedsb_lbl.setText(Integer.toString(ic.getBtnLy()));
//        speedps.setValue(ic.getBtnLy());
//        speedsb.setValue(ic.getBtnLy());
//
//        podposps_cmd.setRotate(ic.getAngleForGUI());
//        podpossb_cmd.setRotate(ic.getAngleForGUI());
//        podposps_fb.setRotate(ic.getAngleForGUI());
//        podpossb_fb.setRotate(ic.getAngleForGUI());
//        podposps_lbl.setText(Integer.toString(ic.getAngle()));
//        podpossb_lbl.setText(Integer.toString(ic.getAngle()));
//
//        compass.setRotate(ic.getAngleForGUI());
//        compass_lbl.setText(Integer.toString(ic.getAngle()));
//
//        tunnelThruster();
//
//        throttletrend.setValue(ic.getBtnLy());
//        speedtrend.setValue(ic.getBtnLy());
    }

    public void tunnelThruster()
    {
        boolean psCommand = ic.getBtnL1();
        boolean sbCommand = ic.getBtnR1();

        if (psCommand && !sbCommand)
        {
            tunnelthruster.setValue(25);
            tunnelthruster_lbl.setText("<");
            tunnelthruster.setRotate(225);
        }
        else if (sbCommand && !psCommand)
        {
            tunnelthruster.setValue(25);
            tunnelthruster_lbl.setText(">");
            tunnelthruster.setRotate(45);
        }
        else if (psCommand && sbCommand)
        {
            tunnelthruster_lbl.setText("X");
            tunnelthruster.setValue(0);
        }
        else
        {
            tunnelthruster_lbl.setText("-");
            tunnelthruster.setValue(0);
        }
    }

    public void checkButtonStates()
    {
        if (btn_main_active && !btn_dp_active && !btn_alarms_active)
        {
            btn_main.setStyle("-fx-background-color: #3d4b5b; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
            btn_dp.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
            btn_alarms.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
        }
        if (!btn_main_active && btn_dp_active && !btn_alarms_active)
        {
            btn_dp.setStyle("-fx-background-color: #3d4b5b; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
            btn_main.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
            btn_alarms.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
        }
        if (!btn_main_active && !btn_dp_active && btn_alarms_active)
        {
            btn_alarms.setStyle("-fx-background-color: #3d4b5b; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
            btn_main.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
            btn_dp.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
        }
    }

    public void buildAlarmBox()
    {
        newButton = new Button("test");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //checkButtonStates();
        buildAlarmBox();
        speedps_lbl.setTextFill(Color.web("#d8dbe2"));
        speedsb_lbl.setTextFill(Color.web("#d8dbe2"));
        podposps_lbl.setTextFill(Color.web("#d8dbe2"));
        podpossb_lbl.setTextFill(Color.web("#d8dbe2"));
        compass_lbl.setTextFill(Color.web("#d8dbe2"));
        tunnelthruster_lbl.setTextFill(Color.web("#d8dbe2"));
        port_lbl.setTextFill(Color.web("8e9397"));
        starboard_lbl.setTextFill(Color.web("8e9397"));
        title_lbl.setTextFill(Color.web("8e9397"));
        btn_lock.setTextFill(Color.web("8e9397"));
        btn_main.setTextFill(Color.web("8e9397"));
        btn_dp.setTextFill(Color.web("8e9397"));
        btn_alarms.setTextFill(Color.web("8e9397"));
        alarm_lbl.setTextFill(Color.web("9c2f2f"));
        btn_exit.setTextFill(Color.web("8e9397"));

    }

}
