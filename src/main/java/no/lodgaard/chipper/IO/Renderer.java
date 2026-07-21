package no.lodgaard.chipper.IO;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Renderer{

    private int _width;
    private int _height;

    private final int pixelSizeWidth;
    private final int pixelSizeHeight;

    private GraphicsContext _gc;

    private static Stage stage;




    public Renderer(GraphicsContext gc, int width, int height) {
        this._gc = gc;
        this._width = width;
        this._height = height;

        pixelSizeWidth = width / 64;
        pixelSizeHeight = height / 32;
    }

    /*
    public void drawExample() {
        _gc.fillRect(50, 50, 100, 100);
        _gc.strokeOval(200, 200, 80, 80);
        _gc.fillText("CHIPPER", 300, 100);


    }

    */

    //Simply clears screen with a big rectangle
    public void clearScreen() {

        _gc.clearRect(0, 0, _width, _height);
    }

    public void drawPixel(int posX, int posY) {



        _gc.fillRect(posX, posY, pixelSizeWidth, pixelSizeHeight);
    }
    /*
    public Canvas getCanvas() {

        return this.canvas;
    }
    */


}
