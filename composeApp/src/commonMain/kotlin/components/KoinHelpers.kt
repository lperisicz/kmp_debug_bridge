package components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import core.viewModel.BaseViewModel
import org.koin.compose.LocalKoinScope
import org.koin.compose.koinInject
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

@Composable
internal inline fun <reified T : BaseViewModel<*>> injectViewModel(
    qualifier: Qualifier? = null,
    scope: Scope = LocalKoinScope.current,
    noinline parameters: ParametersDefinition? = null,
): T {
    val instance: T = koinInject(qualifier, scope, parameters)
    DisposableEffect(instance) {
        onDispose {
            instance.close()
        }
    }
    return instance
}
