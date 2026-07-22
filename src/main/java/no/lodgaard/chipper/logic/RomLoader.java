package no.lodgaard.chipper.logic;

import java.io.*;
import java.sql.Array;
import java.util.HexFormat;
import java.util.Scanner;

public class RomLoader {

    private static final String fontString =
            "f0909090f02060202070f010f080f0f010f010f09090f01010f080f010f0f080f090f0f010204040f090f090f0f090f010f0f090f09090e090e090e0f0808080f0e0909090e0f080f080f0f080f08080";
            /*"0xF0", "0x90", "0x90", "0x90", "0xF0",
            "0x20", "0x60", "0x20", "0x20", "0x70",
            "0xF0", "0x10", "0xF0", "0x80", "0xF0",
            "0xF0", "0x10", "0xF0", "0x10", "0xF0",
            "0x90", "0x90", "0xF0", "0x10", "0x10",
            "0xF0", "0x80", "0xF0", "0x10", "0xF0",
            "0xF0", "0x80", "0xF0", "0x90", "0xF0",
            "0xF0", "0x10", "0x20", "0x40", "0x40",
            "0xF0", "0x90", "0xF0", "0x90", "0xF0",
            "0xF0", "0x90", "0xF0", "0x10", "0xF0",
            "0xF0", "0x90", "0xF0", "0x90", "0x90",
            "0xE0", "0x90", "0xE0", "0x90", "0xE0",

            "0xF0", "0x80", "0x80", "0x80", "0xF0",

            "0xE0", "0x90", "0x90", "0x90", "0xE0",

            "0xF0", "0x80", "0xF0", "0x80", "0xF0",
            "0xF0", "0x80", "0xF0", "0x80", "0x80";*/

    /*
    Parses font into bytes
     */
    private byte[] fontByte = HexFormat.of().parseHex(fontString);



    private Memory memory;

    public RomLoader(Memory memory) {
        this.memory = memory;
    }

    public void loadRom(String filePath) throws FileNotFoundException {

        byte[] bufferArray = new byte[3583];

        //Load font into memory
        System.arraycopy(fontByte, 0, memory.getMemoryArray(), 0, 80);
        System.out.println(fontByte.length);

        //Load Rom after Font load
        try {
            FileInputStream inputStream = new FileInputStream(filePath);

            //This reads everything into memory starting
            //at address 0x00 which would be wrong
            //as the first 0x200 bytes are reserved for the
            //font.
            inputStream.read(bufferArray);

            System.out.println(bufferArray.length);

            System.arraycopy(bufferArray, 0, memory.getMemoryArray(), 510, bufferArray.length);


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
