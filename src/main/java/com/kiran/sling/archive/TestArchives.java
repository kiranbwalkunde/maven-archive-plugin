package com.kiran.sling.archive;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.zip.*;

/**
 * The Archive Class to see how to create an Archive File with Java Programs.
 *
 * @author Kiran. Created on 5th Dec. 2018.
 */
public class TestArchives {

    public static void main(final String[] args) throws UnsupportedEncodingException, DataFormatException {
        System.out.println("The Program has been started...");
        // tryAdler();
        final byte[] bytes = tryDeflaterAndInflater();
        tryInflater(bytes);
        System.out.println("The Program has been exited. ");
    }

    private static void tryInflater(final byte[] bytes) throws DataFormatException {
        final Inflater inflater = new Inflater();

        inflater.setInput(bytes);
        final byte[] result = new byte[1024];
        while (!inflater.finished()) {
            final int readData = inflater.inflate(result);
            System.out.println("The Inflated Data is " + new String(result, 0, readData));
        }
        System.out.println("Done with the Inflating Process. ");
    }

    private static byte[] tryDeflaterAndInflater() {

        final Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        final String stringAsInput = "This is Kiran Baburao Walkunde";

        deflater.setInput(stringAsInput.getBytes(StandardCharsets.UTF_8));

        deflater.finish();
        final byte[] bytes = new byte[1024];
        while (!deflater.finished()) {
            final int deflatedSize = deflater.deflate(bytes);
            System.out.println(new String(bytes, 0, deflatedSize));
        }
        deflater.end();
        System.out.println("Done with the Deflation. ");
        return bytes;
    }

    private static void tryAdler() throws UnsupportedEncodingException {
        final String str = "HELLO";
        final byte[] bytes = str.getBytes(StandardCharsets.UTF_8);

        System.out.println("The Adler32 and CRC32 Checksum for " + str);

        // Compute Adler Checksum:
        final Adler32 adler32 = new Adler32();
        adler32.update(bytes);

        final long adlerChecksum = adler32.getValue();
        System.out.println("The Adler Checksum is " + adlerChecksum);

        final CRC32 crc32 = new CRC32();
        crc32.update(bytes);

        final long crcChecksum = crc32.getValue();
        System.out.println("The CRC Checksum " + crcChecksum);

    }
}
