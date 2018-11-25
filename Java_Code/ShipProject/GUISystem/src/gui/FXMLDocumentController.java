/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import InputController.InputController;
import eu.hansolo.medusa.Gauge;
import guisystem.DataHandler;
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
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.chart.ChartDataBuilder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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

    private boolean ackButtonAlreadyPressed = false;
    private boolean btn_main_active = true;
    private boolean btn_dp_active = false;
    private boolean btn_alarms_active = false;

    @FXML
    Pane alarmPane, centerPane;

    @FXML
    Button btn_main, btn_dp, btn_alarms, btn_lock, btn_exit, btn_ack;

    @FXML
    ProgressBar throttleps, throttlesb;

    @FXML
    Gauge speedps, speedsb, podposps_cmd, podposps_fb, podpossb_cmd,
            podpossb_fb, compass, tunnelthruster;

    @FXML
    Tile throttletrend, speedtrend, clock;

    @FXML
    Label port_lbl, starboard_lbl, title_lbl, speedps_lbl, speedsb_lbl,
            podposps_lbl, podpossb_lbl, tunnelthruster_lbl, compass_lbl,
            alarm_lbl, ping_lbl;

    @FXML
    VBox centerBox, vbox;

    @FXML
    HBox alarmBox;

    @FXML
    StackPane stack1;

    DataHandler dh = GUISystem.dh;
    InputController ic = GUISystem.inputController;

    @FXML
    private void handleMainButtonAction(ActionEvent event)
    {
        btn_main_active = true;
        btn_dp_active = false;
        btn_alarms_active = false;
        title_lbl.setText("MAIN");
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
        title_lbl.setText("DYNAMIC POSITIONING");
//        btn_main.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
//        btn_alarms.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");

        centerBox.getChildren().clear();
        ImageView centerImage = new ImageView();
        Image image = new Image(getClass().getResourceAsStream("img/radar.jpg"));
        centerImage.setPreserveRatio(true);
        centerImage.setImage(image);
        centerBox.getChildren().add(centerImage);

    }

    @FXML
    private void handleAlarmsButtonAction(ActionEvent event)
    {
        btn_main_active = false;
        btn_dp_active = false;
        btn_alarms_active = true;
        title_lbl.setText("ALARMS");

//        btn_main.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
//        btn_dp.setStyle("-fx-background-color: #364250; -fx-font-family: Yu Gothic; -fx-font-size: 16;");
        centerBox.getChildren().clear();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.prefWidth(724);
        centerBox.prefHeight(510);
        centerBox.minWidth(724);
        centerBox.minHeight(510);
        buildAlarmBox();
        centerBox.getChildren().add(alarmBox);
    }

    @FXML
    private void handleLockButtonAction(ActionEvent event)
    {
        if (!ackButtonAlreadyPressed)
        {
            ackButtonAlreadyPressed = true;
            btn_lock.setText("UNLOCK");
        }
        else if (ackButtonAlreadyPressed)
        {
            ackButtonAlreadyPressed = false;
            btn_lock.setText("LOCK");
        }
    }

    @FXML
    private void handleAckButtonAction(ActionEvent event)
    {
        // acknowledge alarms
        System.out.println("Acknowledged all alarms.");
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
        if (button.toLowerCase().contains("btn_ack"))
        {
            btn_ack.setStyle("-fx-pref-width: 81; -fx-pref-height: 32; -fx-background-color: #3d4b5b; -fx-font-size: 14; -fx-font-weight: Bold; -fx-effect: None;");
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
        if (button.toLowerCase().contains("btn_ack"))
        {
            btn_ack.setStyle("-fx-pref-width: 81; -fx-pref-height: 32; -fx-background-color: #364250; -fx-font-size: 14; -fx-font-weight: Bold; -fx-effect: None;");
        }
    }

    public void updatePage()
    {
        //System.out.println("updated");

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
        alarmBox = new HBox();
        vbox = new VBox();
        stack1 = new StackPane();
        StackPane stack2 = new StackPane();
        StackPane stack3 = new StackPane();
        StackPane stack4 = new StackPane();
        StackPane stack5 = new StackPane();
        StackPane stack6 = new StackPane();
        StackPane stack7 = new StackPane();
        StackPane stack8 = new StackPane();
        StackPane stack9 = new StackPane();
        StackPane stack10 = new StackPane();
        StackPane stack11 = new StackPane();
        StackPane stack12 = new StackPane();
        Label label1 = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        Label label4 = new Label();
        Label label5 = new Label();
        Label label6 = new Label();
        Label label7 = new Label();
        Label label8 = new Label();
        btn_ack = new Button();

        alarmBox.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER; -fx-background-color: #272c32;");

        vbox.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER; -fx-background-color: #272c32;");

        stack1.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER_LEFT; -fx-background-color: #2e3642;");
        stack2.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER_LEFT; -fx-background-color: #2a323d;");
        stack3.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER_LEFT; -fx-background-color: #2e3642;");
        stack4.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER_LEFT; -fx-background-color: #2a323d;");
        stack5.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER_LEFT; -fx-background-color: #2e3642;");
        stack6.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER_LEFT; -fx-background-color: #2a323d;");
        stack7.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER_LEFT; -fx-background-color: #2e3642;");
        stack8.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER_LEFT; -fx-background-color: #2a323d;");
        stack9.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER_LEFT; -fx-background-color: #272c32;");
        stack10.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER; -fx-background-color: #272c32;");
        stack11.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER_LEFT; -fx-background-color: #272c32;");
        stack12.setStyle("-fx-pref-width: 724; -fx-pref-height: 510; -fx-alignment: CENTER_LEFT; -fx-background-color: #272c32;");

        label1.setText("SB Speed Feedback Alarm");
        label1.setPadding(new Insets(0, 0, 0, 50));
        label1.setFont(Font.font("Segoe UI", 13));
        label1.setTextFill(Color.web("#8e9397"));
        label2.setText("PS Speed Feedback Alarm");
        label2.setPadding(new Insets(0, 0, 0, 50));
        label2.setFont(Font.font("Segoe UI", 13));
        label2.setTextFill(Color.web("#8e9397"));
        label3.setText("SB Azimuth Thruster Position Feedback Alarm");
        label3.setPadding(new Insets(0, 0, 0, 50));
        label3.setFont(Font.font("Segoe UI", 13));
        label3.setTextFill(Color.web("#8e9397"));
        label4.setText("SB Azimuth Thruster Position Feedback Alarm");
        label4.setPadding(new Insets(0, 0, 0, 50));
        label4.setFont(Font.font("Segoe UI", 13));
        label4.setTextFill(Color.web("#8e9397"));
        label5.setText("Vision Deviation Alarm");
        label5.setPadding(new Insets(0, 0, 0, 50));
        label5.setFont(Font.font("Segoe UI", 13));
        label5.setTextFill(Color.web("#8e9397"));
        label6.setText("Ship Roll High Alarm");
        label6.setPadding(new Insets(0, 0, 0, 50));
        label6.setFont(Font.font("Segoe UI", 13));
        label6.setTextFill(Color.web("#8e9397"));
        label7.setText("Communication Ping High Alarm");
        label7.setPadding(new Insets(0, 0, 0, 50));
        label7.setFont(Font.font("Segoe UI", 13));
        label7.setTextFill(Color.web("#8e9397"));
        label8.setText("Communication Lost Alarm");
        label8.setPadding(new Insets(0, 0, 0, 50));
        label8.setFont(Font.font("Segoe UI", 13));
        label8.setTextFill(Color.web("#8e9397"));
        btn_ack.setText("ACK");
        btn_ack.setId("btn_ack");
        btn_ack.setStyle("-fx-pref-width: 81; -fx-pref-height: 32; -fx-background-color: #364250; -fx-font-size: 14; -fx-font-weight: Bold; -fx-effect: None;");
        btn_ack.setTextFill(Color.web("8e9397"));
        btn_ack.setOnAction(event -> this.handleAckButtonAction(event));
        btn_ack.setOnMouseEntered(event -> this.enterHoverButtonAction(event));
        btn_ack.setOnMouseExited(event -> this.exitHoverButtonAction(event));

        stack1.getChildren().add(label1);
        stack2.getChildren().add(label2);
        stack3.getChildren().add(label3);
        stack4.getChildren().add(label4);
        stack5.getChildren().add(label5);
        stack6.getChildren().add(label6);
        stack7.getChildren().add(label7);
        stack8.getChildren().add(label8);
        stack10.getChildren().add(btn_ack);

        vbox.getChildren().add(stack1);
        vbox.getChildren().add(stack2);
        vbox.getChildren().add(stack3);
        vbox.getChildren().add(stack4);
        vbox.getChildren().add(stack5);
        vbox.getChildren().add(stack6);
        vbox.getChildren().add(stack7);
        vbox.getChildren().add(stack8);
        vbox.getChildren().add(stack9);
        vbox.getChildren().add(stack10);
        vbox.getChildren().add(stack11);
        //vbox.getChildren().add(stack12);

        alarmBox.setAlignment(Pos.CENTER);
        alarmBox.setPadding(new Insets(30, 0, 0, 0));
        alarmBox.getChildren().add(vbox);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //checkButtonStates();
        //buildAlarmBox();

        clock.setRunning(true);
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
        btn_exit.setTextFill(Color.web("8e9397"));
        ping_lbl.setTextFill(Color.web("8e9397"));
        alarm_lbl.setTextFill(Color.web("9c2f2f"));
        //btn_ack.setTextFill(Color.web("8e9397"));

    }

}
