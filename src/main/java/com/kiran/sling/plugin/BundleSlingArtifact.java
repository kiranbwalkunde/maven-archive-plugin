package com.kiran.sling.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * 
 * @phase process-sources
 */
@Mojo(name = "bundle")
public class BundleSlingArtifact extends AbstractMojo {

    @Parameter(name = "sourcePath")
    private String sourcePath;

    @Parameter(name = "artifactType")
    private String artifactType;

    @Parameter(name = "deploy", defaultValue = "false")
    private String deploy;

    @Parameter(name = "outputPath", defaultValue = "target")
    private String outputPath;

    @Parameter(name = "artifactName")
    private String artifactName;

    public void execute() {
        final Log log = getLog();
        if (sourcePath != null && !"".equals(sourcePath)) {
            final File file = new File(sourcePath);
            if (file.exists()) {
                final String outputFileName = outputPath + "/" + artifactName;
                if (checkAndCreateOutputDir()) {
                    getLog().info("The Output path is " + outputFileName);
                    try (final FileOutputStream fileOutputStream = new FileOutputStream(outputFileName);
                         final ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
                        final File[] files = file.listFiles();
                        for (final File inputFile: files) {
                            System.out.println("The Input File is " + inputFile.getName());
                            zipTheFile(inputFile, inputFile.getName(), zipOutputStream);
                        }

                    } catch (final IOException ioException) {
                        log.error("There is an error while zipping the file content", ioException);
                    }
                }
            } else {
                log.error("Input File does not exists, please check the same.");
            }
        }
    }

    private void zipTheFile(final File inputFile,
                                   final String fileName,
                                   ZipOutputStream zipOutputStream) throws IOException {
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

    private boolean checkAndCreateOutputDir() {
        boolean isCreated = false;
        final File file = new File(outputPath);
        if (file.exists()) {
            getLog().info("The Output Directory Exists. ");
            isCreated = true;
        } else {
            getLog().warn("The Output directory does not exists, trying to create...");
            isCreated = file.mkdir();
        }
        return isCreated;
    }
}
