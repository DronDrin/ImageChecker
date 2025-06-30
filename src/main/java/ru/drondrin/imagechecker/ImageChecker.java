package ru.drondrin.imagechecker;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Tag;
import com.drew.metadata.file.FileTypeDirectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class ImageChecker {
    public static void main(String[] args) throws IOException, ImageProcessingException {
        var scanner = new Scanner(System.in);
        System.out.print("Enter path: ");
        String rootPath = scanner.nextLine();

        rootPath = rootPath.replaceFirst("\\\\$", "");

        System.out.println("Image files with wrong extension:");
        boolean foundWrongFiles = false;
        try (var walkStream = Files.walk(Path.of(rootPath))) {
            for (var file : walkStream.map(Path::toFile).toList()) {
                String[] nameSplit = file.getName().split("\\.");
                String extension = nameSplit[nameSplit.length - 1];
                if ((extension.equals("jpg") || extension.equals("png")) &&
                        ImageMetadataReader.readMetadata(file)
                                .getFirstDirectoryOfType(FileTypeDirectory.class)
                                .getTags().stream().map(Tag::getDescription).map(String::toLowerCase)
                                .noneMatch(t -> t.equals(extension.toLowerCase()))) {
                    System.out.println(file.getAbsolutePath().replace(rootPath + "\\", ""));
                    foundWrongFiles = true;
                }
            }
        }
        if (!foundWrongFiles) {
            System.out.println("(none)");
        }
    }
}
