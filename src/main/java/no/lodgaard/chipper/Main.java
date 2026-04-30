package no.lodgaard.chipper;


import javafx.application.Application;
import no.lodgaard.chipper.IO.Display;
import no.lodgaard.chipper.logic.CPU;
import no.lodgaard.chipper.logic.Memory;
import no.lodgaard.chipper.logic.RomLoader;

import java.io.FileNotFoundException;


public class Main{


    private static Memory memory;
    private static RomLoader romLoader;
    private static CPU cpu;
    private static Display display;


    static void main(String[] args) throws FileNotFoundException {

        display = new Display();
        Application.launch(Display.class, args);

        //Initializing the different components(objects)
        memory = new Memory();
        romLoader = new RomLoader(memory);
        romLoader.loadRom("src/main/resources/1-chip8-logo.ch8");

        cpu = new CPU(memory, display);


        //Test suite
        for (byte b : memory.getMemoryArray()) {
            System.out.println(Byte.toString(b));
        }

        //display.drawExample();

        display.drawPixel(20, 40);


        System.out.println(memory.getMemoryArray().length);











    }



}
