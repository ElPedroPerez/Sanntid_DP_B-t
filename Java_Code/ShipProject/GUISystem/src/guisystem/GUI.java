/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guisystem;

import InputController.InputController;
import eu.hansolo.medusa.*;
import eu.hansolo.medusa.*;
import eu.hansolo.medusa.skins.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.*;

/**
 *
 * @author Haakon
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import eu.hansolo.medusa.*;
import eu.hansolo.medusa.*;
import eu.hansolo.medusa.skins.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import static javafx.geometry.Pos.TOP_LEFT;
import static javafx.geometry.Pos.TOP_RIGHT;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import guisystem.Datahandler;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Haakon
 */
public class GUI extends Application implements Runnable, Observer
{

    private Datahandler dh;

    private GridPane rightPane;
    private GridPane leftPane;
    private Gauge speedPS;
    private Gauge speedSB;
    private Gauge podPosPS;
    private Gauge podPosSB;
    private Gauge heading;
    private Gauge sample1;
    private Gauge sample2;
    private Gauge sample3;
    private Gauge sample4;

    public GUI()
    {

    }

    @Override
    public void run()
    {
        Application.launch();
        while (true)
        {
            try
            {
                // run
                updateGUI();
                // this will never run at this point.
                // Class is stuck in lauch method, make a seperate
                // controller class to fix this?

                Thread.sleep(100);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void updateGUI()
    {
        this.podPosPS.setValue(this.dh.getAngle());
        this.podPosSB.setValue(this.dh.getAngle());
    }

    @Override

    public void init()
    {
        GaugeBuilder builder = GaugeBuilder.create(); //.skin(SlimSkin.class);
        speedPS = builder.decimals(0).maxValue(5000).unit("RPM").title("SPEED").build();
        speedSB = builder.decimals(0).maxValue(5000).unit("RPM").title("SPEED").build();
        podPosPS = builder.decimals(1).maxValue(360).unit("DEGREES").title("POD POS").build();
        podPosSB = builder.decimals(1).maxValue(360).unit("DEGREES").title("POD POS").build();
        heading = builder.decimals(1).maxValue(360).unit("HEADING").title("COMPASS").build();
        sample1 = builder.decimals(1).maxValue(20).unit("UNIT").title("TITLE").build();
        sample2 = builder.decimals(1).maxValue(20).unit("UNIT").title("TITLE").build();
        sample3 = builder.decimals(1).maxValue(20).unit("UNIT").title("TITLE").build();
        sample4 = builder.decimals(1).maxValue(20).unit("UNIT").title("TITLE").build();

        VBox speedPsBox = getTitleTopicBox("PORT", Color.rgb(229, 115, 115), speedPS);
        VBox speedSbBox = getTitleTopicBox("STARBOARD", Color.rgb(129, 199, 132), speedSB);
        VBox podPosPsBox = getTopicBox("POD POS", Color.rgb(77, 208, 225),
                podPosPS);
        VBox podPosSbBox = getTopicBox("POD POS", Color.rgb(255, 183, 77), podPosSB);
        VBox headingBox = getTopicBox("COMPASS", Color.rgb(149, 117, 205), heading);
        VBox sample1Box = getTopicBox("SAMPLE 1", Color.rgb(186, 104, 200), sample1);
        VBox sample2Box = getTopicBox("SAMPLE 2", Color.rgb(186, 104, 200), sample2);
        VBox sample3Box = getTopicBox("SAMPLE 3", Color.rgb(186, 104, 200), sample3);
        VBox sample4Box = getTopicBox("SAMPLE 4", Color.rgb(186, 104, 200), sample4);

        rightPane = new GridPane();
        rightPane.setPadding(new Insets(20));
        rightPane.setHgap(10);
        rightPane.setVgap(15);
        rightPane.setBackground(new Background(new BackgroundFill(Color.rgb(39, 44, 50), CornerRadii.EMPTY, Insets.EMPTY)));
        rightPane.add(speedPsBox, 0, 0);
        rightPane.add(speedSbBox, 2, 0);
        rightPane.add(podPosPsBox, 0, 2);
        rightPane.add(podPosSbBox, 2, 2);
        rightPane.add(headingBox, 0, 4);
        rightPane.add(sample1Box, 2, 4);
        rightPane.alignmentProperty().set(TOP_RIGHT);

        leftPane = new GridPane();
        leftPane.setPadding(new Insets(20));
        leftPane.setHgap(10);
        leftPane.setVgap(15);
        leftPane.setBackground(new Background(new BackgroundFill(Color.rgb(39, 44, 50), CornerRadii.EMPTY, Insets.EMPTY)));
        leftPane.add(sample2Box, 0, 0);
        leftPane.add(sample3Box, 2, 0);
        //leftPane.add(sample4Box, 1, 2);
        leftPane.alignmentProperty().set(TOP_LEFT);

    }

    @Override
    public void start(Stage stage)
    {
        HBox rootPane = new HBox();
        rootPane.getChildren().addAll(leftPane, rightPane);
        Scene scene = new Scene(rootPane);

        speedPS.setValue(2451);
        speedSB.setValue(2091);
        podPosPS.setValue(180);
        podPosSB.setValue(180);
        heading.setValue(137.7);
        sample1.setValue(14.2);
        sample2.setValue(14.2);
        sample3.setValue(14.2);
        sample4.setValue(14.2);

        stage.setTitle("M/S MARTHA");
        stage.setScene(scene);
        stage.setWidth(1135);
        stage.setHeight(800);
        stage.show();
    }

    @Override
    public void stop()
    {
        System.exit(0);
    }

    private VBox getTopicBox(final String TEXT, final Color COLOR, final Gauge GAUGE)
    {

        GAUGE.setSkin(new SlimSkin(GAUGE));
        GAUGE.setBarColor(COLOR);
        GAUGE.setBarBackgroundColor(Color.rgb(48, 52, 57));
        GAUGE.setValueColor(Color.rgb(225, 229, 234));
        GAUGE.setUnitColor(Color.rgb(67, 70, 76));
        GAUGE.setTitleColor(COLOR);
        GAUGE.setAnimated(true);

        VBox vBox = new VBox(GAUGE);
        vBox.setSpacing(3);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private VBox getTitleTopicBox(final String TEXT, final Color COLOR, final Gauge GAUGE)
    {
        Rectangle bar = new Rectangle(200, 3);
        bar.setArcWidth(6);
        bar.setArcHeight(6);
        bar.setFill(COLOR);

        Label label = new Label(TEXT);
        label.setTextFill(COLOR);
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(0, 0, 10, 0));

        GAUGE.setSkin(new SlimSkin(GAUGE));
        GAUGE.setBarColor(COLOR);
        GAUGE.setBarBackgroundColor(Color.rgb(48, 52, 57));
        GAUGE.setValueColor(Color.rgb(225, 229, 234));
        GAUGE.setUnitColor(Color.rgb(67, 70, 76));
        GAUGE.setTitleColor(COLOR);
        GAUGE.setAnimated(true);

        VBox vBox = new VBox(bar, label, GAUGE);
        vBox.setSpacing(3);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Overrided method for Observer. Method used for observer pattern. Fetches
     * updated data or video and calls specified update function.
     *
     * @param o Observable object (ReceiveDataObservable or
     * ReceiveVideoOvservable)
     */
    @Override
    public void update(Observable o, Object arg)
    {
        if (o instanceof Datahandler)
        {
            Datahandler dh = (Datahandler) o;
            this.dh = dh;
        }
        else
        {
            System.out.println("Not instance of DH");
        }
    }
}
