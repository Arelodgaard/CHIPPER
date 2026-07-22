package no.lodgaard.chipper;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
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
import java.util.HexFormat;
import java.util.concurrent.TimeUnit;


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
    public void start(Stage stage) throws FileNotFoundException, InterruptedException {

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        renderer = new Renderer(gc, width, height);
        //renderer.draw();

        Pane root = new Pane(canvas);
        stage.setScene(new Scene(root));
        stage.show();

        memory = new Memory();



        romLoader = new RomLoader(memory);
        romLoader.loadRom("src/main/resources/2-ibm-logo.ch8");

        cpu = new CPU(memory, renderer);
        cpu.setProgramCounter(510);




        for (int i = 0; i < 800; i++) {
            System.out.println("Address: " + i + " Hex: " + HexFormat.of().toHexDigits(memory.getMemoryArray()[i]));
        }





        renderer.clearScreen();
        renderer.drawScreen(renderer.getPixelGrid());

        AnimationTimer emulatorLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                int instruction = cpu.fetchInstruction();
                cpu.decodeAndExecute(instruction);
            }
        };
        emulatorLoop.start();








        //Test suite





        //Main.renderer.drawExample();


    }

}
