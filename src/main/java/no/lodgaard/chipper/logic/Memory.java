package no.lodgaard.chipper.logic;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

//Chip-8 Memory has access to 4kb(4096) bytes
public class Memory {



    //Using byte type might be smart we'll see.
    private byte[] memoryArray = new byte[4096];



    private Deque<Byte> stack = new ArrayDeque<>();

	public Memory() {
	}

    //Pop, removes the two bytes at the top of the stack and returns them
    public byte[] popStack() {

        //size is set such, because the stack pops out two bytes instructions
        Byte[] byteWrapperBuffer = new Byte[2];

        //Does it twice to get both bytes
        byteWrapperBuffer[1] = stack.removeFirst();
        byteWrapperBuffer[0] = stack.removeFirst();

        byte[] finalInstruction = new byte[2];

        //Converts it back to primitive datatype
        for (int i = 0; i < byteWrapperBuffer.length; i++) {
            finalInstruction[i] = byteWrapperBuffer[i];
        }

        return finalInstruction;
    }



    public void pushStack(byte[] entry) {
        Byte[] byteWrapperBuffer = new Byte[entry.length];

        //Converts the primitive byte input to the class Byte for interactibility with the Stack
        //However does it inverted, such as when popping the bytes out they get put together the correct way
        for (int i = entry.length; i >= 0; i--) {
            byteWrapperBuffer[i] = Byte.valueOf(entry[i]);
        }

        //Pushes the bytes into the stack
        for (Byte b : byteWrapperBuffer) {
            stack.push(b);
        }


    }

    public void pushStack(int entry) {
        Byte[] byteWrapperBuffer = new Byte[2];

        byteWrapperBuffer[0] = (byte) ((entry >> 8) & 0xFF);
        byteWrapperBuffer[1] = (byte) (entry & 0xFF);

        for (Byte b : byteWrapperBuffer) {
            stack.push(b);
        }
    }


	//Index Register supports 12-bits of addresses which conveniently
	//Is 4096 addresses.
    public static int indexRegister(String addressString) {

        //Since memory array index is in base 10 the base 16 of
        //Chip-8 need to be translated to base 10.
        return Integer.parseInt(addressString, 16);
    }






	public byte[] getMemoryArray() {
		return this.memoryArray;
	}

	/*public void writeMemory(byte[] memoryArray) {

		this.memoryArray = memoryArray;
	}*/


}
