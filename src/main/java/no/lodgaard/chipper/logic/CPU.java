package no.lodgaard.chipper.logic;

import no.lodgaard.chipper.IO.Renderer;

import java.util.Arrays;
import java.util.HexFormat;

public class CPU {

    private Memory memory;

    private Renderer renderer;

    private int programCounter;

    private byte[] indexRegister = new byte[2];

    private byte[] variableRegisters = new byte[16];

    public CPU(Memory memory, Renderer renderer) {
        this.memory = memory;
        this.renderer = renderer;
    }

    //Fetches the two bytes necessary for an instruction and increments the PC +2
   public byte[] fetchInstruction() {

        byte[] fetchedByte = new byte[2];

        fetchedByte[0] = memory.getMemoryArray()[programCounter];
        fetchedByte[1] = memory.getMemoryArray()[programCounter + 1];

        setProgramCounter(programCounter + 2);

        return fetchedByte;
    }



    public void decodeAndExecute(byte[] instruction) {

        String instructionToHex = HexFormat.of().formatHex(instruction);

        //FirstNibble
        char firstNibbleChar = instructionToHex.charAt(0);

        //X
        char charX = instructionToHex.charAt(1);
        byte[] byteX = HexFormat.of().parseHex(String.valueOf('0' + charX));
        int intX = (int) byteX[0];

        //Y
        char charY = instructionToHex.charAt(2);
        byte[] byteY = HexFormat.of().parseHex(String.valueOf('0' + charY));
        int intY = (int) byteY[0];

        //N: FourthNibble
        char charN = instructionToHex.charAt(3);
        byte[] byteN = HexFormat.of().parseHex(String.valueOf('0' + charN));
        int intN = (int) byteN[0];

        //NN: The Second Byte (Third and Fourth Nibbles)
        String stringNN = String.valueOf(charY + charN);
        byte[] byteNN = HexFormat.of().parseHex(stringNN);
        int intNN = (int) byteNN[0];

        //NNN: The second, third and fourth nibbles
        String stringNNN = String.valueOf(charX + charY + charN);
        byte[] byteNNN = HexFormat.of().parseHex(String.valueOf(charX + charY + '0' + charN));
        int intNNN = (int) byteNNN[0] + (int) byteNNN[1];

        switch (firstNibbleChar) {
            //(00E0) Clear Screen
            case('0'):
                renderer.clearScreen();
                break;

            //(1NNN)Jump: set the pc to nnn
            case('1'):
                setProgramCounter(intNNN);
                break;
            //(6XNN)Set: Simply set the register vx to the value nn.
            case('6'):
                variableRegisters[intX] = byteNN[0];
                break;
            //(7XNN)Add: Add the value nn to vx
            case('7'):
                variableRegisters[intX] += byteNN[0];
                break;
            //(ANNN) Set Index: This sets the index register I to the value of NNN
            case('A'):
                indexRegister[0] = byteNNN[0];
                indexRegister[1] = byteNNN[1];
                break;
            //(DXYN) Display: Despair awaits
            case('D'):
                break;
            default:
                throw new RuntimeException("No such instruction as:" + Arrays.toString(instruction));

        }


    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int location) {
        this.programCounter = location;
    }


}
