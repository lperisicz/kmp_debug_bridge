package picker

import java.awt.Desktop
import java.io.File

expect suspend fun chooseDirectory(): String?

expect suspend fun chooseFilePath(description: String, vararg extensions: String): String?

fun showFile(path: String) = Desktop.getDesktop().browseFileDirectory(File(path))
