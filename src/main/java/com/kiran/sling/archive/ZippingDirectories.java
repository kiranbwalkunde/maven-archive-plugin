package com.kiran.sling.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The Class to Make the ZIP of the Directories.
 *
 * Reference: https://www.baeldung.com/java-compress-and-uncompress
 *
 * @author Shiva. Created on 07th Dec. 2018.
 */
public class ZippingDirectories {

    private static final String INPUT_DIR = "resources/packageRoot";

    private static final String OUTPUT_FILE_NAME = "output.zip";

    public static void main(final String[] args) throws IOException {

        final FileOutputStream fileOutputStream = new FileOutputStream(OUTPUT_FILE_NAME);
        final ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

        final File file = new File(INPUT_DIR);

        if (file.exists() && file.isDirectory()) {
            final File[] files = file.listFiles();
            for (final File inputFile: files) {
                System.out.println("The Input File is " + inputFile.getName());
                zipTheFile(inputFile, inputFile.getName(), zipOutputStream);
            }
            zipOutputStream.flush();
            zipOutputStream.close();
            fileOutputStream.close();
            System.out.println("Done with archiving process.");
        } else {
            System.err.println("Please check if the input DIR exists and has proper access rights. ");
        }
    }

    private static void zipTheFile(final File inputFile,
                                   final String fileName,
                                   ZipOutputStream zipOutputStream) throws IOException {
//        if (inputFile.isHidden()) {
//            return;
//        }
        if (inputFile.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOutputStream.putNextEntry(new ZipEntry(fileName));
                zipOutputStream.closeEntry();
            } else {
                zipOutputStream.putNextEntry(new ZipEntry(fileName + "/"));
                zipOutputStream.closeEntry();
            }

            final File[] childFiles = inputFile.listFiles();
            for (final File childFile: childFiles) {
                zipTheFile(childFile, fileName + "/" + childFile.getName(), zipOutputStream);
            }
            return;
        }
        final FileInputStream fileInputStream = new FileInputStream(inputFile);
        final ZipEntry zipEntry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(zipEntry);

        final byte[] bytes = new byte[1024];
        int length;

        while ((length = fileInputStream.read(bytes)) >=0 ) {
            zipOutputStream.write(bytes, 0, length);
        }
        fileInputStream.close();
    }
}
