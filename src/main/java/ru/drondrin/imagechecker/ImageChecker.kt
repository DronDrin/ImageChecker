package ru.drondrin.imagechecker

import com.drew.imaging.ImageMetadataReader.readMetadata
import com.drew.metadata.Tag
import com.drew.metadata.file.FileTypeDirectory
import java.io.File

fun main() {
    print("Enter path: ")
    readLine()?.run { replaceFirst("\\\\$", "") }?.run {
        File(this).walk().toList().filter {
            it.extension in listOf("png", "jpg") && it.extension !in readMetadata(it)
                .getFirstDirectoryOfType(FileTypeDirectory::class.java).tags.map(Tag::getDescription)
        }.apply { println("Image files with wrong extension:" + (if (isEmpty()) "\n(none)" else "")) }
            .forEach { println(it.name.replace(this, "")) }
    }
}
