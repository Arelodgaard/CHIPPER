package no.lodgaard.chipper;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import no.lodgaard.chipper.IO.Display;
import no.lodgaard.chipper.logic.CPU;
import no.lodgaard.chipper.logic.Memory;
import no.lodgaard.chipper.logic.RomLoader;

import java.io.FileNotFoundException;



public class DisplayLaunch extends Application {

    private static Stage stage;

    public static final int width = 1024;
    public static final int height = 512;



    private static Memory memory;
    private static RomLoader romLoader;
    private static CPU cpu;
    private static Display display;

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage stage) throws FileNotFoundException {



        memory = new Memory();
        display = new Display();

        romLoader = new RomLoader(memory);
        romLoader.loadRom("src/main/resources/1-chip8-logo.ch8");

        cpu = new CPU(memory, display);


        Rectangle rectangle = new Rectangle();
        rectangle.setX(200);
        rectangle.setY(200);
        rectangle.setWidth(300);
        rectangle.setHeight(400);
        rectangle.setStroke(Color.TRANSPARENT);
        rectangle.setFill(Color.valueOf("#00ffff"));

        // Add canvas to the scene
        Group root = new Group();
        root.getChildren().add(rectangle);



        Scene scene = new Scene(root, width, height);
        stage.setTitle("CHIPPER v.0.0.1");
        stage.setScene(scene);
        stage.show();


        display.drawPixel(20, 40);




        //Test suite

        for (byte b : memory.getMemoryArray()) {
            System.out.println(Byte.toString(b));
        }

        System.out.println(memory.getMemoryArray().length);

        display.drawExample();


    }

}
