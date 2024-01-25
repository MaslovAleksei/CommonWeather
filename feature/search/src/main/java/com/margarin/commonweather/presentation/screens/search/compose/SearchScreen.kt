package com.margarin.commonweather.presentation.screens.search.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.margarin.commonweather.domain.SearchModel
import com.margarin.commonweather.presentation.screens.search.SearchEvent
import com.margarin.commonweather.presentation.screens.search.SearchScreenState
import com.margarin.commonweather.presentation.screens.search.SearchViewModel
import com.margarin.search.R

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onChipClick: (String) -> Unit,
    onCardClickListener: (String) -> Unit,
    onButtonAddClickListener: (SearchModel) -> Unit
) {

    val screenState = viewModel.state.collectAsState(SearchScreenState.SearchesList(listOf()))
    SearchScreenContent(
        viewModel = viewModel,
        onChipClick = onChipClick,
        onCardClickListener = onCardClickListener,
        onButtonAddClickListener = onButtonAddClickListener,
        screenState = screenState
    )
}

@Composable
private fun SearchScreenContent(
    viewModel: SearchViewModel,
    onChipClick: (String) -> Unit,
    onCardClickListener: (String) -> Unit,
    onButtonAddClickListener: (SearchModel) -> Unit,
    screenState: State<SearchScreenState>
) {
    val currentState = screenState.value
    if (currentState is SearchScreenState.SearchesList) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SearchBar(
                viewModel = viewModel,
                queryList = currentState.queryList ?: listOf(),
                onCardClickListener = onCardClickListener,
                onButtonAddClickListener = onButtonAddClickListener
            )
            Spacer(modifier = Modifier.padding(vertical = 16.dp))
            Text(text = stringResource(id = R.string.popular_cities))
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            FlowRowList(onChipClick = onChipClick)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    viewModel: SearchViewModel,
    queryList: List<SearchModel>,
    onCardClickListener: (String) -> Unit,
    onButtonAddClickListener: (SearchModel) -> Unit
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
                viewModel.sendEvent(SearchEvent.OnQuery(text))
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
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = queryList,
                    key = { it.id }
                ) { searchItem ->
                    SearchItem(
                        searchItem = searchItem,
                        onCardClickListener = onCardClickListener,
                        onButtonAddClickListener = onButtonAddClickListener
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchItem(
    searchItem: SearchModel,
    onCardClickListener: (String) -> Unit,
    onButtonAddClickListener: (SearchModel) -> Unit
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
    onChipClick: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(
            16.dp,
            Alignment.CenterHorizontally
        )
    ) {
        ChipItem(cityName = stringResource(R.string.kazan), onChipClick)
        ChipItem(cityName = stringResource(R.string.moscow), onChipClick)
        ChipItem(cityName = stringResource(R.string.saint_petersburg), onChipClick)
        ChipItem(cityName = stringResource(R.string.tashkent), onChipClick)
        ChipItem(cityName = stringResource(R.string.kyiv), onChipClick)
        ChipItem(cityName = stringResource(R.string.ekaterinburg), onChipClick)
        ChipItem(cityName = stringResource(R.string.novosibirsk), onChipClick)
        ChipItem(cityName = stringResource(R.string.minsk), onChipClick)
        ChipItem(cityName = stringResource(R.string.almaty), onChipClick)
        ChipItem(cityName = stringResource(R.string.ashkhabad), onChipClick)
        ChipItem(cityName = stringResource(R.string.omsk), onChipClick)
        ChipItem(cityName = stringResource(R.string.chelyabinsk), onChipClick)
        ChipItem(cityName = stringResource(R.string.perm), onChipClick)
        ChipItem(cityName = stringResource(R.string.samara), onChipClick)
        ChipItem(cityName = stringResource(R.string.tallinn), onChipClick)
        ChipItem(cityName = stringResource(R.string.tbilisi), onChipClick)
        ChipItem(cityName = stringResource(R.string.tyumen), onChipClick)
        ChipItem(cityName = stringResource(R.string.volgograd), onChipClick)
        ChipItem(cityName = stringResource(R.string.voronezh), onChipClick)
        ChipItem(cityName = stringResource(R.string.vilnius), onChipClick)
        ChipItem(cityName = stringResource(R.string.krasnoyarsk), onChipClick)
        ChipItem(cityName = stringResource(R.string.riga), onChipClick)
        ChipItem(cityName = stringResource(R.string.bucharest), onChipClick)
        ChipItem(cityName = stringResource(R.string.bishkek), onChipClick)
        ChipItem(cityName = stringResource(R.string.astana), onChipClick)
        ChipItem(cityName = stringResource(R.string.baku), onChipClick)
        ChipItem(cityName = stringResource(R.string.erevan), onChipClick)
    }
}

@Composable
private fun ChipItem(
    cityName: String,
    onChipClick: (String) -> Unit
) {
    AssistChip(
        onClick = { onChipClick(cityName) },
        label = { Text(text = cityName) }
    )
}
