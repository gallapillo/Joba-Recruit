package com.gallapillo.jobarecruit.presentation.items

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gallapillo.jobarecruit.presentation.theme.BorderColor
import com.gallapillo.jobarecruit.presentation.theme.HoverBackground
import com.gallapillo.jobarecruit.presentation.theme.fontFamily

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry?.destination?.route
                BottomNavigationItem(
                selected = selected,
                onClick = {
                    onItemClick(item)
                },
                selectedContentColor = HoverBackground,
                unselectedContentColor = BorderColor,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (item.badgeCount > 0) {
                            BadgedBox(
                                badge = {
                                    Text(text = item.badgeCount.toString())
                                }
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name
                                )
                            }
                        } else {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name
                            )
                        }
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            )
        }
    }
}