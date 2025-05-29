package picker

import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual suspend fun chooseDirectory(): String? =
    suspendCoroutine { continuation ->
        try {
            SwingUtilities.invokeLater {
                val fileChooser = JFileChooser()
                fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                val result = fileChooser.showOpenDialog(null)
                val path = if (result == JFileChooser.APPROVE_OPTION) {
                    fileChooser.selectedFile.absolutePath
                } else {
                    null
                }
                continuation.resume(path)
            }
        } catch (e: Exception) {
            continuation.resumeWithException(e)
            return@suspendCoroutine
        }
    }

actual suspend fun chooseFilePath(description: String, vararg extensions: String): String? =
    suspendCoroutine { continuation ->
        try {
            SwingUtilities.invokeLater {
                val fileChooser = JFileChooser()
                fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
                fileChooser.fileFilter = FileNameExtensionFilter(description, *extensions)
                val result = fileChooser.showOpenDialog(null)
                val path = if (result == JFileChooser.APPROVE_OPTION) {
                    fileChooser.selectedFile.absolutePath
                } else {
                    null
                }
                continuation.resume(path)
            }
        } catch (e: Exception) {
            continuation.resumeWithException(e)
            return@suspendCoroutine
        }
    }
