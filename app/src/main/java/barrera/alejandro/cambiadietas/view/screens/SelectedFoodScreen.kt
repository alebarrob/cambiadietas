package barrera.alejandro.cambiadietas.view.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import barrera.alejandro.cambiadietas.R
import barrera.alejandro.cambiadietas.model.data.Food
import barrera.alejandro.cambiadietas.view.commonui.CambiaDietasContainer
import barrera.alejandro.cambiadietas.view.commonui.CambiaDietasFoodColumn
import barrera.alejandro.cambiadietas.view.theme.Aquamarine
import barrera.alejandro.cambiadietas.view.theme.KellyGreen
import barrera.alejandro.cambiadietas.viewmodel.CommonUiViewModel
import barrera.alejandro.cambiadietas.viewmodel.SelectedFoodScreenViewModel
import java.lang.reflect.Field

@Composable
fun SelectedFoodScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    foodCategory: String,
    food: Food,
    foodItems: List<Food>,
    commonUiViewModel: CommonUiViewModel,
    context: Context
) {
    val selectedFoodScreenViewModel = SelectedFoodScreenViewModel()

    val foodAmount by selectedFoodScreenViewModel.foodAmount.observeAsState(initial = "")
    val foodUnit by selectedFoodScreenViewModel.foodUnit.observeAsState(initial = "")
    val alternativeFood by selectedFoodScreenViewModel.alternativeFood.observeAsState(
        initial = Food(
            imageId = R.drawable.food_image_placeholder,
            nameId = R.string.food_text_placeholder,
            equivalentAmount = 0.00
        )
    )
    val alternativeFoodAmount by selectedFoodScreenViewModel.alternativeFoodAmount.observeAsState(initial = "")
    val alternativeFoodUnit by selectedFoodScreenViewModel.alternativeFoodUnit.observeAsState(initial = "")
    val wrongInput by selectedFoodScreenViewModel.wrongInput.observeAsState(initial = false)

    selectedFoodScreenViewModel.loadFoodUnit(stringResource(id = food.nameId))
    selectedFoodScreenViewModel.loadAlternativeFoodUnit(stringResource(id = alternativeFood.nameId))

    CambiaDietasContainer(
        modifier = modifier,
        paddingValues = paddingValues
    ) {
        FoodComparator(
            foodCategory = foodCategory,
            food = food,
            foodAmount = foodAmount,
            onFoodAmountChange = {
                selectedFoodScreenViewModel.onFoodAmountChange(
                    foodAmount = it,
                    foodCategory = foodCategory,
                    food = food,
                    alternativeFood = alternativeFood
                )
                if (wrongInput) {
                    Toast.makeText(context, "Has introducido un valor incorrecto", Toast.LENGTH_SHORT).show()
                }
            },
            foodUnit = foodUnit,
            alternativeFood = alternativeFood,
            onAlternativeFoodChange = {
                selectedFoodScreenViewModel.onAlternativeFoodChange(it)
                selectedFoodScreenViewModel.updateAlternativeFoodAmount(
                    foodAmount = foodAmount,
                    foodCategory = foodCategory,
                    food = food,
                    alternativeFood = alternativeFood
                )
            },
            alternativeFoodAmount = alternativeFoodAmount,
            alternativeFoodUnit = alternativeFoodUnit,
            foodItems = foodItems,
            wrongInput = wrongInput,
            commonUiViewModel = commonUiViewModel
        )
    }
}

@Composable
fun FoodComparator(
    modifier: Modifier = Modifier,
    foodCategory: String,
    food: Food,
    foodAmount: String,
    onFoodAmountChange: (String) -> Unit,
    foodUnit: String,
    alternativeFood: Food,
    onAlternativeFoodChange: (Food) -> Unit,
    alternativeFoodAmount: String,
    alternativeFoodUnit: String,
    foodItems: List<Food>,
    wrongInput: Boolean,
    commonUiViewModel: CommonUiViewModel
) {
    Card(
        modifier = modifier.padding(start = 30.dp, top = 4.dp, end = 30.dp, bottom = 15.dp),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = Aquamarine
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FoodImageComparator(
                food = food,
                foodAmount = foodAmount,
                onFoodAmountChange = onFoodAmountChange,
                foodUnit = foodUnit,
                alternativeFood = alternativeFood,
                alternativeFoodAmount = alternativeFoodAmount,
                alternativeFoodUnit = alternativeFoodUnit,
                wrongInput = wrongInput
            )
            Text(
                text = stringResource(id = R.string.food_comparator_question),
                fontSize = 20.sp
            )
            CambiaDietasFoodColumn(
                foodCategory = foodCategory,
                onAlternativeFoodChange = onAlternativeFoodChange,
                foodItems = foodItems,
                commonUiViewModel = commonUiViewModel
            )
        }
    }
}

@Composable
fun FoodImageComparator(
    modifier: Modifier = Modifier,
    food: Food,
    foodAmount: String,
    onFoodAmountChange: (String) -> Unit,
    foodUnit: String,
    alternativeFood: Food,
    alternativeFoodAmount: String,
    alternativeFoodUnit: String,
    wrongInput: Boolean
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FoodQuantityCard(
            anyFood = food,
            foodAmount = foodAmount,
            onFoodAmountChange = onFoodAmountChange,
            measurementUnit = foodUnit,
            enabled = true,
            wrongInput = wrongInput
        )
        Image(
            painter = painterResource(id = R.drawable.arrow),
            contentDescription = null
        )
        FoodQuantityCard(
            anyFood = alternativeFood,
            foodAmount = alternativeFoodAmount,
            onFoodAmountChange = { },
            measurementUnit = alternativeFoodUnit,
            enabled = false,
            wrongInput = wrongInput
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FoodQuantityCard(
    modifier: Modifier = Modifier,
    anyFood: Food,
    foodAmount: String,
    onFoodAmountChange: (String) -> Unit,
    measurementUnit: String,
    enabled: Boolean,
    wrongInput: Boolean
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Card(
        shape = MaterialTheme.shapes.medium,
        backgroundColor = Color.White,
        elevation = 5.dp
    ) {
        Column(
            modifier = modifier.padding(top = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Image(
                painter = painterResource(id = anyFood.imageId),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 5.dp),
                text = stringResource(id = anyFood.nameId)
            )
            TextField(
                value = foodAmount,
                onValueChange = onFoodAmountChange,
                modifier = Modifier.width(120.dp),
                enabled = enabled,
                label = { Text(text = measurementUnit) },
                isError = wrongInput,
                shape = RectangleShape,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = KellyGreen),
            )
        }
    }
}

fun getResId(resName: String, c: Class<*>): Int {
    return try {
        val idField: Field = c.getDeclaredField(resName)
        idField.getInt(idField)
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }
}