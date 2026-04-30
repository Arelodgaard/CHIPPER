package no.lodgaard.chipper;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import no.lodgaard.chipper.logic.CPU;
import no.lodgaard.chipper.logic.Memory;
import no.lodgaard.chipper.logic.RomLoader;

import java.io.FileNotFoundException;


public class Main extends Application{

    private Stage stage;

    private final int width = 1024;
    private final int height = 512;
    private final int pixelSizeWidth = width / 64;
    private final int pixelSizeHeight = height / 32;

    private WritableImage chipImage;
    private Canvas canvas;
    private PixelWriter pixelWriter;
    private GraphicsContext gc;

    private Memory memory;
    private RomLoader romLoader;
    private CPU cpu;

    @Override
    public void start(Stage stage) throws FileNotFoundException {

        canvas = new Canvas(width, height);

        // Add canvas to the scene
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 1024, 512);
        stage.setTitle("CHIPPER v.0.0.1");
        stage.setScene(scene);
        stage.show();

        memory = new Memory();

        romLoader = new RomLoader(memory);

        romLoader.loadRom("src/main/resources/1-chip8-logo.ch8");

        cpu = new CPU(memory);

        for (byte b : memory.getMemoryArray()) {
            System.out.println(Byte.toString(b));
        }



        System.out.println(memory.getMemoryArray().length);



    }

    static void main() {

        Memory memory = new Memory();

        System.out.println(Memory.indexRegister("1ff"));






    }



}
