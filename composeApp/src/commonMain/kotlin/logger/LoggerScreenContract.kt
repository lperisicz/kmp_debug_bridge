package logger

import core.viewModel.BaseViewModel
import kotlinx.coroutines.flow.map

internal sealed class LoggerViewState {

    data class Logs(val output: String) : LoggerViewState() {

        companion object {

            val initial = Logs("")
        }
    }
}

internal abstract class LoggerViewModel : BaseViewModel<LoggerViewState>() {

    abstract fun clearOutput()
}

internal class LoggerViewModelImpl(
    private val getLogOutputUseCase: GetLogOutputUseCase,
    private val clearLogOutputUseCase: ClearLogOutputUseCase,
) : LoggerViewModel() {

    init {
        query {
            getLogOutputUseCase().map(LoggerViewState::Logs)
        }
    }

    override fun clearOutput() = clearLogOutputUseCase()
}
