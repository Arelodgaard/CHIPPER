package no.lodgaard.chipper.logic;

import java.io.*;
import java.util.Scanner;

public class RomLoader {

    private Memory memory;

    public RomLoader(Memory memory) {
        this.memory = memory;
    }

    private void loadRom(String filePath) throws FileNotFoundException {




        try {
            FileInputStream inputStream = new FileInputStream(filePath);

            //This reads everything into memory starting
            //at address 0x00 which would be wrong
            //as the first 0x200 bytes are reserved for the
            //font.
            inputStream.read(memory.getMemoryArray());


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
