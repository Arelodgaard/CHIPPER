package no.lodgaard.chipper.logic;

public class CPU {

    private Memory memory;

    public CPU(Memory memory) {
        this.memory = memory;
    }

    //Fetches the two bytes necessary for an instruction and increments the PC +2
   private byte[] fetchInstruction(int programCounter) {

        byte[] fetchedByte = new byte[1];

        fetchedByte[0] = memory.getMemoryArray()[programCounter];
        fetchedByte[1] = memory.getMemoryArray()[programCounter + 1];

        memory.setProgramCounter(programCounter + 2);

        return fetchedByte;
    }

}
