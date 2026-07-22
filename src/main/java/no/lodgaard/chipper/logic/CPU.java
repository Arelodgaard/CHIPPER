package no.lodgaard.chipper.logic;

import no.lodgaard.chipper.IO.Renderer;

import java.util.Arrays;
import java.util.HexFormat;

public class CPU {

    private Memory memory;

    private Renderer renderer;

    private int programCounter;

    private int indexRegister;

    private byte[] variableRegisters = new byte[16];

    public CPU(Memory memory, Renderer renderer) {
        this.memory = memory;
        this.renderer = renderer;
    }

    //Fetches the two bytes necessary for an instruction and increments the PC +2
   public int fetchInstruction() {

        byte[] fetchedByte = new byte[2];

        fetchedByte[0] = memory.getMemoryArray()[programCounter];
        fetchedByte[1] = memory.getMemoryArray()[programCounter + 1];

        System.out.println("PC before increment: " + getProgramCounter());
        setProgramCounter(programCounter + 2);
        System.out.println("PC after increment: " + getProgramCounter());



        return ((fetchedByte[0] & 0xFF) << 8) | (fetchedByte[1] & 0xFF);
    }



    public void decodeAndExecute(int instruction) {

        System.out.println("Int instruction: " + Integer.toBinaryString(instruction));

        String instructionHexBuffer = HexFormat.of().toHexDigits(instruction);
        String instructionHex = instructionHexBuffer.substring(4);
        System.out.println("Hex instruction: " + instructionHex);

        //FirstNibble
        char firstNibble = instructionHex.charAt(0);

        //X
        char charX = instructionHex.charAt(1);
        byte[] byteX = HexFormat.of().parseHex("" + '0' + charX);
        int intX = byteX[0];

        //Y
        char charY = instructionHex.charAt(2);
        byte[] byteY = HexFormat.of().parseHex("" + '0' + charY);
        int intY = byteY[0];

        //N: FourthNibble
        char charN = instructionHex.charAt(3);
        byte[] byteN = HexFormat.of().parseHex("" + '0' + charN);
        int intN = byteN[0];

        //NN: The Second Byte (Third and Fourth Nibbles)
        String stringNN = "" + charY + charN;
        byte[] byteNN = HexFormat.of().parseHex(stringNN);
        int intNN = byteNN[0];

        //NNN: The second, third and fourth nibbles
        String stringNNN = String.valueOf(charX + charY + charN);
        byte[] byteNNN = HexFormat.of().parseHex("" + charX + charY + '0' + charN);
        int intNNN = HexFormat.of().fromHexDigits("" + charX + charY + charN);

        switch (firstNibble) {
            //(00E0) Clear Screen
            case('0'):
                if (charX == '0' & charY == 'e' & charN == '0') {
                    renderer.clearScreen();
                    break;
                } else {
                    break;
                }



            //(1NNN)Jump: set the pc to nnn
            case('1'):
                setProgramCounter(intNNN);
                break;
            //(2NNN)Call: call subroutine at NNN
            case('2'):
                memory.pushStack();
            //(6XNN)Set: Simply set the register vx to the value nn.
            case('6'):
                variableRegisters[intX] = byteNN[0];

                break;
            //(7XNN)Add: Add the value nn to vx
            case('7'):
                variableRegisters[intX] += byteNN[0];
                break;
            //(ANNN) Set Index: This sets the index register I to the value of NNN
            case('a'):
                indexRegister = intNNN;
                break;
            //(3XNN) Skip instruction if vX == NN
            case('3'):
                if (variableRegisters[intX] == byteNN[0]) {
                    programCounter += 2;
                }
                break;
            //(4XNN) Skip instruction if vX != NN
            case('4'):
                if (variableRegisters[intX] != byteNN[0]) {
                    programCounter += 2;
                }
                break;
            //(5XY0) Skip instruction if vX == vY
            case('5'):
                if (variableRegisters[intX] == variableRegisters[intY]) {
                    programCounter += 2;
                }
                break;
            //(9XY0) Skip instruction if vX != vY
            case('9'):
                if (variableRegisters[intX] != variableRegisters[intY]) {
                    programCounter += 2;
                }
                break;
            //(DXYN) Display: Despair awaits
            case('d'):

                int posX = variableRegisters[intX] % 64;
                int posY = variableRegisters[intY] % 64;

                //Sets VF to 0;
                variableRegisters[15] = 0;

                for (int i = 0; i < intN; i++) {
                    posX = variableRegisters[intX] % 64;

                    byte spriteData = memory.getMemoryArray()[indexRegister + i];

                    for (int j = 7; j >= 0; j--) {
                        int bit = (spriteData >> j) & 1;
                        switch (bit) {
                            case(0):
                                break;
                            case(1):
                                if (renderer.getPixelGridValue(posX, posY) == 0) {
                                    renderer.flipPixel(posX, posY);
                                } else if (renderer.getPixelGridValue(posX, posY) == 1) {
                                    renderer.flipPixel(posX, posY);
                                    variableRegisters[15] = 1;
                                }
                                break;
                            default:
                                break;
                        }
                        if (posX >= 64) break;
                        posX++;
                    }

                    posY++;

                    if (posY >= 32) break;
                }

                break;
            default:
                throw new RuntimeException("No such instruction as:" + instructionHex);

        }


    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int location) {



        this.programCounter = location;
    }


}
