package no.lodgaard.chipper.IO;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import no.lodgaard.chipper.Main;

public class Display {

    private final int pixelSizeWidth = Main.width / 64;
    private final int pixelSizeHeight = Main.height / 32;

    private Canvas canvas;
    private GraphicsContext gc;

    public Display(int width, int height) {
        canvas = new Canvas(800,600);
        gc = canvas.getGraphicsContext2D();
    }


    public void drawExample() {
        gc.fillRect(50, 50, 100, 100);
        gc.strokeOval(200, 200, 80, 80);
        gc.fillText("CHIPPER", 300, 100);


    }

    public void drawPixel(int posX, int posY) {
        gc.fillRect(posX, posY, pixelSizeWidth, pixelSizeHeight);
    }

    public Canvas getCanvas() {

        return this.canvas;
    }


}
