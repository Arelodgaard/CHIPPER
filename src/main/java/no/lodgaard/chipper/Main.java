package no.lodgaard.chipper;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import no.lodgaard.chipper.IO.Renderer;
import no.lodgaard.chipper.logic.CPU;
import no.lodgaard.chipper.logic.Memory;
import no.lodgaard.chipper.logic.RomLoader;

import java.io.FileNotFoundException;



public class Main extends Application {

    private static Stage stage;

    public static final int width = 1024;
    public static final int height = 512;



    private static Memory memory;
    private static RomLoader romLoader;
    private static CPU cpu;
    private static Renderer renderer;

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage stage) throws FileNotFoundException {

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Renderer renderer = new Renderer(gc, width, height);
        //renderer.draw();

        Pane root = new Pane(canvas);
        stage.setScene(new Scene(root));
        stage.show();

        memory = new Memory();


        romLoader = new RomLoader(memory);
        romLoader.loadRom("src/main/resources/1-chip8-logo.ch8");

        cpu = new CPU(memory, renderer);









        renderer.clearScreen();
        renderer.drawPixel(0, 0);
        renderer.drawPixel(0, 31);
        renderer.drawPixel(63, 0);
        renderer.drawPixel(63, 31);




        //Test suite

        for (byte b : memory.getMemoryArray()) {
            System.out.println(Byte.toString(b));
        }

        System.out.println(memory.getMemoryArray().length);

        //Main.renderer.drawExample();


    }

}
