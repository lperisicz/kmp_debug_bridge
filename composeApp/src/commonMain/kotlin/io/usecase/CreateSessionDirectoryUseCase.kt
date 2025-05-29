package io.usecase

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import logger.Logger
import record.GetRecordingSessionUseCase
import record.RecordingSession
import java.nio.file.Files
import java.nio.file.Paths

internal interface CreateSessionDirectoryUseCase {

    suspend operator fun invoke()
}

internal class CreateSessionDirectoryUseCaseImpl(
    private val logger: Logger,
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
) : CreateSessionDirectoryUseCase {

    override suspend fun invoke(): Unit = coroutineScope {
        val session = getRecordingSessionUseCase().first()
        val path = Paths.get(getOutputPath(session))

        try {
            Files.createDirectories(path)
            logger.logI("Session directory created at path: $path")
        } catch (e: Exception) {
            logger.logE("Unable to create directory at: $path, caused by: ${e.message}")
            throw e
        }
    }
}

private fun getOutputPath(session: RecordingSession) = "${session.workspace}/${session.timestamp}"
