/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Haakon
 */
public class GUI extends Application implements Runnable
{

    private boolean launched = false;
    private int count = 0;
    public static Stage window;
    public static Scene scene, scene_Alarms, scene_DP;

    @Override
    public void run()
    {
        while (true)
        {
            if (!launched)
            {
                Application.launch();
                this.launched = true;
            }
        }
    }

    private void incrementCount()
    {
        count++;
        System.out.println(count);

    }

    @Override
    public void start(Stage stage) throws Exception
    {
        window = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = fxmlLoader.load();
        FXMLDocumentController fxController = fxmlLoader.getController();
        this.scene = new Scene(root);

        FXMLLoader fxmlAlarmsLoader = new FXMLLoader(getClass().getResource("FXML_AlarmsPage.fxml"));
        Parent root_Alarms = fxmlAlarmsLoader.load();
        this.scene_Alarms = new Scene(root_Alarms);

        FXMLLoader fxmlDPLoader = new FXMLLoader(getClass().getResource("FXML_DPPage.fxml"));
        Parent root_DP = fxmlDPLoader.load();
        this.scene_DP = new Scene(root_DP);

        // longrunning operation runs on different thread 
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Runnable updater = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        fxController.updatePage();
                        //Button btn_main = (Button) root.lookup("btn_main");
                        //btn_main.setText("test");
                        //fxController.updatePage("update ");
                        //incrementCount();
                    }
                };
                while (true)
                {
                    try
                    {
                        Thread.sleep(1);
                    }
                    catch (InterruptedException ex)
                    {
                    }
                    // UI update is run on the Application thread 
                    Platform.runLater(updater);
                }
            }
        });
        // don't let thread prevent JVM shutdown 

        thread.setDaemon(true);
        thread.start();

        window.setScene(scene);
        window.initStyle(StageStyle.UNDECORATED);
        window.setFullScreen(true);
        window.show();

    }
}
