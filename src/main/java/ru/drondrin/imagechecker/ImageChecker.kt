package ru.drondrin.imagechecker

import com.drew.imaging.ImageMetadataReader.readMetadata
import com.drew.metadata.file.FileTypeDirectory
import java.io.File

fun main() {
    print("Enter path: ")
    File(readln()).walk().toList().filter { f ->
        f.extension in listOf("png", "jpg") && f.extension !in readMetadata(f)
            .getFirstDirectoryOfType(FileTypeDirectory::class.java).tags.map { it.description }
    }.map { it.name }.apply { println("Image files with wrong extension: ${if (isEmpty()) "\n(none)" else ""}") }
        .forEach(::println)
}
