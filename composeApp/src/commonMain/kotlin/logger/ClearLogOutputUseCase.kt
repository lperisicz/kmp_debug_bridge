package logger

internal interface ClearLogOutputUseCase {

    operator fun invoke()
}

internal class ClearLogOutputUseCaseImpl(
    private val loggerStream: LoggerStream,
) : ClearLogOutputUseCase {

    override fun invoke() = loggerStream.clear()
}
