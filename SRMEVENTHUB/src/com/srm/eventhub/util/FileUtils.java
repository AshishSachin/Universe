package com.srm.eventhub.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    /**
     * Copies a selected file to a destination directory within the project.
     * @param sourceFile The file selected by the user.
     * @param destDirectory The destination directory (e.g., "uploads/qrcodes").
     * @return The relative path of the newly saved file, or null on failure.
     */
    public static String copyFileToProject(File sourceFile, String destDirectory) {
        try {
            // Create the destination directory if it doesn't exist
            Path destPath = Paths.get(destDirectory);
            if (!Files.exists(destPath)) {
                Files.createDirectories(destPath);
            }

            // Create a unique filename to avoid overwrites
            String originalFileName = sourceFile.getName();
            String fileExtension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                fileExtension = originalFileName.substring(i);
            }
            String newFileName = System.currentTimeMillis() + fileExtension;
            
            Path targetPath = destPath.resolve(newFileName);
            
            // Copy the file
            Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Return the relative path to store in the database
            return destDirectory + File.separator + newFileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
