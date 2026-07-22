package no.lodgaard.chipper.IO;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Arrays;




public class Renderer{

    private int _width;
    private int _height;

    private final int pixelSizeWidth;
    private final int pixelSizeHeight;

    private final double gridWidth = 64;
    private final double gridHeight = 32;

    private double scale_x;
    private double scale_y;

    //2D Array/Matrix that determines if pixel flipped
    private int gridX = 64;
    private int gridY = 32;
    private int[][] pixelGrid = new int[gridX][gridY];

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

        for (int i = 0; i < pixelGrid.length; i++) {
            Arrays.fill(pixelGrid[i], 0);
            System.out.println(Arrays.toString(pixelGrid[i]));
        }


    }



    //Simply clears screen with a big rectangle
    public void clearScreen() {

        int[][] bufferGrid = new int[gridX][gridY];

        //To flip all gridpixels to zero
        for (int i = 0; i < bufferGrid.length; i++) {
            Arrays.fill(bufferGrid[i], 0);
        }

        setPixelGrid(bufferGrid);

        drawScreen(getPixelGrid());
    }

    private void updatePixelGrid(int[][] grid) {




    }

    public void flipPixel(int posX, int posY) {

        int pixelBit = getPixelGridValue(posX, posY);

        switch (pixelBit) {
            case 0:
                setPixelGridBit(posX, posY, 1);
                break;
            case 1:
                setPixelGridBit(posX, posY, 0);
                break;
            default:
                break;
        }

        drawScreen(getPixelGrid());


    }

    public void drawScreen(int[][] grid) {

        //To either draw or clear pixel at the iterated position.
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (getPixelGridValue(i,j) == 1) {
                    drawPixel(i, j);
                } else if (getPixelGridValue(i,j) == 0) {
                    clearPixel(i, j);
                }
            }
        }


    }

    //Remember as position starts at 0 not 1, max value is 63 x 31 not 64 x 32
    private void drawPixel(int posX, int posY) {



        _gc.fillRect(posX * scale_x, posY * scale_y, pixelSizeWidth, pixelSizeHeight);
    }

    private void clearPixel(int posX, int posY) {
        _gc.clearRect(posX * scale_x, posY * scale_y, pixelSizeWidth, pixelSizeHeight);

    }


    public int[][] getPixelGrid() {
        return pixelGrid;
    }

    public int getPixelGridValue(int posX, int posY) {

        if (posX >= 64) posX -= 64;
        if (posY >= 32) posX -= 32;

        return getPixelGrid()[posX][posY];

    }

    public void setPixelGridBit(int posX, int posY, int value) {

        if (posX >= 64) posX -= 64;
        if (posY >= 32) posX -= 32;

        getPixelGrid()[posX][posY] = value;
    }

    public void setPixelGrid(int[][] pixelGrid) {
        this.pixelGrid = pixelGrid;
    }

    /*private void setPixelGridBit(int x, int y, int bit) {
        int[][] bufferGrid = getPixelGrid();

        bufferGrid[x][y] = bit;

        setPixelGrid(bufferGrid);
    }*/



    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < pixelGrid.length; i++) {
            sb.append(Arrays.toString(pixelGrid[i]));
            sb.append("\n");
        }
        return sb.toString();
    }


}
