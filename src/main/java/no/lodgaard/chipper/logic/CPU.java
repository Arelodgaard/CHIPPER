package no.lodgaard.chipper.logic;

import no.lodgaard.chipper.IO.Display;

import java.util.Arrays;
import java.util.HexFormat;

public class CPU {

    private Memory memory;

    private Display display;

    public CPU(Memory memory, Display display) {
        this.memory = memory;
        this.display = display;
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

        char firstNibble = instructionToHex.charAt(0);
        char secondNibble = instructionToHex.charAt(1);
        char thirdNibble = instructionToHex.charAt(2);
        char fourthNibble = instructionToHex.charAt(3);


        switch (firstNibble) {
            case '0':
                switch (secondNibble) {
                    case '0':
                        switch (thirdNibble) {
                            case 'E':
                                switch (fourthNibble) {
                                    case '0':
                                        // Clear Screen
                                        break;
                                }
                                break;
                        }
                        break;
                }
                break;
            case '1':
                break;
            case '6':
                break;
            case 'a':
                break;
            case 'D':
                break;
            default:
                throw new RuntimeException("No such instruction as:" + Arrays.toString(instruction));

        }


    }

}
