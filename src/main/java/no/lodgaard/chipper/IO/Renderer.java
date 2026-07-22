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

    private final double gridWidth = 64;
    private final double gridHeight = 32;

    private double scale_x;
    private double scale_y;

    private GraphicsContext _gc;

    private static Stage stage;

    public enum Pixel {


    }



    public Renderer(GraphicsContext gc, int width, int height) {
        this._gc = gc;
        this._width = width;
        this._height = height;

        pixelSizeWidth = width / 64;
        pixelSizeHeight = height / 32;

        //Provides a scale to multiply with to get position on the grid
        //Scale = Size_of_window / grid(width or height).
        scale_x = _width / gridWidth;
        scale_y = _height / gridHeight;


    }



    //Simply clears screen with a big rectangle
    public void clearScreen() {

        _gc.clearRect(0, 0, _width, _height);
    }


    //Remember as position starts at 0 not 1, max value is 63 x 31 not 64 x 32
    public void drawPixel(int posX, int posY) {



        _gc.fillRect(posX * scale_x, posY * scale_y, pixelSizeWidth, pixelSizeHeight);
    }



}
