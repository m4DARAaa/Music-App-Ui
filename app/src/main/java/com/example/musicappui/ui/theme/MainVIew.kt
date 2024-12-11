package com.example.musicappui.ui.theme

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicappui.AccountDialog
import com.example.musicappui.AccountView
import com.example.musicappui.BrowseView
import com.example.musicappui.HomeView
import com.example.musicappui.LibraryView
import com.example.musicappui.MainViewModel
import com.example.musicappui.R
import com.example.musicappui.Screen
import com.example.musicappui.Subscription
import com.example.musicappui.screensInBottom
import com.example.musicappui.screensInDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView() {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    val controller: NavController = rememberNavController()
    val navBackStateEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStateEntry?.destination?.route

    val title = remember {
        mutableStateOf(currentScreen.title)
    }

    val dialogOpen = remember {
        mutableStateOf(false)
    }

    val bottomBar: @Composable () -> Unit = {
        if (currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomScreen.Home) {
            BottomNavigation(modifier = Modifier.wrapContentSize()) {
                screensInBottom.forEach { item ->

                    val isSelected=currentRoute==item.bRoute
                    Log.d("Navigation","Item:${item.bRoute},CurrentRoute:$currentRoute,Is Selected")
                    val tint=if (isSelected) Color.White else Color.Black

                    BottomNavigationItem(
                        selected = currentRoute == item.bRoute,
                        onClick = { controller.navigate(item.bRoute) },
                        icon = {

                            Icon(
                                tint=tint,
                                painter = painterResource(id = item.icon),
                                contentDescription = item.bRoute
                            )
                        },
                        label = { Text(text = item.bRoute, color = tint) },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black

                    )
                }

            }
        }
        ModalBottomSheetLayout(sheetContent = {
            MoreBottomSheet(modifier =modifier )
        }) {
            
        }
    }


    Scaffold(
        bottomBar = bottomBar,
        topBar = {
            TopAppBar(title = { Text(text = title.value) }, navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Menu")
                }
            })
        }, scaffoldState = scaffoldState, drawerContent = {
            LazyColumn(Modifier.padding(16.dp)) {
                items(screensInDrawer) { item ->
                    DrawerItem(selected = currentRoute == item.dRoute, item = item) {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }

                        if (item.dRoute == "add_account") {
                            dialogOpen.value = true
                        } else {
                            controller.navigate(item.dRoute)
                            title.value = item.dTitle
                        }
                    }
                }
            }
        }) {
        com.example.musicappui.ui.theme.Navigation(
            navController = controller,
            viewModel = viewModel,
            pd = it
        )
        AccountDialog(dialogOpen = dialogOpen)
    }
}

@Composable
fun DrawerItem(
    selected: Boolean, item: Screen.DrawerScreen, onDrawerItemClicked: () -> Unit
) {
    val background = if (selected) Color.DarkGray else Color.White
    Row(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(background)
            .clickable {
                onDrawerItemClicked()
            }) {
        androidx.compose.material.Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.dTitle,
            Modifier.padding(end = 8.dp, top = 4.dp)
        )
        Text(
            text = item.dTitle, style = MaterialTheme.typography.headlineLarge
        )
    }
}
@Composable
fun MoreBottomSheet(modifier: Modifier){
    Box(modifier = Modifier
        .fillMaxWidth()
        .size(300.dp)
        .background(
            MaterialTheme.colorScheme.primaryContainer
        )
    ){
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row (modifier =Modifier.padding(16.dp) ){
                Icon(modifier=Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.baseline_settings_24) ,
                    contentDescription = "Settings")
                Text(text = "Settings", fontSize = 20.sp, color = Color.White)

            }

        }




    }

}



@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, pd: PaddingValues) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.DrawerScreen.Account.route, modifier = Modifier.padding(pd)
    )
    {
        composable(Screen.DrawerScreen.Account.route) {
            AccountView()
        }
        composable(Screen.DrawerScreen.Subscription.route) {
            Subscription()
        }
        composable(Screen.BottomScreen.Home.bRoute) {
            HomeView()
        }
        composable(Screen.BottomScreen.Library.bRoute) {
            LibraryView()
        }
        composable(Screen.BottomScreen.Browse.bRoute) {
            BrowseView()
        }

    }

}