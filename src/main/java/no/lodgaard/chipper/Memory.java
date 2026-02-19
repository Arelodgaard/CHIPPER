package no.lodgaard.chipper;


//Chip-8 Memory has access to 4kb(4096) bytes
public class Memory {

    //Using byte type might be smart we'll see.
    private byte[] memoryArray = new byte[4096];

    private int programCounter;

    public int indexRegister(String addresString) {

        //Since memory array index is in base 10 the base 16 of
        //Chip-8 need to be translated to base 10.
        return Integer.parseInt(addresString, 16);
    }



    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int pc) {
        this.programCounter = pc;
    }


}
