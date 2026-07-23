package no.lodgaard.chipper;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import no.lodgaard.chipper.IO.KeyCodeListener;
import no.lodgaard.chipper.IO.Renderer;
import no.lodgaard.chipper.logic.CPU;
import no.lodgaard.chipper.logic.Memory;
import no.lodgaard.chipper.logic.RomLoader;

import java.io.FileNotFoundException;
import java.util.HexFormat;
import java.util.Scanner;


public class Main extends Application {


    public static final int width = 1024;
    public static final int height = 512;



    private static Memory memory;
    private static RomLoader romLoader;
    private static CPU cpu;
    private static Renderer renderer;



    @Override
    public void start(Stage stage) throws FileNotFoundException {


        Scanner sc = new Scanner(System.in);
        String romChoice = "";
        boolean correct = false;

        System.out.println("Select Rom:\n1) Chip-8-Logo\n2) IBM-Logo\n3) Corax+\n4) Flags");

        while (!correct) {
            if (!sc.hasNextInt()) {
                System.out.println("Please enter a number.");
                sc.next();
                continue;
            }
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    romChoice = "1-chip8-logo.ch8";
                    correct = true;
                    break;
                case 2:
                    romChoice = "2-ibm-logo.ch8";
                    correct = true;
                    break;
                case 3:
                    romChoice = "3-corax+.ch8";
                    correct = true;
                    break;
                case 4:
                    romChoice = "4-flags.ch8";
                    correct = true;
                    break;
                default:
                    System.out.println("Not a valid choice, be serious m8!");
                    break;
            }
        }

        System.out.println("You've chosen " + romChoice);




        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        renderer = new Renderer(gc, width, height);

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





        romLoader.loadRom("src/main/resources/" + romChoice);

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


    }

}
