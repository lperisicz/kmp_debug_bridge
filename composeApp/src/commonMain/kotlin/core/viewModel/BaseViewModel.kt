package core.viewModel

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.io.Closeable

internal abstract class BaseViewModel<T> : Closeable {

    private val viewModelScope = CoroutineScope(
        Dispatchers.Default +
                SupervisorJob() +
                CoroutineExceptionHandler { coroutineContext, throwable ->
                    println("Exception in ${this::class.java} viewModelScope[$coroutineContext]: $throwable")
                }
    )
    private val viewState = MutableSharedFlow<T>(replay = 10)

    protected fun runCommand(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch(block = block)

    protected fun query(block: suspend CoroutineScope.() -> Flow<T>) {
        viewModelScope.launch {
            block().collect(viewState)
        }
    }

    override fun close() = viewModelScope.cancel("Closing ViewModel")

    inline fun <reified T> viewState(): Flow<T> = viewState.filterNotNull().filterIsInstance()
}
