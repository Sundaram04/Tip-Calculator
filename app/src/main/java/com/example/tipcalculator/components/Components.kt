package com.example.tipcalculator.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.MyApp
import com.example.tipcalculator.R


@Composable
fun InputField(
    modifier: Modifier = Modifier,  // default and Customizes the appearance or layout of the input field.
    valueState: MutableState<String>,   // Holds the current value of the text input.
    labelId: String,    //Provides a label or hint for the input field.
    enabled: Boolean,   // Determines whether the input field is interactive.
    isSingleLine: Boolean,      // Controls whether the input field allows single-line input.
    keyboardType: KeyboardType = KeyboardType.Number,   // Specifies the type of keyboard to display (e.g., number, text).
    imeAction: ImeAction = ImeAction.Next,  // Defines the IME action (like "Next", "Done") for the keyboard.
    onAction: KeyboardActions = KeyboardActions.Default // Handles actions triggered by the IME (e.g., what happens when "Next" is pressed).
){
    OutlinedTextField(
        modifier = modifier
            .padding(top = 1.dp, bottom = 15.dp, start = 15.dp, end = 15.dp)
            .fillMaxWidth(),
        value = valueState.value,
        onValueChange = { valueState.value = it},
        label = { Text(text = labelId)},
        leadingIcon = {
            Icon(painter = painterResource(id = R.drawable.baseline_attach_money_24), contentDescription = "Money Icon")
        },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = onAction
    )

}

@Preview(showBackground = true)
@Composable
fun InputPreview() {
    val valueState = remember {
        mutableStateOf("ABC")
    }
    InputField(valueState = valueState, labelId = "Enter A", enabled = true, isSingleLine = true)
}