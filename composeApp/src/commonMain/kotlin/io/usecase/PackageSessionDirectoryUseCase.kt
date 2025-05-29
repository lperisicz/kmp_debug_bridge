package io.usecase

import kotlinx.coroutines.flow.first
import logger.Logger
import notification.NotificationData
import notification.PublishNotificationUseCase
import record.GetRecordingSessionUseCase
import record.RecordingSession
import java.io.File
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

internal interface PackageSessionDirectoryUseCase {

    suspend operator fun invoke()
}

internal class PackageSessionDirectoryUseCaseImpl(
    private val logger: Logger,
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
    private val publishNotificationUseCase: PublishNotificationUseCase,
) : PackageSessionDirectoryUseCase {

    override suspend fun invoke() {
        val session = getRecordingSessionUseCase().first()
        val sessionDirectory = File(getInputPath(session))
        val outputFile = File(getOutputPath(session))

        outputFile
            .outputStream()
            .use { fileOutputStream ->
                fileOutputStream
                    .zipOutputStream()
                    .use { zipOutputStream ->
                        sessionDirectory
                            .walkTopDown()
                            .forEach { directoryChild ->
                                directoryChild
                                    .takeIf { it.isDirectory.not() }
                                    ?.inputStream()
                                    ?.use { fileInputStream ->
                                        val zipEntryName = directoryChild.relativeTo(sessionDirectory).path
                                        val zipEntry = ZipEntry(zipEntryName)
                                        zipOutputStream.putNextEntry(zipEntry)
                                        fileInputStream.copyTo(zipOutputStream)
                                        zipOutputStream.closeEntry()
                                    }
                            }
                    }
            }

        publishNotificationUseCase(
            notificationData = NotificationData(
                message = "Recording session saved",
                action = NotificationData.Action.ShowInDirectory(filePath = getOutputPath(session))
            )
        )

        logger.logI("Session directory successfully compressed to ${getOutputPath(session)}")
    }
}

private fun getInputPath(session: RecordingSession) = "${session.workspace}/${session.timestamp}"

private fun getOutputPath(session: RecordingSession) = "${getInputPath(session)}.zip"

private fun OutputStream.zipOutputStream() = ZipOutputStream(this)
