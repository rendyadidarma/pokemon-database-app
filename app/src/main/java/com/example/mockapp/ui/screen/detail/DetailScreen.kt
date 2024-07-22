@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mockapp.ui.screen.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mockapp.R
import com.example.mockapp.navigation.NavigationDestination
import com.example.mockapp.ui.CustomTopAppBar
import com.example.mockapp.ui.ErrorScreen
import com.example.mockapp.ui.LoadingScreen
import com.example.mockapp.ui.model.PokemonDetail
import com.example.mockapp.ui.screen.HomeUiState

object DetailDestination : NavigationDestination {
    override val route: String
        get() = "detail"
    override val titleRes: Int
        get() = R.string.app_name

    const val pokemonName = "pokemonName"
    val routeWithArgs = "$route/{$pokemonName}"
}

@Composable
fun DetailScreenRoot(
    viewModel: DetailViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {
    val pokemonDetail = viewModel.state.collectAsState()

    DetailScreenRootScreen(
        state = pokemonDetail.value,
        navigateUp = navigateUp
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun DetailScreenRootScreen(
    modifier: Modifier = Modifier,
    state: DetailUiState,
    navigateUp: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CustomTopAppBar(
                title = if (state is DetailUiState.Success) state.data.name else "",
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ) { _ ->
        when (state) {
            DetailUiState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize())
            DetailUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            is DetailUiState.Success -> {
                DetailBody(
                    detail = state.data,
                    modifier = modifier.fillMaxSize()
                )
            }
        }

    }
}

@Composable
private fun DetailBody(
    detail: PokemonDetail,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        AsyncImage(
            model = detail.sprites.front_default,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(id = R.string.base),
            style = MaterialTheme.typography.headlineSmall
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            detail.stats.forEach {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = it.stat?.name.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = it.base_stat.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Text(
            text = stringResource(id = R.string.abilities),
            style = MaterialTheme.typography.headlineSmall
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            detail.abilities.forEach {
                Card(
                    modifier = modifier,
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = AbsoluteCutCornerShape(16.dp)
                ) {
                    Text(
                        text = it.ability?.name.orEmpty(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}