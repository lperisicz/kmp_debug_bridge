package logger

import kotlinx.coroutines.flow.Flow

internal interface GetLogOutputUseCase {

    suspend operator fun invoke(): Flow<String>
}

internal class GetLogOutputUseCaseImpl(
    private val loggerStream: LoggerStream,
): GetLogOutputUseCase {

    override suspend fun invoke(): Flow<String> = loggerStream.logs()
}
