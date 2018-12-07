package com.kiran.sling.archive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * The Deflater / Inflater Test.
 *
 * @author Shiva. Created on 05th Dec. 2018.
 */
public class DeflatorInflatorTest {

    public static void main(final String[] args) throws IOException, DataFormatException {

        final String input = "Hello World";

        final byte[] uncompressedData = input.getBytes(StandardCharsets.UTF_8);
        final byte[] decoded = compress(uncompressedData, Deflater.BEST_COMPRESSION, true);
        final byte[] plain = decompress(decoded, true);
        System.out.println("The Decrypted String is " + new String(plain));
    }

    private static byte[] compress(final byte[] input,
                                   final int compressionLevel,
                                   final boolean gzipFormat) throws IOException {

        // Create the Deflater Object to compress the data.
        final Deflater compressor = new Deflater(compressionLevel, gzipFormat);
        compressor.setInput(input);

        compressor.finish();

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] byteBuffer = new byte[1024];

        while (!compressor.finished()) {
            final int readCount = compressor.deflate(byteBuffer);
            if (readCount > 0) {
                byteArrayOutputStream.write(byteBuffer, 0, readCount);
            }
        }
        compressor.end();

        return byteArrayOutputStream.toByteArray();
    }

    private static byte[] decompress(final byte[] input, final boolean gzipFormat) throws DataFormatException {

        final Inflater inflater = new Inflater(gzipFormat);
        inflater.setInput(input);

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] byteBuffer = new byte[1024];

        while (!inflater.finished()) {
            final int readCount = inflater.inflate(byteBuffer);
            if (readCount > 0) {
                byteArrayOutputStream.write(byteBuffer, 0, readCount);
            }
        }

        return byteArrayOutputStream.toByteArray();
    }
}
