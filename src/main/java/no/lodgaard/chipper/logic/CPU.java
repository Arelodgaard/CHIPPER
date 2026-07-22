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
        byte[] byteX = HexFormat.of().parseHex("" + '0' + charX);
        int intX = byteX[0];

        //Y
        char charY = instructionToHex.charAt(2);
        byte[] byteY = HexFormat.of().parseHex("" + '0' + charY);
        int intY = byteY[0];

        //N: FourthNibble
        char charN = instructionToHex.charAt(3);
        byte[] byteN = HexFormat.of().parseHex("" + '0' + charN);
        int intN = byteN[0];

        //NN: The Second Byte (Third and Fourth Nibbles)
        String stringNN = "" + charY + charN;
        byte[] byteNN = HexFormat.of().parseHex(stringNN);
        int intNN = byteNN[0];

        //NNN: The second, third and fourth nibbles
        String stringNNN = String.valueOf(charX + charY + charN);
        byte[] byteNNN = HexFormat.of().parseHex("" + charX + charY + '0' + charN);
        int intNNN = ((byteNNN[0] & 0xFF) << 4) | (byteNNN[1] & 0x0F);

        switch (firstNibbleChar) {
            //(00E0) Clear Screen
            case('0'):
                System.out.println("OpCode: 00E0");
                renderer.clearScreen();
                break;

            //(1NNN)Jump: set the pc to nnn
            case('1'):
                System.out.println("OpCode: 1NNN");
                setProgramCounter(intNNN);
                System.out.println("PC:" + getProgramCounter());
                break;
            //(6XNN)Set: Simply set the register vx to the value nn.
            case('6'):
                System.out.println("OpCode: 6XNN");
                variableRegisters[intX] = byteNN[0];
                System.out.println("Set v" + intX + " = " + byteNN[0]);
                break;
            //(7XNN)Add: Add the value nn to vx
            case('7'):
                System.out.println("OpCode: 7XNN");
                variableRegisters[intX] += byteNN[0];
                System.out.println("Added " + byteNN[0] + " to v" + intX);
                break;
            //(ANNN) Set Index: This sets the index register I to the value of NNN
            case('a'):
                System.out.println("OpCode: ANNN");
                indexRegister[0] = byteNNN[0];
                indexRegister[1] = byteNNN[1];
                System.out.println("Set I = " + indexRegister[0] + indexRegister[1]);
                break;
            //(DXYN) Display: Despair awaits
            case('d'):
                System.out.println("OpCode: DXYN");
                int posX = variableRegisters[intX] - 1;
                int posY = variableRegisters[intY] - 1;

                //Sets VF to 0;
                variableRegisters[15] = 0;

                for (int i = 0; i < intN; i++) {
                    System.out.println("posX: " + posX + " posY: " + posY);
                    //look at how the indexregister is fetched
                    byte spriteData = memory.getMemoryArray()[indexRegister[0] + indexRegister[1] + 511 + intN];

                    for (int j = 7; j >= 0; j--) {
                        int bit = (spriteData >> j) & 1;
                        switch (bit) {
                            case(0):
                                break;
                            case(1):
                                if (renderer.getPixelGrid()[posX][posY] == 0) {
                                    renderer.flipPixel(posX, posY);
                                } else if (renderer.getPixelGrid()[posX][posY] == 1) {
                                    renderer.flipPixel(posX, posY);
                                    variableRegisters[15] = 1;
                                }
                                break;
                            default:
                                break;
                        }
                        if (posX >= 63) break;
                        posX++;
                    }

                    posY++;

                    if (posY >= 31) break;
                }

                renderer.drawScreen(renderer.getPixelGrid());
                break;
            default:
                throw new RuntimeException("No such instruction as:" + instructionToHex);

        }


    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int location) {
        this.programCounter = location;
    }


}
