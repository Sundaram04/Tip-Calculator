package com.example.tipcalculator

import android.graphics.Paint.Align
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.components.InputField
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import com.example.tipcalculator.util.calculateTotalPerPerson
import com.example.tipcalculator.util.calculateTotalTip
import com.example.tipcalculator.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    TipCalculatorTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                content()
            }
        }
    }
}


@Composable
fun TopHeader(totalPerPerson: Double){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(225.dp)
            .padding(top = 20.dp, end = 35.dp, start = 35.dp, bottom = 10.dp)
            .padding(WindowInsets.statusBars.asPaddingValues())
            // clip will make rounded corner
            //.clip(shape = RoundedCornerShape(CornerSize(12.dp)))
            // other way
            .clip(shape = CircleShape.copy(all = CornerSize(5.dp))),
        color = Color(0xFFE9D7F7),
        border = BorderStroke(width = 3.dp, color = Color(0xFFD2B3E2))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson) // will return value of total per person to two decimal places
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview
@Composable
fun MainContent() {
    val range = IntRange(start = 1, endInclusive = 100)
    val tipAmountState = remember { mutableStateOf(0.0) }
    val totalPerPersonState = remember { mutableStateOf(0.0) }
    var splitCounter = remember { mutableStateOf(1) }

    BillForm(
        splitCounter = splitCounter,
        tipAmountState = tipAmountState,
        totalPerPersonState = totalPerPersonState,
        range = range
    ) {
    }

}
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    splitCounter: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    onValChange: (String) -> Unit = {}
) {
    val totalBillState = remember{ mutableStateOf("") }
    val validState = remember(totalBillState.value){
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember { mutableStateOf(0f) }
    var tipPercentage = (sliderPositionState.value * 100).toInt()

    if(!validState) {totalPerPersonState.value = 0.0}
    TopHeader(totalPerPerson = totalPerPersonState.value)

    Surface(
        modifier = modifier
            .padding(10.dp),
            //.padding(WindowInsets.statusBars.asPaddingValues()),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 3.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions{
                    if(!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())
                    keyboardController?.hide()
                }
            )
            if(validState){
                Row(
                    modifier = modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start
                ){
                    Text(
                        text = "Split",
                        modifier = modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = modifier.width(120.dp))
                    Row(
                        modifier = modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End,
                        ){
                        RoundIconButton(
                            painter = painterResource(id = R.drawable.baseline_remove_24),
                            onClick = {
                                if(splitCounter.value > 1){
                                    splitCounter.value--
                                }else{
                                    splitCounter.value = 1
                                }
                                totalPerPersonState.value =
                                    calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitCounter.value,
                                        tipPercentage = tipPercentage
                                    )
                            }
                        )
                        Text(
                            text = "${splitCounter.value}",
                            modifier = modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )
                        RoundIconButton(
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            onClick = {
                                if(splitCounter.value < range.last){
                                    splitCounter.value++
                                    totalPerPersonState.value / splitCounter.value
                                }
                                totalPerPersonState.value =
                                    calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitCounter.value,
                                        tipPercentage = tipPercentage
                                    )
                            }
                        )

                    }
                }
            // Tip Row
            Row(
                modifier = modifier
                .padding(horizontal = 3.dp, vertical = 12.dp)
            ){
                Text(
                    text = "Tip",
                    modifier = modifier
                        .align(alignment = Alignment.CenterVertically)
                )
                Spacer(modifier = modifier.width(195.dp))
                Text(
                    text = "$${tipAmountState.value}",
                    modifier = modifier
                        .align(alignment = Alignment.CenterVertically)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "$tipPercentage%"
                )
                Spacer(modifier = Modifier.height(14.dp))
                //Slider
                Slider(
                    value = sliderPositionState.value,
                    onValueChange = {newVal ->
                        sliderPositionState.value = newVal
                        tipPercentage = (newVal * 100.0).toInt()
                        tipAmountState.value = calculateTotalTip(
                            totalBill = totalBillState.value.toDouble(),
                            tipPercentage = tipPercentage
                        )
                        totalPerPersonState.value =
                            calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitCounter.value,
                                tipPercentage = tipPercentage
                            )
                    },
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp),
                    //steps = 5,
                    onValueChangeFinished = {

                    }
                )

            }
            }else {
                Box{}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApp {
        Text(text = "Hello World")
    }
}