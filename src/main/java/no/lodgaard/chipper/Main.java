package no.lodgaard.chipper;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import no.lodgaard.chipper.IO.Display;
import no.lodgaard.chipper.logic.CPU;
import no.lodgaard.chipper.logic.Memory;
import no.lodgaard.chipper.logic.RomLoader;

import java.io.FileNotFoundException;


public class Main extends Application{

    private Stage stage;

    public static final int width = 1024;
    public static final int height = 512;




    private Memory memory;
    private RomLoader romLoader;
    private CPU cpu;
    private Display display;

    @Override
    public void start(Stage stage) throws FileNotFoundException {





        memory = new Memory();

        romLoader = new RomLoader(memory);

        romLoader.loadRom("src/main/resources/1-chip8-logo.ch8");

        cpu = new CPU(memory);

        display = new Display(width, height);

        for (byte b : memory.getMemoryArray()) {
            System.out.println(Byte.toString(b));
        }

        display.drawExample();

        display.drawPixel(20, 45);


        System.out.println(memory.getMemoryArray().length);

        // Add canvas to the scene
        StackPane root = new StackPane(display.getCanvas());
        Scene scene = new Scene(root, width, height);
        stage.setTitle("CHIPPER v.0.0.1");
        stage.setScene(scene);
        stage.show();

    }

    static void main() {

        Memory memory = new Memory();

        System.out.println(Memory.indexRegister("1ff"));






    }



}
