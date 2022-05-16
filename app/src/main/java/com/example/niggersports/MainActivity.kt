package com.example.niggersports

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.niggersports.ui.theme.NiggerSportsTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            NiggerSportsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
                                CenterAlignedTopAppBar(
                                    modifier = Modifier.systemBarsPadding(),
                                    title = { Text(viewModel.currentScreen.title) },
                                    colors = smallTopAppBarColors(
                                        containerColor = Color.Transparent,
                                        scrolledContainerColor = Color.Transparent
                                    )
                                )
                            }
                        },
                        bottomBar = {
                            NavigationBar(Modifier.navigationBarsPadding()) {
                                listOf(
                                    Screen.News,
                                    Screen.Stats,
                                    Screen.Matches,
                                    Screen.Players
                                ).forEach {
                                    val selected = viewModel.currentScreen == it
                                    NavigationBarItem(
                                        alwaysShowLabel = false,
                                        selected = selected,
                                        onClick = { viewModel.currentScreen = it },
                                        icon = {
                                            Icon(
                                                if (selected) it.selectedIcon else it.icon,
                                                null
                                            )
                                        },
                                        label = { Text(it.title) }
                                    )
                                }
                            }
                        }
                    ) { padding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            AnimatedContent(targetState = viewModel.currentScreen) { screen ->
                                when (screen) {
                                    Screen.Stats -> {
                                        GamesScreen()
                                    }
                                    Screen.News -> {
                                        HomeScreen()
                                    }
                                    Screen.Matches -> {
                                        CockScreen()
                                    }
                                    Screen.Players -> {

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class MainViewModel : ViewModel() {
    var currentScreen by mutableStateOf<Screen>(Screen.News)

}

class HomeViewModel : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    fun fetch() {
        flowOf(1, 2, 3).onEach {
            _state.value = HomeState(loading = true)
        }.launchIn(viewModelScope)
    }

}

sealed class Action<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Action<T>(data)
    class Success<T>(data: T) : Action<T>(data)
    class Error<T>(message: String?) : Action<T>(message = message)
    class Empty<T> : Action<T>()
}

data class HomeState(
    val loading: Boolean = false
)


sealed class Screen(val title: String, val icon: ImageVector, val selectedIcon: ImageVector) {
    object News : Screen("News", Icons.Outlined.NewReleases, Icons.Filled.NewReleases)
    object Stats : Screen("Stats", Icons.Outlined.TableChart, Icons.Filled.TableChart)
    object Matches :
        Screen("Matches", Icons.Outlined.SportsBasketball, Icons.Filled.SportsBasketball)

    object Players :
        Screen("Players", Icons.Outlined.SupervisedUserCircle, Icons.Filled.SupervisedUserCircle)
}


@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    Surface(Modifier.fillMaxSize(), color = Color.Cyan) {}
}

@Composable
fun GamesScreen() {
    Surface(Modifier.fillMaxSize(), color = Color.Red) {}
}

@Composable
fun CockScreen() {
    Surface(Modifier.fillMaxSize(), color = Color.Green) {}
}

interface Repository {


}
