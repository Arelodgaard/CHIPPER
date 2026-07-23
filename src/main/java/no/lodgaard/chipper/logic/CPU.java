package no.lodgaard.chipper.logic;

import no.lodgaard.chipper.IO.Renderer;

import java.time.LocalTime;
import java.util.HexFormat;
import java.util.Random;

public class CPU {

    private Memory memory;

    private Renderer renderer;

    private int programCounter;

    private int indexRegister;

    private byte[] variableRegisters = new byte[16];

    private long lastTime;

    private int delayTimer = 0;
    private int soundTimer = 0;

    private boolean delayValueOver;
    private boolean soundValueOver;

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

    public void checkTimers() {





        final double ns = 1000000000.0 / 60.0;

        double delta = 0;

        if (delayValueOver) {
            long delayTimeNow = System.nanoTime();
            long sumDelay = delayTimeNow - lastTime;
            delayTimer -= (sumDelay > (long) 16666666.667 ? 1 : 0);
            lastTime = System.nanoTime();
        }

        if (soundValueOver) {
            long soundTimeNow = System.nanoTime();
            long sumSound = soundTimeNow - lastTime;
            delayTimer -= (sumSound > (long) 16666666.667 ? 1 : 0);
            lastTime = System.nanoTime();
        }



    }

    public void decodeAndExecute(int instruction) {

        System.out.println("Int instruction: " + Integer.toBinaryString(instruction));

        String instructionHexBuffer = HexFormat.of().toHexDigits(instruction);
        String instructionHex = instructionHexBuffer.substring(4);
        System.out.println("Hex instruction: " + instructionHex);
        System.out.println("IndexRegister: " + indexRegister);


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
                } else if (charX == '0' & charY == 'e' & charN == 'e'){
                    byte[] stackInstruction = memory.popStack();
                    //converting 12-bit back into an integer
                    setProgramCounter(((stackInstruction[0] & 0xFF) << 8) | (stackInstruction[1] & 0xFF));
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
                memory.pushStack(getProgramCounter());
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
            //(8XY) Logical and Arithmetic operations
            case('8'):
                eightOpcodes(charN, intX, intY);
                break;
            //(ANNN) Set Index: This sets the index register I to the value of NNN
            case('a'):
                indexRegister = intNNN;
                break;
            //(BNNN) Jump with offset: Sets PC to the address NNN plus v0
            case('b'):
                int BNNNv0 = variableRegisters[0] & 0xFF;
                setProgramCounter(intNNN + BNNNv0);
                break;
            //(CXNN) Random: Generates random number, binary AND with value NN and puts result in vX
            case('c'):
                Random r = new Random();
                int rand = r.nextInt(1000);
                int binaryAND = rand & intNN;
                variableRegisters[intX] = (byte) binaryAND;
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
            //(FX)
            case('f'):
                fOpcodes(stringNN, intX);
                break;
            //(DXYN) Display: Despair awaits
            case('d'):
                int posY = variableRegisters[intY] % 64;
                //Sets VF to 0;
                variableRegisters[15] = 0;
                for (int i = 0; i < intN; i++) {
                    int posX = variableRegisters[intX] % 64;
                    byte spriteData = memory.getMemoryArray()[indexRegister + i];
                    for (int j = 7; j >= 0; j--) {
                        int bit = (spriteData >> j) & 1;
                        switch (bit) {
                            case(0):
                                break;
                            case(1):
                                System.out.println("PosX passed to getPixelGridValue: " + posX);
                                System.out.println("PosY passed to getPixelGridValue: " + posY);
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
                        if (posX > 63) break;
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

    private void eightOpcodes(char charN, int intX, int intY) {
        //Checking fourthnibble
        switch (charN) {
            //(8XY0) Set: vX is set to vY
            case('0'):
                variableRegisters[intX] = variableRegisters[intY];
                break;
            //(8XY1) Binary OR: vX = vX OR vY
            case('1'):
                variableRegisters[intX] = (byte) (variableRegisters[intX] | variableRegisters[intY]);
                break;
            //(8XY2) Binary AND: vX = vX AND vY
            case('2'):
                variableRegisters[intX] = (byte) (variableRegisters[intX] & variableRegisters[intY]);
                break;
            //(8XY3) Logical XOR: vX = vX XOR vY
            case('3'):
                variableRegisters[intX] = (byte) (variableRegisters[intX] ^ variableRegisters[intY]);
                break;
            //(8XY4) Add: vX = vX + vY, if overflow set vF = 1 else vF = 0
            case('4'):
                int sum = (variableRegisters[intX] & 0xFF) + (variableRegisters[intY] & 0xFF);
                variableRegisters[intX] = (byte) sum;
                variableRegisters[15] = (byte) (sum > 255 ? 1 : 0);
                break;
                //(8XY5) Subtract: vX = vX - vY, if minuend >= subtrahend -> vF = 1 else vF = 0
            case('5'):
                if (variableRegisters[intX] >= variableRegisters[intY]) {
                    variableRegisters[intX] = (byte) (variableRegisters[intX] - variableRegisters[intY]);
                    variableRegisters[15] = 1;
                    break;
                } else {
                    variableRegisters[intX] = (byte) (variableRegisters[intX] - variableRegisters[intY]);
                    variableRegisters[15] = 0;
                    break;
                }


                //(8XY7) Subtract: vX = vY - vX, if minuend >= subtrahend -> vF = 1 else vF = 0
            case('7'):
                if (variableRegisters[intY] >= variableRegisters[intX]) {
                    variableRegisters[intX] = (byte) (variableRegisters[intY] - variableRegisters[intX]);
                    variableRegisters[15] = 1;
                    break;
                } else {
                    variableRegisters[intX] = (byte) (variableRegisters[intY] - variableRegisters[intX]);
                    variableRegisters[15] = 0;
                    break;
                }
                //(8XY6) Shift: Set vX = vY, then shift vX right then set vF = shiftedBit
            case('6'):
                int valueToShiftRight = variableRegisters[intY] & 0xFF;
                int shiftedOutBitRight = valueToShiftRight & 1;
                variableRegisters[intX] = (byte) (valueToShiftRight >> 1);
                variableRegisters[15] = (byte) shiftedOutBitRight;
                break;
            //(8XYE) Shift: Set vX = vY, then shift vX left then set vF = shiftedBit
            case('e'):
                int valueToShiftLeft = variableRegisters[intY] & 0xFF;
                int shiftedOutBitLeft = (valueToShiftLeft & 0x80) >> 7;
                variableRegisters[intX] = (byte) (valueToShiftLeft << 1);
                variableRegisters[15] = (byte) shiftedOutBitLeft;
                break;
            default:
                break;
        }
    }

    private void fOpcodes(String stringNN, int intX) {
        //Checks third and fourth nibble
        switch (stringNN) {
            //(FX1E) Add to index: I += vX, if indexregister overflows over 12-bit it sets vF = 1
            case("1e"):
                System.out.println("We got to FX1E");
                int sum = indexRegister + (variableRegisters[intX] & 0xFF);
                indexRegister = sum;
                variableRegisters[15] = (byte) (sum > 0xFFF ? 1 : 0);
                break;
            //(FX33) Binary-coded decimal conversion:
            case("33"):
                System.out.println("We got to FX33");
                int value = variableRegisters[intX] & 0xFF;
                memory.getMemoryArray()[indexRegister]     = (byte) (value / 100);
                memory.getMemoryArray()[indexRegister + 1] = (byte) ((value / 10) % 10);
                memory.getMemoryArray()[indexRegister + 2] = (byte) (value % 10);
                break;
            //(FX55) Store memory: from v0 -> vX into I -> I + X
            case("55"):
                System.out.println("We got to FX55");
                for (int i = 0; i <= intX; i++) {
                    memory.getMemoryArray()[indexRegister + i] = variableRegisters[i];
                }
                break;
            //(FX65) Load memory: from I -> I + X into v0 -> vX
            case("65"):
                for (int i = 0; i <= intX; i++) {
                    variableRegisters[i] = memory.getMemoryArray()[indexRegister + i];
                }
                break;
            default:
                break;
        }
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int location) {
        this.programCounter = location;
    }
}
