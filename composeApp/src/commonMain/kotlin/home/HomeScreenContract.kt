package home

import core.viewModel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal sealed class HomeViewState {

    data class ContentSection(val screen: Screen) : HomeViewState() {

        companion object {
            val initial = ContentSection(screen = Screen.initialValue())
        }
    }

    data class NavigationSection(val navItems: List<NavigationItem>) : HomeViewState() {

        companion object {
            val initial = NavigationSection(navItems = emptyList())
        }
    }

    data class NavigationItem(
        val screen: Screen,
        val isSelected: Boolean,
    )

    enum class Screen {
        Recording,
        Install,
        Settings;

        companion object {

            fun initialValue(): Screen = Screen.entries.first()
        }
    }
}

internal abstract class HomeViewModel : BaseViewModel<HomeViewState>() {

    abstract fun selectScreen(screen: HomeViewState.Screen)
}

internal class HomeViewModelImpl : HomeViewModel() {

    private val currentScreen = MutableStateFlow(HomeViewState.Screen.initialValue())

    init {
        query { currentScreen.mapToContentSection() }

        query { currentScreen.mapToNavigationSection() }
    }

    override fun selectScreen(screen: HomeViewState.Screen) = currentScreen.update { screen }
}

private fun Flow<HomeViewState.Screen>.mapToContentSection(): Flow<HomeViewState.ContentSection> =
    map { currentScreen ->
        HomeViewState.ContentSection(
            screen = currentScreen
        )
    }

private fun Flow<HomeViewState.Screen>.mapToNavigationSection(): Flow<HomeViewState.NavigationSection> =
    map { currentScreen ->
        HomeViewState.NavigationSection(
            navItems = HomeViewState.Screen.entries.map { screen ->
                HomeViewState.NavigationItem(
                    screen = screen,
                    isSelected = screen == currentScreen
                )
            }
        )
    }
