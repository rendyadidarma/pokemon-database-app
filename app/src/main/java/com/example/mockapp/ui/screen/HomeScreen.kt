@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mockapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mockapp.R
import com.example.mockapp.navigation.NavigationDestination
import com.example.mockapp.ui.CustomTopAppBar
import com.example.mockapp.ui.ErrorScreen
import com.example.mockapp.ui.LoadingScreen
import com.example.mockapp.ui.model.Pokemon
import com.example.mockapp.ui.theme.MockAppTheme

object HomeDestination : NavigationDestination {
    override val route: String
        get() = "home"
    override val titleRes: Int
        get() = R.string.pokemon
}

@Composable
fun HomeScreenRoot(
    navigateToDetail: (String) -> Unit,
    viewModel: HomeViewModel =
        hiltViewModel()
) {
    val pokemonList = viewModel.state.collectAsState()

    HomeScreenRootScreen(
        state = pokemonList.value,
        navigateToDetail = navigateToDetail,
        loadMore = { viewModel.loadMore() },
        loadingState = viewModel._loadingMoreState.value
    )
}

@Composable
private fun HomeScreenRootScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    navigateToDetail: (String) -> Unit,
    loadMore: () -> Unit,
    loadingState: Boolean
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        when (state) {
            HomeUiState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize())
            HomeUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            is HomeUiState.Success -> {
                HomeBody(
                    itemList = state.data,
                    onItemClick = navigateToDetail,
                    modifier = modifier.fillMaxSize(),
                    contentPadding = innerPadding,
                    loadMore = loadMore,
                    loadingState = loadingState
                )
            }
        }

    }
}

@Composable
private fun HomeBody(
    itemList: List<Pokemon>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    loadMore: () -> Unit,
    loadingState: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (itemList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_items_found),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            PokemonList(
                itemList = itemList,
                onItemClick = { onItemClick(it.name) },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = 8.dp),
                loadMore = loadMore,
                loadingState = loadingState
            )
        }
    }
}

@Composable
fun PokemonList(
    itemList: List<Pokemon>,
    onItemClick: (Pokemon) -> Unit,
    contentPadding: PaddingValues,
    loadMore: () -> Unit,
    loadingState: Boolean,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // observe list scrolling
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(reachedBottom) {
        if (reachedBottom) loadMore()
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = itemList, key = { it.name }) { pokemon ->
            PokemonItem(
                item = pokemon,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onItemClick(pokemon) }
            )
        }

        item {
            if (loadingState) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun PokemonItem(
    item: Pokemon, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = item.url,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.weight(1f)
            )

            Text(text = item.name, modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MockAppTheme {
    }
}