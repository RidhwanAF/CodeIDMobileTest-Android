package com.raf.mobiletaskcodeidtest.home.presentation.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.raf.mobiletaskcodeidtest.home.presentation.components.ErrorItemView
import com.raf.mobiletaskcodeidtest.home.presentation.components.LoadingItemView
import com.raf.mobiletaskcodeidtest.home.presentation.components.PokemonItemView
import com.raf.mobiletaskcodeidtest.home.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    animatedContentScope: AnimatedContentScope,
    parentPaddingValues: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigationDetail: (id: String, name: String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pokemonItems = viewModel.pokemonPager.collectAsLazyPagingItems()

    val searchResult by viewModel.pokemonSearchResult.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    var showSearchBox by rememberSaveable {
        mutableStateOf(false)
    }
    val searchBoxPaddingTop by animateDpAsState(
        targetValue = if (scrollBehavior.state.collapsedFraction >= 0.5f) 32.dp else 0.dp
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "POKEMON")
                },
                actions = {
                    IconButton(
                        onClick = {
                            showSearchBox = !showSearchBox
                            focusManager.clearFocus()
                            if (!showSearchBox) {
                                viewModel.onSearchInputChange("")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (showSearchBox) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                AnimatedVisibility(
                    visible = showSearchBox,
                    enter = fadeIn() + slideInVertically { -it },
                    exit = slideOutVertically { -it } + fadeOut()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(top = searchBoxPaddingTop)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                viewModel.onSearchInputChange(it)
                            },
                            placeholder = {
                                Text(text = "Search Pokemon...")
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions {
                                this.defaultKeyboardAction(imeAction = ImeAction.Done)
                                focusManager.clearFocus()
                            },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        FilledTonalIconButton(
                            onClick = {
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                }
            }

            if (searchResult.isNotEmpty()) {
                items(searchResult) { pokemon ->
                    PokemonItemView(
                        modifier = Modifier
                            .animateItem(),
                        animatedContentScope = animatedContentScope,
                        id = pokemon.id,
                        name = pokemon.name,
                        onClick = {
                            onNavigationDetail(pokemon.id, pokemon.name)
                        }
                    )
                }
            } else if (searchQuery.isNotBlank()) {
                item {
                    Text(
                        text = "No Pokemon Found",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .animateItem()
                    )
                }
            }

            if (searchQuery.isBlank()) {
                items(pokemonItems.itemCount) { index ->
                    val pokemon = pokemonItems[index]
                    if (pokemon != null) {
                        PokemonItemView(
                            animatedContentScope = animatedContentScope,
                            modifier = Modifier
                                .animateItem(),
                            id = pokemon.id,
                            name = pokemon.name,
                            onClick = {
                                onNavigationDetail(pokemon.id, pokemon.name)
                            }
                        )
                    } else {
                        LoadingItemView(
                            modifier = Modifier
                                .animateItem()
                        )
                    }
                }

                pokemonItems.apply {
                    when (loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                LoadingItemView(
                                    modifier = Modifier
                                        .animateItem()
                                )
                            }
                        }

                        is LoadState.Error -> {
                            item {
                                ErrorItemView(
                                    modifier = Modifier
                                        .animateItem()
                                )
                            }
                        }

                        else -> {/*NO-OP*/
                        }
                    }
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(parentPaddingValues.calculateBottomPadding())
                )
            }
        }
    }
}