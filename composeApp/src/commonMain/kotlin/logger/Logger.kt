package logger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal interface Logger {

    fun logI(message: String)

    fun logE(message: String)
}

internal interface LoggerStream {

    fun logs(): Flow<String>

    fun clear()
}

private const val INITIAL_VALUE = "Started tracking logs:"

internal class LoggerImpl : Logger, LoggerStream {

    private val output = MutableStateFlow(INITIAL_VALUE)

    override fun logI(message: String) = output.append(LogType.INFO, message)

    override fun logE(message: String) = output.append(LogType.ERROR, message)

    override fun logs(): Flow<String> = output

    override fun clear() = output.update { INITIAL_VALUE }
}

private enum class LogType {
    INFO,
    ERROR;
}

private fun LogType.toPrefix() = when (this) {
    LogType.INFO -> "INFO: "
    LogType.ERROR -> "ERROR: "
}

private fun MutableStateFlow<String>.append(logType: LogType, message: String) =
    update { it.append(logType, message) }

private fun String.append(logType: LogType, message: String) =
    this + "\n" + logType.toPrefix() + message


