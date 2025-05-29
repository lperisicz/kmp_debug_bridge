package adb.usecase

import externalcommand.use
import kotlinx.coroutines.flow.first
import record.GetRecordingSessionUseCase
import java.io.BufferedReader
import java.io.InputStreamReader

internal interface GetProcessIdUseCase {

    suspend operator fun invoke(): String?
}

internal class GetProcessIdUseCaseImpl(
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
) : GetProcessIdUseCase {

    override suspend fun invoke(): String? {
        val session = getRecordingSessionUseCase().first()
        val processId = ProcessBuilder("adb", "shell", "pidof", session.packageName)
            .redirectErrorStream(true)
            .use { process ->
                BufferedReader(InputStreamReader(process.inputStream)).readLine()
            }
        return processId
    }
}
