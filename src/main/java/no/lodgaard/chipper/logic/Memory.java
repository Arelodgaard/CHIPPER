package no.lodgaard.chipper.logic;


//Chip-8 Memory has access to 4kb(4096) bytes
public class Memory {



    //Using byte type might be smart we'll see.
    private byte[] memoryArray = new byte[4096];

    private int programCounter;

	public Memory() {

	}

	private void loadMemory() {

	}

	//Index Register supports 12-bits of addresses which conveniently
	//Is 4096 addresses.
    public static int indexRegister(String addressString) {

        //Since memory array index is in base 10 the base 16 of
        //Chip-8 need to be translated to base 10.
        return Integer.parseInt(addressString, 16);
    }



    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int pc) {



		this.programCounter = pc;
    }


	public byte[] getMemoryArray() {
		return this.memoryArray;
	}

	public void writeMemory(byte[] memoryArray) {

		this.memoryArray = memoryArray;
	}
}
