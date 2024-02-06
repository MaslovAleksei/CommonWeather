package com.margarin.commonweather.presentation.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.margarin.commonweather.search.City
import com.margarin.search.R


@Composable
fun SearchContent(component: SearchComponent) {


    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SearchBar(

        )
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        Text(text = stringResource(id = R.string.popular_cities))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        FlowRowList(

        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
) {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Box {
        DockedSearchBar(
            modifier = Modifier
                .fillMaxWidth(),
            query = text,
            onQueryChange = {
                text = it
                //viewModel.sendEvent(SearchEvent.OnQuery(text))
            },
            onSearch = { },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text(stringResource(id = R.string.search)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (text.isNotEmpty()) {
                                text = ""
                            } else {
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon"
                    )
                }
            }
        ) {
//            val currentState = screenState.value
//            if (currentState is SearchScreenState.SearchesList) {
//                LazyColumn(
//                    contentPadding = PaddingValues(12.dp),
//                    verticalArrangement = Arrangement.spacedBy(4.dp)
//                ) {
//                    items(
//                        items = currentState.queryList ?: listOf(),
//                        key = { it.id }
//                    ) { searchItem ->
//                        SearchItem(
//                            searchItem = searchItem,
//                            onCardClickListener = onCardClickListener,
//                            onButtonAddClickListener = onButtonAddClickListener
//                        )
//                    }
//                }
//            }
        }
    }
}

@Composable
private fun SearchItem(
    searchItem: City,
    onCardClickListener: (String) -> Unit,
    onButtonAddClickListener: (City) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onCardClickListener(searchItem.name.toString()) }
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = searchItem.name.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${searchItem.name}, ${searchItem.country}")
        }
        IconButton(onClick = { onButtonAddClickListener(searchItem) }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRowList(
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(
            16.dp,
            Alignment.Start
        )
    ) {
//        val context = LocalContext.current
//        Button(onClick = {
//            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//            val currentState = screenState.value
//            viewModel.sendEvent(SearchEvent.UseGps(fusedLocationClient = fusedLocationClient))
//            if (currentState is SearchScreenState.Close) {
//                onButtonBackClickListener()
//            }
//        }) {
//            Text(text = "Define")
//        }
//        CityItemButton(cityName = stringResource(R.string.kazan), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.moscow), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.saint_petersburg), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.tashkent), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.kyiv), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.ekaterinburg), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.novosibirsk), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.minsk), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.almaty), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.ashkhabad), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.omsk), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.chelyabinsk), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.perm), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.samara), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.tallinn), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.tbilisi), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.tyumen), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.volgograd), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.voronezh), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.vilnius), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.krasnoyarsk), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.riga), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.bucharest), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.bishkek), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.astana), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.baku), onChipClick)
//        CityItemButton(cityName = stringResource(R.string.erevan), onChipClick)
    }
}

@Composable
private fun CityItemButton(
    cityName: String,
    onChipClick: (String) -> Unit
) {
    Button(onClick = { onChipClick(cityName) }) {
        Text(text = cityName)
    }
}
