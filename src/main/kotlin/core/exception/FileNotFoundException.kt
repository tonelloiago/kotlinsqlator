package io.github.tonelloiago.core.exception

private const val FILE_NOT_FOUND = "File %s not found in resources."

class FileNotFoundException(fileName: String) : RuntimeException(String.format(FILE_NOT_FOUND, fileName))