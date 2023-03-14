package barrera.alejandro.cambiadietas.categories.presentation.category_detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import barrera.alejandro.cambiadietas.R
import barrera.alejandro.cambiadietas.core.domain.model.Food
import barrera.alejandro.cambiadietas.core.presentation.theme.LocalSpacing

@Composable
fun CategoryDetailScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: CategoryDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(key1 = Unit) {
        viewModel.loadFoodByCategory()
    }

    Column(
        modifier = modifier
            .padding(top = paddingValues.calculateTopPadding())
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CategoryCard(
            getDrawableId = { foodCategory, foodName ->
                viewModel.getDrawableId(
                    foodCategory,
                    foodName
                )
            },
            category = state.category,
            foods = state.foods
        )
    }
}

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    getDrawableId: (foodCategory: String, foodName: String) -> Int,
    category: String,
    foods: List<Food>
) {
    val spacing = LocalSpacing.current

    Card(
        modifier = modifier.padding(spacing.spaceMedium),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = colorScheme.secondary),
        border = BorderStroke(2.dp, colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(spacing.spaceMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.spaceExtraSmall)
        ) {
            Text(
                text = category.uppercase(),
                style = typography.displayMedium
            )
            Divider(
                color = colorScheme.primary,
                thickness = 1.dp
            )
            FoodFlowRow(
                foods = foods,
                getDrawableId = getDrawableId
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FoodFlowRow(
    modifier: Modifier = Modifier,
    getDrawableId: (foodCategory: String, foodName: String) -> Int,
    foods: List<Food>
) {
    val spacing = LocalSpacing.current

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        maxItemsInEachRow = 2
    ) {
        foods.forEach { food ->
            Card(
                modifier = modifier.padding(spacing.spaceExtraSmall),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = colorScheme.tertiary),
                border = BorderStroke(1.dp, colorScheme.primary),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = modifier
                        .padding(vertical = spacing.spaceMedium)
                        .fillMaxHeight()
                        .height(115.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        space = spacing.spaceExtraSmall,
                        alignment = Alignment.CenterVertically
                    )
                ) {
                    Image(
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp),
                        painter = painterResource(
                            getDrawableId(
                                food.category,
                                food.name
                            )
                        ),
                        contentDescription = stringResource(id = R.string.food_category_image_description),
                        contentScale = ContentScale.Inside
                    )
                    Text(
                        modifier = Modifier
                            .width(120.dp)
                            .padding(spacing.spaceExtraSmall),
                        text = food.name,
                        style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}