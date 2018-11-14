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

    private int count = 0;

    @Override
    public void run()
    {
        while (true)
        {
            Application.launch();
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = fxmlLoader.load();
        FXMLDocumentController fxController = fxmlLoader.getController();
        //fxController.initialize(url, rb);
        //fxmlLoader.setController(fxController);
        Scene scene = new Scene(root);

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
                        Thread.sleep(1000);
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

        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setFullScreen(true);
        stage.show();

    }
}
