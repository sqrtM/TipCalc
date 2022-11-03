package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipTimeScreen()
                }
            }
        }
    }
}

private fun calculateTip(amount: Double, tipPercent: Double = 15.0): String {
    val tip = tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Composable
fun EditNumberField(@StringRes label: Int, value: String, onValueChange: (String) -> Unit, ime: ImeAction, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ime),
        keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)}, onDone = {focusManager.clearFocus()})
    )
}

@Composable
fun RoundTheTipRow(roundUp: Boolean, onRoundUpChanged: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .size(48.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(id = R.string.round_up_tip))
        Switch(modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(), checked = roundUp, onCheckedChange = onRoundUpChanged)
    }
}

@Composable
fun TipTimeScreen() {
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercent)
    var roundUp by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(16.dp))
        EditNumberField(label = R.string.bill_amount, value = amountInput, onValueChange = { amountInput = it }, ime = ImeAction.Next)
        Spacer(Modifier.height(24.dp))
        EditNumberField(label = R.string.how_was_the_service, value = tipInput, onValueChange = { tipInput = it }, ime = ImeAction.Done)
        Spacer(Modifier.height(24.dp))
        RoundTheTipRow(roundUp = roundUp, onRoundUpChanged = { roundUp = it })
        Text(
            text = stringResource(id = R.string.tip_amount, tip),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipTimeTheme {
        TipTimeScreen()
    }
}