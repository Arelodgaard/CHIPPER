package no.lodgaard.chipper.IO;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import no.lodgaard.chipper.DisplayLaunch;
import no.lodgaard.chipper.Main;

public class Display extends Application {

    public static final int width = 1024;
    public static final int height = 512;

    private final int pixelSizeWidth = width / 64;
    private final int pixelSizeHeight = height / 32;

    private Canvas canvas;
    private GraphicsContext gc;

    private static Stage stage;




    public Display() {
        canvas = new Canvas(width,height);
        gc = canvas.getGraphicsContext2D();
    }


    public void drawExample() {
        gc.fillRect(50, 50, 100, 100);
        gc.strokeOval(200, 200, 80, 80);
        gc.fillText("CHIPPER", 300, 100);


    }

    //Simply clears screen with a big rectangle
    public void clearScreen() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawPixel(int posX, int posY) {



        gc.fillRect(posX, posY, pixelSizeWidth, pixelSizeHeight);
    }

    public Canvas getCanvas() {

        return this.canvas;
    }

    @Override
    public void start(Stage stage){

        // Add canvas to the scene
        StackPane root = new StackPane(getCanvas());
        Scene scene = new Scene(root, width, height);
        stage.setTitle("CHIPPER v.0.0.1");
        stage.setScene(scene);
        stage.show();

    }


}
