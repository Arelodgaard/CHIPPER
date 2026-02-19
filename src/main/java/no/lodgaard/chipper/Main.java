package no.lodgaard.chipper;


import javafx.application.Application;


public class Main{
    static void main() {

        Memory memory = new Memory();

        System.out.println(Memory.indexRegister("1ff"));

        Application.launch(Display.class);




    }



}
