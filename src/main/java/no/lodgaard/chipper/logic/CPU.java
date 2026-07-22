package no.lodgaard.chipper.logic;

import no.lodgaard.chipper.IO.Renderer;

import java.util.Arrays;
import java.util.HexFormat;

public class CPU {

    private Memory memory;

    private Renderer renderer;

    public CPU(Memory memory, Renderer renderer) {
        this.memory = memory;
        this.renderer = renderer;
    }

    //Fetches the two bytes necessary for an instruction and increments the PC +2
   public byte[] fetchInstruction(int programCounter) {

        byte[] fetchedByte = new byte[1];

        fetchedByte[0] = memory.getMemoryArray()[programCounter];
        fetchedByte[1] = memory.getMemoryArray()[programCounter + 1];

        memory.setProgramCounter(programCounter + 2);

        return fetchedByte;
    }

    public void decodeAndExecute(byte[] instruction) {

        String instructionToHex = HexFormat.of().formatHex(instruction);




        switch (instructionToHex) {
            case("00E0"):
                renderer.clearScreen();
            default:
                throw new RuntimeException("No such instruction as:" + Arrays.toString(instruction));

        }


    }

}
