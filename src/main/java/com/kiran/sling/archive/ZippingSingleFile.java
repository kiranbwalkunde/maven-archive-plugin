package com.kiran.sling.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The Class is to Learn Zipping a Single File.
 *
 * @author Shiva. Created on 06th Dec. 2018.
 */
public class ZippingSingleFile {

    private static final String INPUT_FILE = "resources/packageRoot/jcr_root/apps/sadashiv/.content.xml";

    public static void main(final String[] args) throws IOException {
        final FileOutputStream fileOutputStream
                = new FileOutputStream("output.zip");
        final ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

        final File file = new File(INPUT_FILE);

        if (file.exists()) {
            System.out.println("The File already exists. ");
            final ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);

            final byte[] bytes = new byte[1024];
            final FileInputStream inputStream = new FileInputStream(file);
            int length = 0;
            while ((length = inputStream.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }
            inputStream.close();
        }
        zipOutputStream.flush();
        zipOutputStream.close();
        fileOutputStream.close();
    }
}
