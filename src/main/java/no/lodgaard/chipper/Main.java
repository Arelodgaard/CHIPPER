package no.lodgaard.chipper;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import no.lodgaard.chipper.IO.KeyCodeListener;
import no.lodgaard.chipper.IO.Renderer;
import no.lodgaard.chipper.logic.CPU;
import no.lodgaard.chipper.logic.Memory;
import no.lodgaard.chipper.logic.RomLoader;

import java.io.FileNotFoundException;
import java.util.HexFormat;
import java.util.concurrent.atomic.AtomicBoolean;


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
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        KeyCodeListener listener = new KeyCodeListener(scene, true, false);
        listener.setUpListeners();
        memory = new Memory();



        romLoader = new RomLoader(memory);
        romLoader.loadRom("src/main/resources/1-chip8-logo.ch8");

        cpu = new CPU(memory, renderer);
        cpu.setProgramCounter(0x200);




        for (int i = 0; i < 1000; i++) {
            System.out.println("Address: " + i + " Hex: " + HexFormat.of().toHexDigits(memory.getMemoryArray()[i]));
        }




        final int CYCLES_PER_FRAME = 700 / 60;

        renderer.clearScreen();
        renderer.drawScreen(renderer.getPixelGrid());






        AnimationTimer emulatorLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (!listener.getInterrupted(0)) {

                    for (int i = 0; i < CYCLES_PER_FRAME; i++) {
                        int instruction = cpu.fetchInstruction();
                        cpu.decodeAndExecute(instruction);

                    }
                }   else if (listener.getInterrupted(0)) {
                    if (listener.getIterate(0)) {
                        int instruction = cpu.fetchInstruction();
                        cpu.decodeAndExecute(instruction);
                        System.out.println("Iterated");
                        listener.setIterate(0, false);
                    }
                }
                renderer.drawScreen(renderer.getPixelGrid());
            }
        };
        emulatorLoop.start();








        //Test suite





        //Main.renderer.drawExample();


    }

}
