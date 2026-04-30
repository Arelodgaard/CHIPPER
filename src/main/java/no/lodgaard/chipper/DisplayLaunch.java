package no.lodgaard.chipper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import no.lodgaard.chipper.IO.Display;
import no.lodgaard.chipper.logic.CPU;
import no.lodgaard.chipper.logic.Memory;
import no.lodgaard.chipper.logic.RomLoader;

import java.io.FileNotFoundException;

public class DisplayLaunch extends Application {

    private static Stage stage;

    public static final int width = 1024;
    public static final int height = 512;






    @Override
    public void start(Stage stage) throws FileNotFoundException {







        // Add canvas to the scene
        StackPane root = new StackPane(display.getCanvas());
        Scene scene = new Scene(root, width, height);
        stage.setTitle("CHIPPER v.0.0.1");
        stage.setScene(scene);
        stage.show();

    }

}
