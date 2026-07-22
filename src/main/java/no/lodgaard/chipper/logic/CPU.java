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

        System.out.println("Int instruction:" + Integer.toBinaryString(instruction));

        String instructionHexBuffer = HexFormat.of().toHexDigits(instruction);
        String instructionHex = instructionHexBuffer.substring(4);
        System.out.println(instructionHex);

        //FirstNibble
        char firstNibble = instructionHex.charAt(0);
        System.out.println("Firstnibble: " + firstNibble);
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
        System.out.println("StringNNN: " + Arrays.toString(byteNNN));
        int intNNN = HexFormat.of().fromHexDigits("" + charX + charY + charN);
        System.out.println("intNNN: " + intNNN);

        switch (firstNibble) {
            //(00E0) Clear Screen
            case('0'):
                if (charX == '0' & charY == 'e' & charN == '0') {
                    System.out.println("Opcode 00E0: Called");
                    renderer.clearScreen();
                    break;
                } else {
                    break;
                }



            //(1NNN)Jump: set the pc to nnn
            case('1'):
                System.out.println("OpCode: 1NNN");
                System.out.println("Supplied: " + instructionHex);
                System.out.println("intNNN: " + intNNN);
                setProgramCounter(intNNN - 80);

                System.out.println("PC:" + getProgramCounter());
                break;
            //(6XNN)Set: Simply set the register vx to the value nn.
            case('6'):
                System.out.println("OpCode: 6XNN");
                System.out.println("Set v" + intX + " = " + byteNN[0]);
                variableRegisters[intX] = byteNN[0];

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
                indexRegister = intNNN;
                System.out.println(indexRegister);
                System.out.println("Set I = " + indexRegister);
                break;
            //(DXYN) Display: Despair awaits
            case('d'):
                System.out.println("OpCode: DXYN");
                System.out.println("intN: " + intN);

                int posX = variableRegisters[intX] % 64;
                int posY = variableRegisters[intY] % 32;
                System.out.println("posX: " + variableRegisters[intX] + " posY: " + variableRegisters[intY]);
                //Sets VF to 0;
                variableRegisters[15] = 0;

                for (int i = 0; i < intN; i++) {
                    System.out.println("posX: " + posX + " posY: " + posY);
                    //look at how the indexregister is fetched
                    System.out.println("Indexregister: " + indexRegister);

                    byte spriteData = memory.getMemoryArray()[intN + indexRegister ];
                    System.out.println("Spritedata: " + Integer.toBinaryString(spriteData));
                    for (int j = 0; j < 8; j++) {
                        int bit = (spriteData >> (7 - j)) & 1;
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
