package com.margarin.commonweather.presentation.screens.citylist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.margarin.commonweather.search.City
import com.margarin.search.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListContent(component: CityListComponent) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.manage_cities),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp),
            contentAlignment = (Alignment.TopCenter)
        ) {

                    Column {
                        ButtonSearch(onButtonSearchClickListener = {})
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = stringResource(id = R.string.list_is_empty))
                    }

                }






    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun CitiesLazyColumn(

    ) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            ButtonSearch(onButtonSearchClickListener =  {})
        }
//        items(
//            items = {},
//            key = {  }
//        ) { cityItem ->
//            val dismissState = rememberDismissState()
//            val scope = rememberCoroutineScope()
//            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
//                swipeToDismissListener(cityItem)
//                viewModel.sendEvent(CityListEvent.DeleteSearchItem(cityItem))
//
//
//            }
//
//            SwipeToDismiss(
//                modifier = Modifier.animateItemPlacement(),
//                state = dismissState,
//                background = {},
//                directions = setOf(DismissDirection.EndToStart),
//                dismissContent = {
//                    CityItem(
//                        cityItem = cityItem,
//                        onCityItemClickListener = onCityItemClickListener
//                    )
//                }
//            )
//
//
//        }
    }
}

@Composable
private fun CityItem(
    cityItem: City,
    onCityItemClickListener: (City) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCityItemClickListener(cityItem)
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = cityItem.name.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = cityItem.country.toString(),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ButtonSearch(
    onButtonSearchClickListener: () -> Unit,
) {
    Button(onClick = { onButtonSearchClickListener() }) {
        Icon(imageVector = Icons.Filled.Search, contentDescription = null)
        Text(text = stringResource(id = R.string.enter_location))
    }
}