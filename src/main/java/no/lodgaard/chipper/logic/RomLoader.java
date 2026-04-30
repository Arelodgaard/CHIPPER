package no.lodgaard.chipper.logic;

import java.io.*;
import java.sql.Array;
import java.util.Scanner;

public class RomLoader {

    private Memory memory;

    public RomLoader(Memory memory) {
        this.memory = memory;
    }

    public void loadRom(String filePath) throws FileNotFoundException {

        byte[] bufferArray = new byte[3584];

        //Load font into memory
        //System.arraycopy(memory.font, 0, memory.getMemoryArray(), 0, memory.font.length);


        //Load Rom after Font load
        try {
            FileInputStream inputStream = new FileInputStream(filePath);

            //This reads everything into memory starting
            //at address 0x00 which would be wrong
            //as the first 0x200 bytes are reserved for the
            //font.
            inputStream.read(bufferArray);

            System.out.println(bufferArray.length);

            System.arraycopy(bufferArray, 0, memory.getMemoryArray(), 512, bufferArray.length);


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
