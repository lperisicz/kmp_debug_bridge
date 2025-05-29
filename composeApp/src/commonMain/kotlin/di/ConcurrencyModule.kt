package di

import di.qualifiers.CoroutineScopeQualifier
import kotlinx.coroutines.CoroutineScope
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.prefs.Preferences

internal fun concurrencyModule(appScope: CoroutineScope) = module {

    single<CoroutineScope>(qualifier = named(CoroutineScopeQualifier.App)) {
        appScope
    }
}
