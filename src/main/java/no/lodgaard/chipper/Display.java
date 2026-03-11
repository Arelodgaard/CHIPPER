package no.lodgaard.chipper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Display extends Application {

    private final int width = 1024;
    private final int height = 512;
    private final int pixelSizeWidth = width / 64;
    private final int pixelSizeHeight = height / 32;

    private WritableImage chipImage;
    private Canvas canvas;
    private PixelWriter pixelWriter;
    private GraphicsContext gc;


    @Override
    public void start(Stage stage) {
        // Create a canvas
        canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw a single pixel (a 1x1 rectangle)
        gc.setFill(Color.RED);
        gc.fillRect(100, 100, pixelSizeWidth, pixelSizeHeight);

        // Draw multiple pixels (e.g., a line)
        for (int i = 0; i < 50; i++) {
            gc.fillRect(116 + i, 150, pixelSizeWidth, pixelSizeHeight);
        }

        // Add canvas to the scene
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 1024, 512);
        stage.setTitle("CHIPPER v.0.0.1");
        stage.setScene(scene);
        stage.show();
    }


}
