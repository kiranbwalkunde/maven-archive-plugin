package com.kiran.sling.archive;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The Class to Create a Zip file.
 *
 * @author Shiva. Created on 05th Dec. 2018.
 */
public class CreatingZipFile {

    public static void main(final String[] args) throws FileNotFoundException, IOException {

        final String jcrRootPath = "resources/packageRoot";

        // Check if the package does exists or not.
        final File file = new File(jcrRootPath);
        if (file.exists()) {
            System.out.println("The File Does Exists ");
            final ZipOutputStream zipOutputStream = new ZipOutputStream(
                    new FileOutputStream("resources/output.zip")
            );
            zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);
            final ZipEntry zipEntry = new ZipEntry("resources/packageRoot/jcr_root");
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.closeEntry();
            zipOutputStream.close();

            System.out.println("The File has been closed. Please check the outcome.");
        } else {
            System.out.println("The File Does not exists. ");
        }
    }
}
