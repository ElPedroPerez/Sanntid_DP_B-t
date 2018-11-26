/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public static Scene scene;

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
